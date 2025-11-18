package csv;

import base.Participant;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class ParticipantCsvReader {
    private final RowHandler rowHandler = new RowHandler();

    //thread pool configuration
    private static final int THREAD_POOL_SIZE=4;

    public ProcessCsvResult readFile(String path) {
        List<Participant> validParticipants = new ArrayList<>();
        List<CsvRowWarning> warnings = new ArrayList<>();

        if (path == null || path.trim().isEmpty()) {
            warnings.add(new CsvRowWarning(-1, List.of("File path cannot be empty.")));
            return new ProcessCsvResult(validParticipants, warnings);
        }


        //think about this a bit

        if (!Files.exists(Paths.get(path))) {
            warnings.add(new CsvRowWarning(-1, List.of("File not found: " + path)));
            return new ProcessCsvResult(validParticipants, warnings);
        }

        //read all the lines to put to threads
        List<String> allRows = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            String row;
            int rowNumber = 1;
            boolean isHeaderLine = true;

            while ((row = br.readLine()) != null) {
                if (isHeaderLine) {
                    isHeaderLine = false;
                    //rowNumber++;
                    continue;
                }

                if (!row.trim().isEmpty()) {
                    allRows.add(row);
                }
            }
        } catch (FileNotFoundException e) {
            warnings.add(new CsvRowWarning(-1, List.of("File not found: " + path)));
            return new ProcessCsvResult(validParticipants, warnings);
        } catch (IOException e) {
            warnings.add(new CsvRowWarning(-1, List.of("Error reading file: " + e.getMessage())));
            return new ProcessCsvResult(validParticipants, warnings);
        }

        // If no data lines, return early
        if (allRows.isEmpty()) {
            return new ProcessCsvResult(validParticipants, warnings);
        }

        // Now process lines in parallel using threads
        System.out.println("🧵 Processing " + allRows.size() + " rows using " + THREAD_POOL_SIZE + " threads...");


        //create the thread pool
        ExecutorService executor = Executors.newFixedThreadPool(THREAD_POOL_SIZE);
        List<Future<RowProcessingResult>> futures = new ArrayList<>();

        //submit each thread to be prcessed
        for (int i = 0; i < allRows.size(); i++) {
            final int rowNumber = i + 2;
            final String row = allRows.get(i);

            //sumit the task to thread pool
            Future<RowProcessingResult> future = executor.submit(() -> {
                return processRowInThread(row, rowNumber);
            });

            futures.add(future);
        }

        for (Future<RowProcessingResult> future : futures) {
            try {
                RowProcessingResult result = future.get();

                if (result.participant != null) {
                    synchronized (validParticipants) {
                        validParticipants.add(result.participant);
                    }
                }

                if (result.warnings != null) {
                    synchronized (warnings) {
                        warnings.add(result.warnings);
                    }
                }
            } catch (InterruptedException e) {
                warnings.add(new CsvRowWarning(-1,
                        List.of("Thread interrupted: " + e.getMessage())));
                Thread.currentThread().interrupt(); // Restore interrupt status
            } catch (ExecutionException e) {
                warnings.add(new CsvRowWarning(-1,
                        List.of("Error in thread execution: " + e.getMessage())));
            }
        }

        //shutdown the htread
        executor.shutdown();
        try {
            // Wait up to 30 seconds for all threads to complete
            if (!executor.awaitTermination(30, TimeUnit.SECONDS)) {
                executor.shutdownNow(); // Force shutdown if timeout
                warnings.add(new CsvRowWarning(-1,
                        List.of("Warning: Some threads did not complete in time")));
            }
        } catch (InterruptedException e) {
            executor.shutdownNow();
            Thread.currentThread().interrupt();
        }

        System.out.println("✅ Parallel processing complete!");

        return new ProcessCsvResult(validParticipants, warnings);
    }


    //helpers
    private RowProcessingResult processRowInThread(String line, int rowNumber) {
        try {
            String[] cols = line.split(",", -1);

            // Process the row using RowHandler
            RowHandler.Result result = rowHandler.readRow(cols);

            if (result.isValidLine()) {
                return new RowProcessingResult(result.getParticipant(), null);
            } else {
                return new RowProcessingResult(null,
                        new CsvRowWarning(rowNumber, result.getWarnings()));
            }

        } catch (Exception e) {
            return new RowProcessingResult(null,
                    new CsvRowWarning(rowNumber,
                            List.of("Unexpected error processing row: " + e.getMessage())));
        }
    }

    //class to hold the threads
    private static class RowProcessingResult {
        final Participant participant;
        final CsvRowWarning warnings;

        RowProcessingResult(Participant participant, CsvRowWarning warnings) {
            this.participant = participant;
            this.warnings = warnings;
        }
    }
}