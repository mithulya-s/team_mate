/*
package older.cli;

import base.Participant;
import csv.TeamCsvReader;
import services.ParticipantLookup;

import java.util.List;
import java.util.Scanner;

public class ParticipantLookupCli {
    private final Scanner scanner;
    private final ParticipantLookup lookupFunctionality;


    public ParticipantLookupCli(Scanner scanner) {
        this.scanner = scanner;
        this.lookupFunctionality = new ParticipantLookup();
    }


    public void manageLookupFlow(List<List<Participant>> formedTeams) {
        System.out.println("Welcome to participant search.");

        if (formedTeams == null || formedTeams.isEmpty()) {
            try {
                formedTeams = new TeamCsvReader().readDefaultFile();
                if (formedTeams.isEmpty()) {
                    System.out.println("Sorry. Teams have not been formed yet.");
                    return;
                }
            } catch (Exception e) {
                System.out.println("No formed teams file found. Please check back later.");
                return;
            }
        }


        System.out.println("Please enter your participant ID: ");
        String participantId = scanner.nextLine().trim();

        //error handling
        if (participantId.isEmpty()){
            System.out.println("ID cannot be empty. Please enter your participant ID: ");
            return;
        }

        //if in wrong format
        if (participantId.length()<4){
            System.out.println("Invalid ID format. ID should be at least 4 characters (Eg: P001).\n ");
            return;
        }

        //searching functionality
        try {
            Participant participant = lookupFunctionality.findParticipantInTeams(participantId, formedTeams);
            List<Participant> assignedTeam = lookupFunctionality.findTeamByParticipant(participantId, formedTeams);

            if (participant == null || assignedTeam == null) {
                System.out.println("\n❌ No team assignment found for ID: " + participantId);
                System.out.println("\nPossible reasons:");
                System.out.println("  • ID might be incorrect (check for typos)");
                System.out.println("  • You may not have filled the survey yet");
                System.out.println("  • Teams may not have been formed with your data");
                System.out.println("\n💡 Please verify your ID or contact the organizer.\n");
            } else {
                int teamNumber = lookupFunctionality.getTeamNum(assignedTeam, formedTeams);

                System.out.println("\n✅ Team Assignment Found!");
                System.out.println("=======================================================");
                System.out.println("📋 You are assigned to Team " + teamNumber);
                System.out.println("=======================================================");
                System.out.println("\n👥 Your Teammates:");

                //printing the mates
                for (Participant p : assignedTeam) {
                    if (p == null) {
                        continue;
                    }

                    if (p.getId().equalsIgnoreCase(participantId)) {
                        System.out.printf("  👉 %s (YOU)\n", p.getFullName());
                        System.out.printf("      Role: %s | Interest: %s | Skill: %d\n",
                                p.getRole(), p.getInterest(), p.getSkillLevel());
                    } else {
                        System.out.printf("  • %s\n", p.getFullName());
                        System.out.printf("      Role: %s | Interest: %s | Skill: %d\n",
                                p.getRole(), p.getInterest(), p.getSkillLevel());
                    }

                    System.out.println("\n===============================================");
                }
            }
        }
        catch (Exception e) {
        System.err.println("An error occurred during lookup: " + e.getMessage());
        }
    }


}

 */
