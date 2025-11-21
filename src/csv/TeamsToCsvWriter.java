package csv;

import base.Participant;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

public class TeamsToCsvWriter implements CsvWritable<List<Participant>> {
    private static final String FILENAME = "formed_teams.csv";

    @Override
    public void writeToCsv(List<List<Participant>> formedTeams, String filePath) throws IOException {
        if (formedTeams == null || formedTeams.isEmpty()) {
            throw new IllegalArgumentException("No teams to write. Teams list is empty.");
        }

        File file = new File(filePath);

        try (PrintWriter fileWriter = new PrintWriter(new FileWriter(file))) {
            fileWriter.println("TeamNumber,ParticipantID,Name,Email,Interest,SkillLevel,Role,PersonalityScore,PersonalityType");

            int teamsWritten = 0;
            int participantsWritten = 0;

            for (int i = 0; i < formedTeams.size(); i++) {
                List<Participant> formedTeam = formedTeams.get(i);

                if (formedTeam == null || formedTeam.isEmpty()) {
                    continue;
                }

                int teamNumber = i + 1;
                teamsWritten++;

                for (Participant p : formedTeam) {
                    if (p == null) continue;

                    try {
                        fileWriter.printf("%d,%s,%s,%s,%s,%d,%s,%d,%s%n",
                                teamNumber,
                                escapeCsvValue(p.getId()),
                                escapeCsvValue(p.getFullName()),
                                escapeCsvValue(p.getEmail()),
                                p.getInterest(),
                                p.getSkillLevel(),
                                p.getRole(),
                                p.getPersonalityScore(),
                                p.getPersonalityType()
                        );
                        participantsWritten++;
                    } catch (Exception e) {
                        System.err.println("⚠️ Warning: Could not write participant " + p.getId() + ": " + e.getMessage());
                    }
                }
            }

            if (fileWriter.checkError()) {
                throw new IOException("Error occurred while writing to file");
            }

            System.out.println("\n✅ Successfully wrote " + teamsWritten + " team(s) with "
                    + participantsWritten + " participant(s) to: " + filePath);

        } catch (IOException e) {
            throw new IOException("Failed to write teams to CSV file: " + e.getMessage(), e);
        }
    }

    // Convenience method to use default filename
    public static void writeTeamsToCsv(List<List<Participant>> formedTeams) throws IOException {
        new TeamsToCsvWriter().writeToCsv(formedTeams, FILENAME);
    }

    private static String escapeCsvValue(String value) {
        if (value == null) return "";
        if (value.contains(",") || value.contains("\"") || value.contains("\n")) {
            value = value.replace("\"", "\"\"");
            return "\"" + value + "\"";
        }
        return value;
    }

    /*
    public static String getFilename() {
        return FILENAME;
    }

    public static boolean fileExists() {
        return new File(FILENAME).exists();
    }

     */
}