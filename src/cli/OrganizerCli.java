package cli;

import base.Participant;
import csv.TeamsToCsvWriter;
import services.OrganizerService;
import utilities.TeamDisplayer;

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

            //calling helper to file path
            String filePath = promptForPath();

            OrganizerService organizerService = new OrganizerService();
            List<Participant> compiledParticipants = organizerService.loadParticipants(filePath);

            if (compiledParticipants == null || compiledParticipants.isEmpty()) {
                System.out.println("\n❌ No valid participants found in the file.");
                System.out.println("💡 Please check the file format and try again.\n");
                return new ArrayList<>();
            }

            int teamSize=promptForTeamSize(compiledParticipants.size());

            //calling the team formation and getting the formed teams.
            List<List<Participant>> formedTeams= organizerService.callFormTeams(compiledParticipants,teamSize);

            if (formedTeams == null || formedTeams.isEmpty()) {
                System.out.println("\n❌ Team formation could not be completed.");
                System.out.println("💡 Please check participant data and try again.\n");
                return new ArrayList<>();
            }

            displayAndExportTeams(formedTeams); //for the last steps, no instance because in the same class

            return formedTeams;

        } catch (Exception e){
            System.err.println("\n❌ An unexpected error occurred in organizer flow:");
            System.err.println("   " + e.getMessage());
            System.out.println("💡 Please try again or contact support.\n");
            return new ArrayList<>();
        }

    }

    //helper for path
    private String promptForPath() {
        while (true) {
            System.out.print("Enter the path to participant records (or press Enter to use default file): ");
            String input = scanner.nextLine().trim();

            if (input.isEmpty()) {
                String defaultFile = "participants.csv";
                System.out.println("✅ Using default file: " + defaultFile);
                return defaultFile;
            }

            if (input.length() < 3) {
                System.out.println("File path is too short. Please enter a valid file path.\n");
                continue;
            }

            if (!input.toLowerCase().endsWith(".csv")) {
                System.out.println(" Warning: File doesn't have .csv extension. Continuing anyway...");
            }

            return input;
        }

}

    //helper for size
    private int promptForTeamSize(int totalParticipants) {
        System.out.println("\n Total valid participants loaded: "+ totalParticipants);


        while (true) {
            System.out.println("Enter the desired team size: ");

            try {
                String userInp= scanner.nextLine().trim();

                if (userInp.isEmpty()) {
                    System.out.println("Team size cannot be empty. Please enter a valid team size.");
                    continue;
                }

                int teamSize = Integer.parseInt(userInp);

                if (teamSize <= 0) {
                    System.out.println("Team size must be greater than zero. Try again.\n");
                    continue;
                }

                if (teamSize > totalParticipants) {
                    System.out.println("Team size (" + teamSize + ") is larger than total participants (" + totalParticipants + ").");
                    System.out.println("💡 Maximum team size is " + totalParticipants + ". Try again.\n");
                    continue;
                }

                if (teamSize == 1) {
                    System.out.print("⚠️ Team size of 1 means no teaming. Continue? (Y/N): ");
                    String confirm = scanner.nextLine().trim().toLowerCase();
                    if (!confirm.equals("y")) {
                        continue;
                    }
                }

                //think about this a bot
                /*
                if (teamSize > 10) {
                    System.out.print("⚠️ Large team size (" + teamSize + "). Are you sure? (Y/N): ");
                    String confirm = scanner.nextLine().trim().toLowerCase();
                    if (!confirm.equals("y")) {
                        continue;
                    }
                }

                 */


                int expectedTeams = (int) Math.ceil((double) totalParticipants / teamSize);
                System.out.println("This will create approximately " + expectedTeams + " teams.\n");

                return teamSize;

            } catch (NumberFormatException e) {
                System.out.println(" Invalid input. Please enter a valid number.\n");
            }

        }
    }


    //separate function to calling the team dispaly and exporting orchestration
    public void displayAndExportTeams(List<List<Participant>> formedTeams) {
        if (formedTeams == null || formedTeams.isEmpty()) {
            System.out.println(" No teams to display or export.\n");
            return;
        }

        try{
            TeamDisplayer.displayTeams(formedTeams);

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
                System.out.println(" Exporting to CSV skipped.\n");
            }

        } catch (Exception e) {
            System.err.println(" Error writing to teams to file:");
            System.err.println("   " + e.getMessage());
        }

    }

}
