package csv;

import base.Participant;
import utilities.Interest;
import utilities.PersonalityType;
import utilities.Role;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.*;


public class ParticipantCsvReader implements CsvReadable<ProcessCsvResult> {
    // thread pool configuration
    private static final int THREAD_POOL_SIZE = 4;

    @Override
    public ProcessCsvResult readFromCsv(String path) {
        List<Participant> validParticipants = Collections.synchronizedList(new ArrayList<>());
        Map<Integer, List<String>> warningsByRow = Collections.synchronizedMap(new LinkedHashMap<>());

        // basic path checks -> add file-level warnings using row key -1
        if (path == null || path.trim().isEmpty()) {
            warningsByRow.put(-1, List.of("File path cannot be empty."));
            return new ProcessCsvResult(validParticipants, warningsByRow);
        }

        if (!Files.exists(Paths.get(path))) {
            warningsByRow.put(-1, List.of("File not found: " + path));
            return new ProcessCsvResult(validParticipants, warningsByRow);
        }

        // read lines (skip header)
        List<String> allRows = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            String row;
            boolean isHeaderLine = true;
            while ((row = br.readLine()) != null) {
                if (isHeaderLine) {
                    isHeaderLine = false;
                    continue;
                }
                if (!row.trim().isEmpty()) {
                    allRows.add(row);
                }
            }
        } catch (IOException e) {
            warningsByRow.put(-1, List.of("Error reading file: " + e.getMessage()));
            return new ProcessCsvResult(validParticipants, warningsByRow);
        }

        if (allRows.isEmpty()) {
            // no rows but no error - return empty result
            return new ProcessCsvResult(validParticipants, warningsByRow);
        }

        System.out.println("🧵 Processing " + allRows.size() + " rows using " + THREAD_POOL_SIZE + " threads...");

        ExecutorService executor = Executors.newFixedThreadPool(THREAD_POOL_SIZE);
        List<Future<RowProcessingResult>> futures = new ArrayList<>();

        for (int i = 0; i < allRows.size(); i++) {
            final int rowNumber = i + 2; // account for header row
            final String row = allRows.get(i);

            Future<RowProcessingResult> future = executor.submit(() -> processRowInThread(row, rowNumber));
            futures.add(future);
        }

        for (Future<RowProcessingResult> future : futures) {
            try {
                RowProcessingResult result = future.get();

                if (result.participant != null) {
                    validParticipants.add(result.participant);
                }

                if (result.warnings != null && !result.warnings.isEmpty()) {
                    // keep the warnings keyed by row number
                    warningsByRow.put(result.rowNumber, new ArrayList<>(result.warnings));
                }

            } catch (InterruptedException e) {
                warningsByRow.put(-1, List.of("Thread interrupted: " + e.getMessage()));
                Thread.currentThread().interrupt();
            } catch (ExecutionException e) {
                warningsByRow.put(-1, List.of("Error in thread execution: " + e.getMessage()));
            }
        }

        // shutdown
        executor.shutdown();
        try {
            if (!executor.awaitTermination(40, TimeUnit.SECONDS)) {
                executor.shutdownNow();
                warningsByRow.put(-1, List.of("Warning: Some threads did not complete in time"));
            }
        } catch (InterruptedException e) {
            executor.shutdownNow();
            Thread.currentThread().interrupt();
        }

        System.out.println("✅ Parallel processing complete!");
        return new ProcessCsvResult(validParticipants, warningsByRow);
    }









    // helper used by threads
    private RowProcessingResult processRowInThread(String line, int rowNumber) {
        try {
            String[] cols = line.split(",", -1);
            List<String> warnings = new ArrayList<>();

            Participant participant = parseRow(cols, warnings);

            if (participant != null) {
                validateLine(participant, warnings);
            }

            if (participant != null && warnings.isEmpty()) {
                return new RowProcessingResult(participant, rowNumber, null);
            } else {
                if (warnings.isEmpty()) {
                    warnings.add("Failed to parse participant from row");
                }
                return new RowProcessingResult(null, rowNumber, warnings);
            }
        } catch (Exception e) {
            return new RowProcessingResult(null, rowNumber,
                    List.of("Unexpected error processing row: " + e.getMessage()));
        }
    }

    // small helper to carry thread results
    private static class RowProcessingResult {
        final Participant participant;
        final int rowNumber;
        final List<String> warnings;

        RowProcessingResult(Participant participant, int rowNumber, List<String> warnings) {
            this.participant = participant;
            this.rowNumber = rowNumber;
            this.warnings = warnings;
        }
    }

    // --------------------- parsing & validation helpers ---------------------

    private Participant parseRow(String[] line, List<String> warnings) {
        if (line == null || line.length != 8) {
            warnings.add("Expected 8 columns, found " + (line == null ? 0 : line.length));
            return null;
        }

        try {
            String id = line[0].trim();
            String fullName = line[1].trim();
            String email = line[2].trim();

            Interest interest = parseInterestCol(line[3].trim(), warnings);
            int skillLevel = parseLevelCol(line[4].trim(), warnings);
            Role role = parseRoleCol(line[5].trim(), warnings);
            int personScore = parsePersonalityScoreCol(line[6].trim(), warnings);
            PersonalityType perType = parsePersonalityType(line[7].trim(), warnings);

            if (interest == null || role == null || perType == null ||
                    skillLevel == -1 || personScore == -1) {
                return null;
            }

            try {
                return new Participant(id, fullName, email, interest, skillLevel, role, personScore, perType);
            } catch (IllegalArgumentException e) {
                warnings.add("Participant validation failed: " + e.getMessage());
                return null;
            }

        } catch (Exception e) {
            warnings.add("Error parsing row: " + e.getMessage());
            return null;
        }
    }

    private void validateLine(Participant part, List<String> warnings) {
        if (part.getId() == null || part.getId().isEmpty()) {
            warnings.add("Missing ID");
        }

        if (part.getFullName() == null || part.getFullName().isEmpty()) {
            warnings.add("Missing full name");
        }

        if (part.getEmail() == null || !part.getEmail().contains("@") || !part.getEmail().contains(".")) {
            warnings.add("Invalid email format");
        }

        if (part.getInterest() == null) {
            warnings.add("Invalid interest");
        }

        if (part.getRole() == null) {
            warnings.add("Invalid role");
        }

        if (part.getPersonalityType() == null) {
            warnings.add("Invalid personality type");
        }

        if (part.getSkillLevel() < 1 || part.getSkillLevel() > 10) {
            warnings.add("Skill level out of range (1-10): " + part.getSkillLevel());
        }

        if (part.getPersonalityScore() < 0 || part.getPersonalityScore() > 100) {
            warnings.add("Personality score out of range (0-100): " + part.getPersonalityScore());
        }
    }

    private Interest parseInterestCol(String col, List<String> warnings) {
        if (col == null || col.isEmpty()) {
            warnings.add("Interest column is empty");
            return null;
        }

        try {
            String normalized = col.toUpperCase()
                    .replace(":", "")
                    .replace(" ", "")
                    .trim();
            return Interest.valueOf(normalized);
        } catch (IllegalArgumentException e) {
            warnings.add("Invalid interest: '" + col + "'");
            return null;
        }
    }

    private Role parseRoleCol(String col, List<String> warnings) {
        if (col == null || col.isEmpty()) {
            warnings.add("Role column is empty");
            return null;
        }

        try {
            return Role.valueOf(col.toUpperCase().trim());
        } catch (IllegalArgumentException e) {
            warnings.add("Invalid role: '" + col + "'");
            return null;
        }
    }

    private PersonalityType parsePersonalityType(String col, List<String> warnings) {
        if (col == null || col.isEmpty()) {
            warnings.add("Personality type column is empty");
            return null;
        }

        try {
            return PersonalityType.valueOf(col.toUpperCase().trim());
        } catch (IllegalArgumentException e) {
            warnings.add("Invalid personality type: '" + col + "'");
            return null;
        }
    }

    private int parseLevelCol(String col, List<String> warnings) {
        if (col == null || col.isEmpty()) {
            warnings.add("Skill level column is empty");
            return -1;
        }

        try {
            return Integer.parseInt(col.trim());
        } catch (NumberFormatException e) {
            warnings.add("Invalid skill level: '" + col + "'");
            return -1;
        }
    }

    private int parsePersonalityScoreCol(String col, List<String> warnings) {
        if (col == null || col.isEmpty()) {
            warnings.add("Personality score column is empty");
            return -1;
        }

        try {
            return Integer.parseInt(col.trim());
        } catch (NumberFormatException e) {
            warnings.add("Invalid personality score: '" + col + "'");
            return -1;
        }
    }
}
