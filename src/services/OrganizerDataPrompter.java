package services;

import base.Participant;
import csv.CsvRowWarning;
import csv.ParticipantCsvReader;
import csv.ProcessCsvResult;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class OrganizerDataPrompter {
    private final Scanner scanner;
    private final ParticipantCsvReader fileReader = new ParticipantCsvReader();

    public OrganizerDataPrompter(Scanner scanner) {
        this.scanner = scanner;
    }

    //helper for path
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


    public List<Participant> loadParticipants(String filePath) {
        if (filePath == null || filePath.trim().isEmpty()) {
            System.err.println("❌ Error: File path cannot be empty.");
            return new ArrayList<>();
        }

        System.out.println("\n📂 Loading participants from: " + filePath + "...");

        try {
            ProcessCsvResult result = fileReader.readFile(filePath);

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


}