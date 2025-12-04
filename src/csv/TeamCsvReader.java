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

/*
 - This class Reads formed team data from a CSV file and reconstructs Team objects.
 - Implements CsvReadable<List<Team>> to provide a consistent reading interface.
 - LinkedHashMap is sued to preserve team order.
 - Parsing logic is encapsulated so it keep the file handling separate.
 */

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


                String[] cols = line.split(",", -1);
                // skip malformed rows
                if (cols.length < 9) continue;

                try {
                    //Parse the columns sequentially from the formed team file
                    int teamNumber = Integer.parseInt(cols[0].trim());
                    String id = cols[1].trim();
                    String name = cols[2].trim();
                    String email = cols[3].trim();
                    Interest interest = Interest.valueOf(cols[4].trim());
                    int skillLevel = Integer.parseInt(cols[5].trim());
                    Role role = Role.valueOf(cols[6].trim());
                    int personalityScore = Integer.parseInt(cols[7].trim());
                    PersonalityType personalityType = PersonalityType.valueOf(cols[8].trim());

                    //Build the participant through the validated constructor
                    Participant participant = new Participant(
                            id,
                            name,
                            email,
                            interest,
                            skillLevel,
                            role,
                            personalityScore,
                            personalityType
                    );

                    Team team = teamMap.computeIfAbsent(teamNumber, k -> new Team(teamNumber));

                    team.addMember(participant);

                } catch (IllegalArgumentException | IndexOutOfBoundsException except) {
                    //Catch corrupted rows silently and continue, since participant doesn't need corrupted row info.
                    // Lookup will handle missing assigment gracefully.

                }
            }
        } catch (IOException e) {
            System.out.println("Error reading formed teams CSV: " + e.getCause());
        }

        return new ArrayList<>(teamMap.values());
    }

    public List<Team> readDefaultFile() {
        return readFromCsv(DEFAULT_FILE_PATH);
    }
}
