package csv;

import base.Participant;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class ParticipantCsvWriter {
    //Class to write the built participant object once it's built.

    private static final String FILE_PATH ="participants.csv";

    //function to write the participants to the file
    public static void saveParticipantToCsv(Participant participant) {
        boolean fileExists = new File(FILE_PATH).exists();

        try (PrintWriter fileWriter = new PrintWriter(new FileWriter(FILE_PATH,true))) {
            //writing the headers if not
            if (!fileExists) {
                fileWriter.println("ID,Name,Email,PreferredGame,SkillLevel,PreferredRole,PersonalityScore,PersonalityType");
            }

            //writing the partiicapnt to the file
            //fomratted string which to be filled at the end
            fileWriter.printf("%s,%s,%s,%s,%d,%s,%d,%s%n",
                    participant.getId(),
                    participant.getFullName(),
                    participant.getEmail(),
                    participant.getInterest(),
                    participant.getSkillLevel(),
                    participant.getRole(),
                    participant.getPersonalityScore(),
                    participant.getPersonalityType()
            );
        }
        catch (IOException e) {
            System.out.println("Error in writing participant to file: "+e.getMessage());
        }
    }
}
