package csv;

import base.Participant;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class ParticipantCsvWriter {
    //Class to write the built participant object once it's built.
    private static final String FILE_PATH ="participants.csv";
    private static final String HEADER_LINE="ID,Name,Email,PreferredGame,SkillLevel,PreferredRole,PersonalityScore,PersonalityType";


    //function to write the participants to the file
    public static void saveParticipantToCsv(Participant participant) throws IOException {

        //input validation
        if (participant == null) {
            throw new IllegalArgumentException("Participant is null. Cannot save null.");
        }

        File file = new File(FILE_PATH);
        boolean fileExists = file.exists();

        //additionally to check if we can write
        if (fileExists && !file.canWrite()) {
            System.out.println("File exists. But it is not writable.");
        }

        try (PrintWriter fileWriter = new PrintWriter(new FileWriter(FILE_PATH,true))) {
            //writing the headers if not
            if (!fileExists) {
                fileWriter.println(HEADER_LINE);
            }

            //helper usage to wote the line
            writeSingleParticipantRow(fileWriter,participant);

            //if erros come there
            if (fileWriter.checkError()){
                throw new IOException("Error while writing participant to file.");
            }
        }
        catch (IOException e) {
            throw new IOException("Error while writing participant to CSV: " + e.getMessage(),e);
        }
    }


    //helper
    private static void writeSingleParticipantRow(PrintWriter fileWriter, Participant participant){
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

    //to catch values with special characters
    private static String escapeCsvValue(String value) {
        if (value==null) {
            return "";
        }

        //if values have other garbage
        if (value.contains(",") || value.contains("\"") || value.contains("\n")) {
            //double to escape
            value = value.replace("\"", "\"\"");
            return "\"" + value + "\"";
        }
        return value;
    }

    //get the path in the torge
    public static boolean getFilePath(){
        return new File(FILE_PATH).exists();
    }

}



