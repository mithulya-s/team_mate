package csv;

import base.Participant;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

public class ParticipantCsvWriter implements CsvWritable<Participant> {
    private static final String FILE_PATH = "participants.csv";
    private static final String HEADER_LINE =
            "ID,Name,Email,PreferredGame,SkillLevel,PreferredRole,PersonalityScore,PersonalityType";

    @Override
    public void writeToCsv(List<Participant> participants, String filePath) throws IOException {
        if (participants == null || participants.isEmpty()) {
            throw new IllegalArgumentException("No participants to write. List is empty.");
        }

        File file = new File(filePath);
        boolean fileExists = file.exists();

        if (fileExists && !file.canWrite()) {
            throw new IOException("File exists but is not writable: " + filePath);
        }

        try (PrintWriter fileWriter = new PrintWriter(new FileWriter(file, true))) {
            // Write header if file is new
            if (!fileExists) {
                fileWriter.println(HEADER_LINE);
            }

            for (Participant participant : participants) {
                if (participant != null) {
                    writeSingleParticipantRow(fileWriter, participant);
                }
            }

            if (fileWriter.checkError()) {
                throw new IOException("Error occurred while writing participants to file.");
            }
        }
    }

    // Convenience method if you want to save a single participant (like your old version)
    public void saveParticipantToCsv(Participant participant) throws IOException {
        writeToCsv(List.of(participant), FILE_PATH);
    }

    // Helper to write one participant row
    private static void writeSingleParticipantRow(PrintWriter fileWriter, Participant participant) {
        fileWriter.printf("%s,%s,%s,%s,%d,%s,%d,%s%n",
                escapeCsvValue(participant.getId()),
                escapeCsvValue(participant.getFullName()),
                escapeCsvValue(participant.getEmail()),
                participant.getInterest(),
                participant.getSkillLevel(),
                participant.getRole(),
                participant.getPersonalityScore(),
                participant.getPersonalityType()
        );
    }

    // Escape values with special characters
    private static String escapeCsvValue(String value) {
        if (value == null) {
            return "";
        }
        if (value.contains(",") || value.contains("\"") || value.contains("\n")) {
            value = value.replace("\"", "\"\"");
            return "\"" + value + "\"";
        }
        return value;
    }

    /*
    // Convenience method to check if file exists
    public static boolean fileExists() {
        return new File(FILE_PATH).exists();
    }

     */
}