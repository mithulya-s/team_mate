package utilities;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

/*
 - To generate unique participant IDs in sequential order.
 - Implemented as a Singleton to ensure consistent ID generation across the system.
 - IDs are prefixed with 'P' and padded to three digits
 */

public class IdGenerator {
    // Singleton instance
    private static IdGenerator instance;

    private static final String FILE_PATH = "participants.csv";
    private static final String PREFIX = "P";
    private int lastGeneratedId;

    // Private constructor initializes last ID from file,made private to stop external instantiation and protect singleton state
    private IdGenerator() {
        this.lastGeneratedId = findMaxIdFromFile();
    }


    public static synchronized IdGenerator getInstance() {
        if (instance == null) {
            instance = new IdGenerator();
        }
        return instance;
    }

    public synchronized String generateNextId() {
        lastGeneratedId++;
        return String.format("%s%03d", PREFIX, lastGeneratedId);
    }



    private int findMaxIdFromFile() {
        File file = new File(FILE_PATH);

        if (!file.exists()) {
            return 0;
        }

        int maxId = 0;

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            boolean isHeaderLine = true;

            while ((line = br.readLine()) != null) {
                // Skip header line
                if (isHeaderLine) {
                    isHeaderLine = false;
                    continue;
                }

                if (line.trim().isEmpty()) {
                    continue;
                }

                // Extract ID from first column
                String[] cols = line.split(",");
                if (cols.length > 0) {
                    String id = cols[0].trim();
                    int idNum = extractIdNumber(id);
                    if (idNum > maxId) {
                        maxId = idNum;
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
            System.out.println("Warning: Could not read participant file for ID generation. Starting from P001.");
            return 0;
        }

        return maxId;
    }


    //To extract the numeric portion of the ID
    private int extractIdNumber(String id) {
        if (id == null || id.trim().isEmpty()) {
            return 0;
        }

        if (id.startsWith(PREFIX)) {
            String numPart = id.substring(PREFIX.length());

            try {
                return Integer.parseInt(numPart);
            } catch (NumberFormatException e) {
                System.out.println("Invalid ID format detected: " + id);
                return 0;
            }
        }
        return 0;
    }
}