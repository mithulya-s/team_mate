package csv;

import base.Participant;
import base.Team;
import utilities.Interest;
import utilities.Role;
import utilities.PersonalityType;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class TeamCsvReader implements CsvReadable<List<Team>> {
    private static final String DEFAULT_FILE_PATH = "formed_teams.csv";

    @Override
    public List<Team> readFromCsv(String filePath) {
        String pathToUse = (filePath == null || filePath.trim().isEmpty()) ? DEFAULT_FILE_PATH : filePath;
        Map<Integer, Team> teamMap = new LinkedHashMap<>();

        try (BufferedReader br = new BufferedReader(new FileReader(pathToUse))) {
            String line;
            boolean isHeader = true;

            while ((line = br.readLine()) != null) {
                if (isHeader) {
                    isHeader = false; // skip header
                    continue;
                }
                if (line.trim().isEmpty()) continue;

                // NOTE: this uses simple comma split like your original reader.
                // If you expect quoted fields with commas, replace with a proper CSV parser.
                String[] cols = line.split(",", -1);
                if (cols.length < 9) continue; // skip malformed rows

                try {
                    int teamNumber = Integer.parseInt(cols[0].trim());
                    String id = cols[1].trim();
                    String name = cols[2].trim();
                    String email = cols[3].trim();
                    Interest interest = Interest.valueOf(cols[4].trim());
                    int skillLevel = Integer.parseInt(cols[5].trim());
                    Role role = Role.valueOf(cols[6].trim());
                    int personalityScore = Integer.parseInt(cols[7].trim());
                    PersonalityType personalityType = PersonalityType.valueOf(cols[8].trim());

                    Participant participant = new Participant(
                            id, name, email, interest, skillLevel, role, personalityScore, personalityType
                    );

                    Team team = teamMap.computeIfAbsent(teamNumber, k -> new Team(teamNumber));
                    team.addMember(participant);

                } catch (IllegalArgumentException | IndexOutOfBoundsException ex) {
                    // Skip malformed row but log a warning for debugging
                    System.err.println("⚠️ Skipping malformed row: \"" + line + "\" — " + ex.getMessage());
                }
            }
        } catch (IOException e) {
            System.err.println("❌ Error reading formed teams CSV: " + e.getMessage());
        }

        return new ArrayList<>(teamMap.values());
    }

    // convenience method for default path
    public List<Team> readDefaultFile() {
        return readFromCsv(DEFAULT_FILE_PATH);
    }
}
