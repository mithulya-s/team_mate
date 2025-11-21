package csv;

import base.Participant;
import utilities.Interest;
import utilities.Role;
import utilities.PersonalityType;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class FormedTeamsCsvReader implements  CsvReadable<List<List<Participant>>> {
    private static final String FILE_PATH = "formed_teams.csv";

    @Override
    public List<List<Participant>> readFromCsv(String filePath) {
        Map<Integer, List<Participant>> teamMap = new LinkedHashMap<>();

        try (BufferedReader br = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;
            boolean isHeader = true;

            while ((line = br.readLine()) != null) {
                if (isHeader) {
                    isHeader = false; // skip header
                    continue;
                }
                if (line.trim().isEmpty()) continue;

                String[] cols = line.split(",");
                if (cols.length < 9) continue; // skip malformed rows

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

                teamMap.computeIfAbsent(teamNumber, k -> new ArrayList<>()).add(participant);
            }
        } catch (IOException e) {
            System.err.println("❌ Error reading formed teams CSV: " + e.getMessage());
        }
        return new ArrayList<>(teamMap.values());
    }

    //to use the def path
    public List<List<Participant>> readDefaultFile() {
        return readFromCsv(FILE_PATH);
    }

}