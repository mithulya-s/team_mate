package services;

import base.Participant;
import csv.*;


import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;



public class OrganizerService {
    private final Scanner scanner;
    private final ParticipantCsvReader csvReader= new ParticipantCsvReader();

    //Constructor
    public OrganizerService(Scanner scanner) {
        this.scanner = scanner;
    }



    //Formation methods

    //Form and return teams
    public List<List<Participant>> manageFormationFlow() {
        System.out.println("Welcome to Organizer CLI");
        System.out.println("===========================================================");

        try {
            //OrganizerDataPrompter organizerDataPrompter = new OrganizerDataPrompter(scanner);

            //calling helper to file path
            String filePath = promptForPath();

            List<Participant> compiledParticipants = loadParticipants(filePath);

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

    // Get file path
    public String promptForPath() {
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

    //Process file and load participant objects
    public List<Participant> loadParticipants(String filePath) {
        if (filePath == null || filePath.trim().isEmpty()) {
            System.err.println("❌ Error: File path cannot be empty.");
            return new ArrayList<>();
        }

        System.out.println("\n📂 Loading participants from: " + filePath + "...");

        try {
            ProcessCsvResult result = csvReader.readFromCsv(filePath);

            if (result == null) {
                System.err.println("❌ Error: Failed to read file.");
                return new ArrayList<>();
            }

            List<Participant> participants = result.getValidParticipants();
            List<CsvRowWarning> warnings = result.getWarnings();

            if (warnings != null && !warnings.isEmpty()) {
                System.out.println("\n⚠️  Errors detected in participant file:");
                System.out.println("═══════════════════════════════════════════════════════");
                for (CsvRowWarning warning : warnings) {
                    if (warning.getMessages() != null && !warning.getMessages().isEmpty()) {
                        System.out.println("Row " + warning.getRowNumber() + ": "
                                + String.join("; ", warning.getMessages()));
                    }
                }
                System.out.println("═══════════════════════════════════════════════════════");
            }

            if (participants == null || participants.isEmpty()) {
                System.out.println("\n❌ No valid participants found.");
                return new ArrayList<>();
            }

            System.out.println("✅ Successfully loaded " + participants.size() + " valid participant(s).\n");
            return participants;

        } catch (Exception e) {
            System.err.println("\n❌ Error loading participants:");
            System.err.println("   " + e.getMessage());
            System.out.println("💡 Please check the file path and format.\n");
            return new ArrayList<>();
        }
    }

    //Display only
    public void displayTeamDetailsOnly(List<List<Participant>> formedTeams){
       //reuse my helper
        displayTeams(formedTeams);
    }








    // Save formed teams methods
    public void displayAndExportTeams(List<List<Participant>> formedTeams) {
        if (formedTeams == null || formedTeams.isEmpty()) {
            System.out.println("No teams formed yet. Please form teams first.");
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




    //General helper to formatted display
    private static void displayTeams(List<List<Participant>> formedTeams) {
        if (formedTeams == null || formedTeams.isEmpty()) {
            System.out.println("\n⚠️ No teams formed yet. Please form teams first");
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
