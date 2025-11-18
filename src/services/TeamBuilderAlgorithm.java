package services;

import base.Participant;
import utilities.Interest;
import utilities.PersonalityType;
import utilities.Role;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class TeamBuilderAlgorithm {
    private static final int TEAM_INTEREST_CAP = 2;
    private static final int MIN_ROLES_FOR_BIG_TEAMS = 3;

    public static class TeamFormationResult {
        private final List<List<Participant>> formedTeams;
        private final List<Participant> pooledParticipants;

        public TeamFormationResult(List<List<Participant>> formedTeams, List<Participant> pooledParticipants) {
            this.formedTeams = formedTeams != null ? formedTeams : new ArrayList<>();
            this.pooledParticipants = pooledParticipants != null ? pooledParticipants : new ArrayList<>();
        }

        public List<List<Participant>> getFormedTeams() {
            return formedTeams;
        }

        public List<Participant> getPooledParticipants() {
            return pooledParticipants;
        }

        public boolean hasPooledParticipants() {
            return !pooledParticipants.isEmpty();
        }

        public int getTotalTeamsFormed() {
            return formedTeams.size();
        }

        public int getTotalParticipantsAssigned() {
            return formedTeams.stream().mapToInt(List::size).sum();
        }
    }

    public static TeamFormationResult formTeams(List<Participant> participants, int teamSize) {
        List<Participant> pooledParticipants = new ArrayList<>();

        if (participants == null || participants.isEmpty()) {
            System.out.println("⚠️  No participants provided for team formation.");
            return new TeamFormationResult(new ArrayList<>(), pooledParticipants);
        }

        if (teamSize <= 0) {
            System.out.println("⚠️  Invalid team size: " + teamSize);
            return new TeamFormationResult(new ArrayList<>(), pooledParticipants);
        }

        int completeTeams = participants.size() / teamSize;

        List<List<Participant>> formedTeams = new ArrayList<>();
        for (int i = 0; i < completeTeams; i++) {
            formedTeams.add(new ArrayList<>());
        }

        if (formedTeams.isEmpty()) {
            System.out.println("⚠️  Not enough participants to form complete formedTeams.");
            System.out.println("   All " + participants.size() + " participant(s) pooled.");
            pooledParticipants.addAll(participants);
            return new TeamFormationResult(new ArrayList<>(), pooledParticipants);
        }

        List<Participant> participantsSorted = manageParticipantImportance(participants);

        int participantsToAssign = completeTeams * teamSize;
        for (int i = 0; i < participantsToAssign; i++) {
            Participant participant = participantsSorted.get(i);
            int bestTeamInd = getBestTeamForParticipant(participant, formedTeams, teamSize);
            formedTeams.get(bestTeamInd).add(participant);
        }

        for (int i = participantsToAssign; i < participants.size(); i++) {
            pooledParticipants.add(participantsSorted.get(i));
        }

        formedTeams.removeIf(List::isEmpty);

        return new TeamFormationResult(formedTeams, pooledParticipants);
    }

    private static List<Participant> manageParticipantImportance(List<Participant> participants) {
        List<Participant> peopleSorted = new ArrayList<>(participants);

        peopleSorted.sort((participant1, participant2) -> {
            if (participant1 == null || participant2 == null) {
                return 0;
            }

            PersonalityType type1 = participant1.getPersonalityType();
            PersonalityType type2 = participant2.getPersonalityType();

            if (type1 == null || type2 == null) {
                return 0;
            }

            int prior1 = getPersonalityImportance(type1);
            int prior2 = getPersonalityImportance(type2);

            if (prior1 != prior2) {
                return Integer.compare(prior1, prior2);
            }

            return Integer.compare(participant2.getSkillLevel(), participant1.getSkillLevel());
        });

        return peopleSorted;
    }

    private static int getPersonalityImportance(PersonalityType personalityType) {
        if (personalityType == null) {
            return 999;
        }

        return switch (personalityType) {
            case LEADER -> 1;
            case THINKER -> 2;
            case BALANCED -> 3;
        };
    }

    private static int getBestTeamForParticipant(Participant participant,
                                                 List<List<Participant>> formedTeams,
                                                 int teamSize) {
        if (participant == null || formedTeams == null || formedTeams.isEmpty()) {
            return 0;
        }

        int bestTeamInd = 0;
        double optimalScore = Double.NEGATIVE_INFINITY;

        for (int i = 0; i < formedTeams.size(); i++) {
            List<Participant> team = formedTeams.get(i);

            if (team == null || team.size() >= teamSize) {
                continue;
            }

            double score = computeTeamScore(participant, team, teamSize);

            if (score > optimalScore) {
                optimalScore = score;
                bestTeamInd = i;
            }
        }

        return bestTeamInd;
    }

    private static double computeTeamScore(Participant participant,
                                           List<Participant> team,
                                           int teamSize) {
        if (participant == null || team == null) {
            return 0;
        }

        double score = 0;

        score += scorePersonalityMatch(participant, team, teamSize);
        score += scoreInterestDiversity(participant, team);
        score += scoreRoleDiversity(participant, team, teamSize);
        score += scoreSkillBalance(participant, team);

        if (team.isEmpty()) {
            score += 50;
        }

        return score;
    }

    private static double scoreInterestDiversity(Participant participant, List<Participant> team) {
        if (participant == null || team == null) {
            return 0;
        }

        double score = 0;
        Interest participantInterest = participant.getInterest();

        if (participantInterest == null) {
            return 0;
        }

        long sameInterestCount = team.stream()
                .filter(p -> p != null && p.getInterest() == participantInterest)
                .count();

        if (sameInterestCount == 0) {
            score += 100;
        } else if (sameInterestCount == 1) {
            score += 50;
        } else if (sameInterestCount >= TEAM_INTEREST_CAP) {
            score -= 200;
        }

        Set<Interest> uniqueInterests = new HashSet<>();
        for (Participant p : team) {
            if (p != null && p.getInterest() != null) {
                uniqueInterests.add(p.getInterest());
            }
        }
        score += uniqueInterests.size() * 20;

        return score;
    }

    private static double scoreRoleDiversity(Participant participant, List<Participant> team, int teamSize) {
        if (participant == null || team == null) {
            return 0;
        }

        double score = 0;
        Role participantRole = participant.getRole();

        if (participantRole == null) {
            return 0;
        }

        long sameRoleCount = team.stream()
                .filter(p -> p != null && p.getRole() == participantRole)
                .count();

        if (sameRoleCount == 0) {
            score += 80;
        } else if (sameRoleCount == 1) {
            score += 20;
        } else {
            score -= 40;
        }

        Set<Role> uniqueRoles = new HashSet<>();
        for (Participant p : team) {
            if (p != null && p.getRole() != null) {
                uniqueRoles.add(p.getRole());
            }
        }

        if (teamSize > 5 && uniqueRoles.size() < MIN_ROLES_FOR_BIG_TEAMS) {
            score += 60;
        } else {
            score += uniqueRoles.size() * 15;
        }

        return score;
    }

    private static double scorePersonalityMatch(Participant participant, List<Participant> team, int teamSize) {
        if (participant == null || team == null) {
            return 0;
        }

        double score = 0;
        PersonalityType personalityType = participant.getPersonalityType();

        if (personalityType == null) {
            return 0;
        }

        long leaderCount = team.stream()
                .filter(p -> p != null && p.getPersonalityType() == PersonalityType.LEADER)
                .count();
        long thinkerCount = team.stream()
                .filter(p -> p != null && p.getPersonalityType() == PersonalityType.THINKER)
                .count();
        long balancedCount = team.stream()
                .filter(p -> p != null && p.getPersonalityType() == PersonalityType.BALANCED)
                .count();

        switch (personalityType) {
            case LEADER:
                if (leaderCount == 0) {
                    score += 200;
                } else {
                    score -= 150;
                }
                break;
            case THINKER:
                if (thinkerCount == 0) {
                    score += 180;
                } else if (thinkerCount == 1) {
                    score += 150;
                } else {
                    score -= 80;
                }
                break;
            case BALANCED:
                score += 100;
                if (balancedCount > teamSize / 2) {
                    score -= 40;
                }
                break;
        }

        return score;
    }

    private static double scoreSkillBalance(Participant participant, List<Participant> team) {
        if (participant == null || team == null || team.isEmpty()) {
            return 0;
        }

        double avgTeamSkillScore = team.stream()
                .filter(p -> p != null)
                .mapToInt(Participant::getSkillLevel)
                .average()
                .orElse(5.0);

        double skillGap = Math.abs(participant.getSkillLevel() - avgTeamSkillScore);

        if (skillGap <= 2) {
            return 50;
        } else if (skillGap <= 4) {
            return 20;
        } else {
            return -30;
        }
    }
}