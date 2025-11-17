package services;

import base.Participant;
import csv.CsvRowWarning;
import csv.ParticipantCsvReader;
import csv.ProcessCsvResult;

import java.util.ArrayList;
import java.util.List;

public class OrganizerService {
    private final ParticipantCsvReader fileReader = new ParticipantCsvReader();

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

    public List<List<Participant>> callFormTeams(List<Participant> participants, int teamSize) {
        if (participants == null || participants.isEmpty()) {
            System.err.println("❌ Error: No participants provided for team formation.");
            return new ArrayList<>();
        }

        if (teamSize <= 0) {
            System.err.println("❌ Error: Invalid team size (" + teamSize + ").");
            return new ArrayList<>();
        }

        if (participants.size() < teamSize) {
            System.out.println("⚠️  Warning: Not enough participants (" + participants.size()
                    + ") to form a complete team of size " + teamSize + ".");
        }

        System.out.println("\n⚙️  Forming teams...");

        try {
            TeamBuilder.TeamFormationResult result = TeamBuilder.formTeams(participants, teamSize);

            if (result == null) {
                System.err.println("❌ Team formation returned null result.");
                return new ArrayList<>();
            }

            List<List<Participant>> formedTeams = result.getFormedTeams();

            if (formedTeams == null || formedTeams.isEmpty()) {
                System.err.println("❌ Team formation failed. No teams created.");

                if (result.hasPooledParticipants()) {
                    System.out.println("ℹ️  All " + result.getPooledParticipants().size()
                            + " participant(s) were pooled (insufficient for complete teams).");
                }

                return new ArrayList<>();
            }

            System.out.println("✅ Successfully formed " + formedTeams.size() + " team(s).");

            if (result.hasPooledParticipants()) {
                List<Participant> pooled = result.getPooledParticipants();
                System.out.println("\n⚠️  " + pooled.size() + " participant(s) pooled (not assigned to teams):");
                for (Participant p : pooled) {
                    System.out.println("    • " + p.getId() + " - " + p.getFullName());
                }
                System.out.println();
            }

            return formedTeams;

        } catch (Exception e) {
            System.err.println("\n❌ Error during team formation:");
            System.err.println("   " + e.getMessage());
            System.out.println("💡 Please check participant data and try again.\n");
            return new ArrayList<>();
        }
    }
}