package services;

import base.Participant;
import base.Team;
import csv.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

/*
 - This class :
    - Provides organizer-side functionality for managing team formation.
        - Loads participant records from CSV.
        - Validates and handles file warnings.
        - Delegates team formation to FormationController.
        - Displays and exports formed teams.
 */

public class OrganizerService {
    private final Scanner scanner;
    private final ParticipantCsvReader csvReader = new ParticipantCsvReader();


    public OrganizerService(Scanner scanner) {
        this.scanner = scanner;
    }

    // Formation methods

    /*
     To orchestrate the team formation flow which:
       - Prompts for participant file path.
       - Loads and validates participant data.
       - Delegates team formation to FormationController.
       - Displays results and returns formed teams.
     */
    public List<Team> manageFormationFlow() {
        System.out.println("\n================================================================================  ");
        System.out.println("                              TEAM FORMATION                                        ");
        System.out.println("================================================================================    ");

        try {

            String filePath = promptForPath();

            //get participant file read and participant objects built
            List<Participant> compiledParticipants = loadParticipants(filePath);


            if (compiledParticipants == null || compiledParticipants.isEmpty()) {
                System.out.println("\nNo valid participants found in the file.");
                System.out.println("Please check the file format and try again.\n");
                return new ArrayList<>();
            }


            // calling team formation and getting the formed teams.
            FormationController formationController = new FormationController(scanner);
            int teamSize = formationController.promptForTeamSize(compiledParticipants.size());

            //Formed team list
            List<Team> formedTeams = formationController.callFormTeams(compiledParticipants, teamSize);

            if (formedTeams == null || formedTeams.isEmpty()) {
                System.out.println("\nTeam formation could not be completed.");
                System.out.println("Please check participant data and try again.\n");
                return new ArrayList<>();
            }

            displayTeamDetailsOnly(formedTeams); // display only, with the exporting options added separately
            System.out.println("If you want to save the formed teams, please select option no.2 in the menu below.");
            return formedTeams;

        } catch (Exception e) {
            System.out.println("\nAn unexpected error occurred while flow is executed.");
            System.out.println("   " + e.getMessage());
            return new ArrayList<>();
        }

    }


    public String promptForPath() {
        while (true) {
            System.out.print("Enter the path to participant records (or press ENTER to use default file):  ");
            String input = scanner.nextLine().trim();

            if (input.isEmpty()) {
                String defaultFile = "participants.csv";
                System.out.println("Using default file: " + defaultFile);
                return defaultFile;
            }

            if (input.length() < 3) {
                System.out.println("File path is too short. Please enter a valid file path.\n");
                continue;
            }

            if (!input.toLowerCase().endsWith(".csv")) {
                System.out.println(" Warning: File doesn't have .csv extension. Continuing anyway.\n");
            }
            return input;
        }

    }

    // To load participants from the given CSV file and stores warnings for issues.
    public List<Participant> loadParticipants(String filePath) {
        if (filePath == null || filePath.trim().isEmpty()) {
            System.out.println("File path cannot be empty.");
            return new ArrayList<>();
        }

        System.out.println("\nLoading participants from: " + filePath + "...");

        try {
            ProcessCsvResult result = csvReader.readFromCsv(filePath);

            if (result == null) {
                System.out.println("Failed to read provided file.Please check the file content and format.");
                return new ArrayList<>();
            }

            List<Participant> participants = result.getValidParticipants();
            Map<Integer, List<String>> warningsByRow = result.getWarningsByRow();

            if (warningsByRow != null && !warningsByRow.isEmpty()) {
                System.out.println("\n⚠️  Errors detected in participant file:");
                System.out.println("====================================================================================================");

                //Getting the warnings saved, on a loop to be printed
                if (warningsByRow.containsKey(-1)) {
                    List<String> fileWarnings = warningsByRow.get(-1);
                    if (fileWarnings != null && !fileWarnings.isEmpty()) {
                        System.out.println("File warnings:");
                        for (String msg : fileWarnings) {
                            System.out.println("  - " + msg);
                        }
                        System.out.println("====================================================================================================");
                    }
                }

                // Print row-specific warnings in inserted order sent from the file reader
                for (Map.Entry<Integer, List<String>> entry : warningsByRow.entrySet()) {
                    int row = entry.getKey();
                    if (row == -1) continue;
                    List<String> msgs = entry.getValue();
                    if (msgs != null && !msgs.isEmpty()) {
                        System.out.println("Row " + row + ": " + String.join("; ", msgs));
                    }
                }

                System.out.println("====================================================================================================");
            }

            if (participants == null || participants.isEmpty()) {
                System.out.println("\nNo valid participants found.");
                return new ArrayList<>();
            }

            System.out.println("\nSuccessfully loaded " + participants.size() + " valid participant(s).");
            return participants;

        } catch (Exception e) {
            System.out.println("\nError loading participants from the provided file.");
            System.out.println("   " + e.getMessage());
            System.out.println("Please check the file path and format.\n");
            return new ArrayList<>();
        }
    }

    public void displayTeamDetailsOnly(List<Team> formedTeams) {
        // reuse the display helper
        displayTeams(formedTeams);
    }









    // Save formed teams methods

    //Save, display formed teams and to export them to CSV.
    public void displayAndExportTeams(List<Team> formedTeams) {
        if (formedTeams == null || formedTeams.isEmpty()) {
            System.out.println("No teams formed yet. Please form teams first.");
            return;
        }

        try {
            displayTeams(formedTeams);

            System.out.print("Would you like to export these teams to a CSV file? (Y/N): ");
            String exportInp = scanner.nextLine().trim().toLowerCase();

            if (exportInp.equals("y") || exportInp.equals("yes")) {
                try {
                    TeamsCsvWriter.writeTeamsToCsv(formedTeams);
                    System.out.println("Teams exported successfully!\n");
                } catch (Exception e) {
                    System.out.println("Failed to write formed teams to CSV:");
                    System.out.println("Teams are formed, but could not write to CSV.\n");
                }
            } else {
                System.out.println("Exporting to CSV skipped.\n");
            }

        } catch (Exception e) {
            System.out.println("Error writing to teams to file:");
            System.out.println("   " + e.getMessage());
        }

    }


    // Reusable displayer to display teams in a formatted output.
    private static void displayTeams(List<Team> formedTeams) {
        if (formedTeams == null || formedTeams.isEmpty()) {
            System.out.println("\nNo teams formed yet. Please form teams first");
            return;
        }

        System.out.println("\n====================================================================================================================");
        System.out.println("                                                FORMED TEAMS                                                         ");
        System.out.println("====================================================================================================================");

        int teamCount = 0;

        for (Team team : formedTeams) {

            if (team == null || team.getMembers().isEmpty()) {
                continue;
            }

            teamCount++;
            System.out.println("\nTEAM " + team.getTeamNumber()
                    + " (" + team.size() + " members):");
            System.out.println("--------------------------------------------------------------------------------------------------------------------");

            for (Participant p : team.getMembers()) {
                if (p == null) continue;

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
                    System.out.println("    [Error displaying participant: " + e.getMessage() + "]");
                }
            }
        }

        System.out.println("\n====================================================================================================================");
        System.out.println("    Total number of teams displayed: " + teamCount);
        System.out.println("====================================================================================================================\n");
    }
}
