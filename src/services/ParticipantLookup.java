package services;

import base.Participant;
import base.Team;
import csv.TeamCsvReader;

import java.util.List;
import java.util.Scanner;


//Provides lookup functionality for participants to find their assigned team.
public class ParticipantLookup {

    private final Scanner scanner;

    public ParticipantLookup(Scanner scanner) {
        this.scanner = scanner;
    }

    /*
     Functionality to:
        - Orchestrate the participant lookup flow:
             - Load team data from CSV.
             - Prompt for participant ID and validate format.
             - Find participant and their team, then display assignment details.
             - Handle missing files or invalid IDs gracefully.
     */

    public void manageLookupFlow(List<Team> formedTeams) {
        System.out.println("\n======================================================================");
        System.out.println("                            PARTICIPANT SEARCH                           ");
        System.out.println("======================================================================  ");

        try {
            //Read the formed team file and get the teams
            formedTeams = new TeamCsvReader().readDefaultFile();
            if (formedTeams == null || formedTeams.isEmpty()) {
                System.out.println("Sorry,teams have not been formed yet.Please check back later.\n");
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

                System.out.println("\nTeam Assignment Found");
                System.out.println("===============================================================================");
                System.out.println("You are assigned to TEAM NO: " + assignedTeam.getTeamNumber());
                System.out.println("Teammates:");
                System.out.println("===============================================================================");

                for (Participant p : assignedTeam.getMembers()) {
                    if (p == null) continue;

                    // Clean display with the participant who entered the ID, emphasized.
                    if (p.getId().equalsIgnoreCase(participantId)) {
                        System.out.printf(" • %s (You)\n", p.getFullName());
                    } else {
                        System.out.printf("  • %s\n", p.getFullName());
                    }

                    System.out.printf(
                            "      Role: %s | Interest: %s | Skill: %d\n\n",
                            p.getRole(), p.getInterest(), p.getSkillLevel()
                    );
                }

                System.out.println("============================================================================\n");
            }
            else {
                System.out.println("\n No team assignment found for provided ID: " + participantId);
            }

        } catch (Exception e) {
            System.out.println("An error occurred during lookup." + e.getMessage());
        }
    }

    //Searches all teams to locate participant
    private Participant findParticipantInTeams(String id, List<Team> formedTeams) {
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

    // Find the team that includes the given participant ID
    private Team findTeamByParticipant(String id, List<Team> formedTeams) {
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
