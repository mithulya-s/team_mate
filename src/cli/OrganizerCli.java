package cli;

import base.Participant;
import csv.TeamsToCsvWriter;
import services.FormationController;
import services.OrganizerDataPrompter;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;



public class OrganizerCli {
    private final Scanner scanner;

    public OrganizerCli(Scanner scanner) {
        this.scanner = scanner;
    }


    public List<List<Participant>> manageOrganizerFlow() {
        System.out.println("Welcome to Organizer CLI");
        System.out.println("===========================================================");

        try {
            OrganizerDataPrompter organizerDataPrompter = new OrganizerDataPrompter(scanner);

            //calling helper to file path
            String filePath = organizerDataPrompter.promptForPath();

            List<Participant> compiledParticipants = organizerDataPrompter.loadParticipants(filePath);

            if (compiledParticipants == null || compiledParticipants.isEmpty()) {
                System.out.println("\n❌ No valid participants found in the file.");
                System.out.println("💡 Please check the file format and try again.\n");
                return new ArrayList<>();
            }



            //calling the team formation and getting the formed teams.
            FormationController formationController = new FormationController(scanner);

            int teamSize=formationController.promptForTeamSize(compiledParticipants.size());

            List<List<Participant>> formedTeams= formationController.callFormTeams(compiledParticipants,teamSize);

            if (formedTeams == null || formedTeams.isEmpty()) {
                System.out.println("\n❌ Team formation could not be completed.");
                System.out.println("💡 Please check participant data and try again.\n");
                return new ArrayList<>();
            }

            displayTeamDetailsOnly(formedTeams); //for the last steps, no instance because in the same class
            System.out.println("If you want to save the formed teams, please select option no.2 in the menu below.");
            return formedTeams;


        } catch (Exception e){
            System.err.println("\n❌ An unexpected error occurred in organizer flow:");
            System.err.println("   " + e.getMessage());
            System.out.println("💡 Please try again or contact support.\n");
            return new ArrayList<>();
        }

    }



    // first methods to be called.












    //Method to only display, which gets called by the formation
    public void displayTeamDetailsOnly(List<List<Participant>> formedTeams){
       //reuse my helper
        displayTeams(formedTeams);
    }

    //separate function to calling the team dispaly and exporting orchestration
    public void displayAndExportTeams(List<List<Participant>> formedTeams) {
        if (formedTeams == null || formedTeams.isEmpty()) {
            System.out.println(" No teams to display or export.\n");
            return;
        }

        try{
            displayTeams(formedTeams);

            //Asking whether he wants it imported.
            System.out.print("Would you like to export these teams to a CSV file? (Y/N): ");
            String exportInp= scanner.nextLine().trim().toLowerCase();

            if (exportInp.equals("y") || exportInp.equals("yes")) {
                try {
                    TeamsToCsvWriter.writeTeamsToCsv(formedTeams);
                    System.out.println("Teams exported successfully!\n");
                } catch (Exception e) {
                    System.err.println("Failed to write formed teams to CSV:");
                    System.err.println("   " + e.getMessage());
                    System.out.println("💡 Teams are formed, but could not write to CSV. failed.\n");
                }
            } else {
                System.out.println("Exporting to CSV skipped.\n");
            }

        } catch (Exception e) {
            System.err.println(" Error writing to teams to file:");
            System.err.println("   " + e.getMessage());
        }

    }


    //helper which displays the formed teams, cleanyl formatted
    private static void displayTeams(List<List<Participant>> formedTeams) {
        if (formedTeams == null || formedTeams.isEmpty()) {
            System.out.println("\n⚠️  No teams to display.");
            return;
        }

        System.out.println("\n📊 Formed Teams:");
        System.out.println("═══════════════════════════════════════════════════════");

        int teamCount = 0;
        for (int i = 0; i < formedTeams.size(); i++) {
            List<Participant> team = formedTeams.get(i);

            if (team == null || team.isEmpty()) {
                continue;
            }

            teamCount++;
            System.out.println("\n🏆 Team " + (i + 1) + " (" + team.size() + " members):");
            System.out.println("───────────────────────────────────────────────────────");

            for (Participant p : team) {
                if (p == null) {
                    continue;
                }

                try {
                    System.out.printf("  • %s | %s | %s | %s (Skill: %d) | %s | Score: %d (%s)%n",
                            p.getId(),
                            p.getFullName(),
                            p.getEmail(),
                            p.getInterest(),
                            p.getSkillLevel(),
                            p.getRole(),
                            p.getPersonalityScore(),
                            p.getPersonalityType()
                    );
                } catch (Exception e) {
                    System.out.println("  • [Error displaying participant: " + e.getMessage() + "]");
                }
            }
        }

        System.out.println("\n═══════════════════════════════════════════════════════");
        System.out.println("Total teams displayed: " + teamCount);
        System.out.println("═══════════════════════════════════════════════════════\n");
    }

}
