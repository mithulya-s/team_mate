package services;

import base.Participant;
import base.Team;
import csv.TeamCsvReader;

import java.util.List;
import java.util.Scanner;

public class ParticipantLookup {

    private final Scanner scanner;

    public ParticipantLookup(Scanner scanner) {
        this.scanner = scanner;
    }

    public void manageLookupFlow(List<Team> formedTeams) {
        System.out.println("Welcome to participant search.");

        try {
            formedTeams = new TeamCsvReader().readDefaultFile();
            if (formedTeams == null || formedTeams.isEmpty()) {
                System.out.println("Sorry. Teams have not been formed yet.");
                return;
            }
        } catch (Exception e) {
            System.out.println("No formed teams file found. Please check back later.");
            return;
        }

        System.out.println("Please enter your participant ID: ");
        String participantId = scanner.nextLine().trim();

        if (participantId.isEmpty()) {
            System.out.println("ID cannot be empty.");
            return;
        }

        if (participantId.length() < 4) {
            System.out.println("Invalid ID format. Example: P001.");
            return;
        }

        try {
            Participant participant = findParticipantInTeams(participantId, formedTeams);
            Team assignedTeam = findTeamByParticipant(participantId, formedTeams);

            if (participant != null && assignedTeam != null) {

                System.out.println("\n✅ Team Assignment Found!");
                System.out.println("=======================================================");
                System.out.println("📋 You are assigned to Team " + assignedTeam.getTeamNumber());
                System.out.println("=======================================================");
                System.out.println("\n👥 Your Teammates:");

                for (Participant p : assignedTeam.getMembers()) {
                    if (p == null) continue;

                    if (p.getId().equalsIgnoreCase(participantId)) {
                        System.out.printf("  👉 %s (YOU)\n", p.getFullName());
                    } else {
                        System.out.printf("  • %s\n", p.getFullName());
                    }

                    System.out.printf(
                            "      Role: %s | Interest: %s | Skill: %d\n",
                            p.getRole(), p.getInterest(), p.getSkillLevel()
                    );
                }

                System.out.println("\n===============================================");
            }
            else {
                System.out.println("\n❌ No team assignment found for ID: " + participantId);
            }

        } catch (Exception e) {
            System.err.println("An error occurred during lookup: " + e.getMessage());
        }
    }

    public Participant findParticipantInTeams(String id, List<Team> formedTeams) {
        if (id == null || formedTeams == null || formedTeams.isEmpty()) {
            return null;
        }

        String searchId = id.trim();

        for (Team team : formedTeams) {
            if (team == null || team.getMembers().isEmpty()) continue;

            for (Participant p : team.getMembers()) {
                if (p != null && p.getId().equalsIgnoreCase(searchId)) {
                    return p;
                }
            }
        }
        return null;
    }

    // Returns the Team object, not List<Participant>
    public Team findTeamByParticipant(String id, List<Team> formedTeams) {
        if (id == null || id.trim().isEmpty()) return null;
        if (formedTeams == null || formedTeams.isEmpty()) return null;

        String searchId = id.trim();

        for (Team team : formedTeams) {
            if (team == null || team.getMembers().isEmpty()) continue;

            for (Participant p : team.getMembers()) {
                if (p != null && p.getId().equalsIgnoreCase(searchId)) {
                    return team;
                }
            }
        }
        return null;
    }
}
