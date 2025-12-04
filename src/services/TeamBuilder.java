package services;

import base.Participant;
import base.Team;
import utilities.PersonalityType;

import java.util.*;

/*
This class:
    - Contains the algorithm for forming balanced teams from participants
    - Parallel team forming is handled through the formationRunner.
    - Sorts participants by personality importance and skill.
    - Assigns participants to teams based on scoring criteria.
    - Pools leftover participants if they cannot be assigned.

The scoring considers personality mix, interest diversity, role distribution,and skill balance to encourage fair and effective teams.
 */

public class TeamBuilder {
    private static final int TEAM_INTEREST_CAP = 2;
    private static final int MIN_ROLES_FOR_BIG_TEAMS = 3;


    public static class TeamFormationResult {
        private final List<Team> formedTeams;
        private final List<Participant> pooledParticipants;

        public TeamFormationResult(List<Team> formedTeams, List<Participant> pooledParticipants) {
            this.formedTeams = formedTeams != null ? formedTeams : new ArrayList<>();
            this.pooledParticipants = pooledParticipants != null ? pooledParticipants : new ArrayList<>();
        }

        public List<Team> getFormedTeams() {
            return formedTeams;
        }

        public List<Participant> getPooledParticipants() {
            return pooledParticipants;
        }

        public boolean hasPooledParticipants() {
            return !pooledParticipants.isEmpty();
        }
    }

    /*
     Forms the teams from a list of participants.
     */

    public static TeamFormationResult formTeams(List<Participant> participants, int teamSize) {
        List<Participant> pooled = new ArrayList<>();

        if (participants == null || participants.isEmpty()) {
            return new TeamFormationResult(new ArrayList<>(), pooled);
        }

        if (teamSize <= 0) {
            return new TeamFormationResult(new ArrayList<>(), pooled);
        }

        int completeTeams = participants.size() / teamSize;
        List<Team> teams = new ArrayList<>();

        for (int i = 0; i < completeTeams; i++) {
            teams.add(new Team(i + 1));
        }

        if (teams.isEmpty()) {
            pooled.addAll(participants);
            return new TeamFormationResult(new ArrayList<>(), pooled);
        }

        // Sort by personality importance first, then by skill of the participant
        List<Participant> sorted = manageParticipantImportance(participants);

        int maxAssign = completeTeams * teamSize;

        for (int i = 0; i < maxAssign; i++) {
            Participant p = sorted.get(i);
            int bestIndex = getBestTeamForParticipant(p, teams, teamSize);
            teams.get(bestIndex).addMember(p);
        }

        // pool
        for (int i = maxAssign; i < sorted.size(); i++) {
            pooled.add(sorted.get(i));
        }

        return new TeamFormationResult(teams, pooled);
    }



    //Sorts the participants by the attributes for teaming.
    private static List<Participant> manageParticipantImportance(List<Participant> participants) {
        List<Participant> sorted = new ArrayList<>(participants);

        sorted.sort((a, b) -> {
            int pa = getPersonalityImportance(a.getPersonalityType());
            int pb = getPersonalityImportance(b.getPersonalityType());

            if (pa != pb) return Integer.compare(pa, pb);

            return Integer.compare(b.getSkillLevel(), a.getSkillLevel());
        });

        return sorted;
    }
    private static int getPersonalityImportance(PersonalityType p) {
        if (p == null) return 999;
        return switch (p) {
            case LEADER -> 1;
            case THINKER -> 2;
            default -> 3;
        };
    }

    //This finds the best team for a participant based on the scoring
    private static int getBestTeamForParticipant(Participant p, List<Team> teams, int teamSize) {
        int bestIndex = 0;
        double bestScore = Double.NEGATIVE_INFINITY;

        for (int i = 0; i < teams.size(); i++) {
            Team t = teams.get(i);

            if (t.size() >= teamSize) continue;


            double score = computeTeamScore(p, t.getMembers(), teamSize);
            if (score > bestScore) {
                bestScore = score;
                bestIndex = i;
            }
        }

        //considers the capacity and returns the index for optimal team
        return bestIndex;
    }

    //Computes overall score for assigning a participant to a team.
    // Considers perosnality match, interest diversity, role diversity, skill balance
    private static double computeTeamScore(Participant p, List<Participant> team, int teamSize) {
        double score = 0;
        score += scorePersonalityMatch(p, team, teamSize);
        score += scoreInterestDiversity(p, team);
        score += scoreRoleDiversity(p, team, teamSize);
        score += scoreSkillBalance(p, team);

        if (team.isEmpty()) score += 50;

        return score;
    }

    // Scoring helpers
    private static double scoreInterestDiversity(Participant p, List<Participant> team) {
        long same = team.stream().filter(t -> t.getInterest() == p.getInterest()).count();

        if (same == 0) return 100;
        if (same == 1) return 50;
        if (same >= TEAM_INTEREST_CAP) return -200;

        return 0;
    }
    private static double scoreRoleDiversity(Participant p, List<Participant> team, int teamSize) {
        long sameRole = team.stream().filter(t -> t.getRole() == p.getRole()).count();
        double score = (sameRole == 0 ? 80 : sameRole == 1 ? 20 : -40);

        long unique = team.stream().map(Participant::getRole).distinct().count();
        if (teamSize > 5 && unique < MIN_ROLES_FOR_BIG_TEAMS) score += 60;
        else score += unique * 15;

        return score;
    }
    private static double scorePersonalityMatch(Participant p, List<Participant> team, int teamSize) {
        long leaders = team.stream().filter(t -> t.getPersonalityType() == PersonalityType.LEADER).count();
        long thinkers = team.stream().filter(t -> t.getPersonalityType() == PersonalityType.THINKER).count();
        long balanced = team.stream().filter(t -> t.getPersonalityType() == PersonalityType.BALANCED).count();

        return switch (p.getPersonalityType()) {
            case LEADER -> (leaders == 0 ? 200 : -150);
            case THINKER -> (thinkers == 0 ? 180 : thinkers == 1 ? 150 : -80);
            case BALANCED -> 100 - (balanced > teamSize / 2 ? 40 : 0);
        };
    }
    private static double scoreSkillBalance(Participant p, List<Participant> team) {
        if (team.isEmpty()) return 0;

        double avg = team.stream().mapToInt(Participant::getSkillLevel).average().orElse(5);
        double diff = Math.abs(p.getSkillLevel() - avg);

        if (diff <= 2) return 50;
        if (diff <= 4) return 20;
        return -30;
    }
}
