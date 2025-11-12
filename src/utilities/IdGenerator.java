package utilities;

import cli.ParticipantLookupCli;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Random;

public class IdGenerator {
    //Upgrade to iterate with the CSV later. After the decison of DB has been done
    private static final String FILE_PATH="participants.csv";
    private static final String PREFIX="P";

    public static String generateId(){
        int maxId=findMaxId();
        int nextId=maxId+1;
        return String.format("%s%03d", PREFIX, nextId);
    }

    private static int findMaxId(){
        File file = new File(FILE_PATH);

        //if it doesnt' exist
        if (!file.exists()){
            return 0;
        }
        int maxId=0;

        //reading the file for the last number
        try (BufferedReader br = new BufferedReader(new FileReader(file))){
            String line;
            boolean isHeaderLine=true;

            while ((line=br.readLine())!=null){
                //skip header line
                if (isHeaderLine){
                    isHeaderLine=false;
                    continue;
                }

                if (line.trim().isEmpty()){
                    continue;
                }

                //get the id from the line
                String[] cols = line.split(",");
                if (cols.length>0){
                    String id=cols[0].trim();
                    int idNum=extractIdNum(id);
                    if (idNum>maxId){
                        maxId=idNum;
                    }
                }
            }
        }
        catch (IOException e){
            System.err.println("Warning: Could not read participant file for ID generation. Starting from P001.");
            System.err.println("Error: " + e.getMessage());
            return 0;
        }
        return maxId;
    }

    //helper to extract num
    private static int extractIdNum(String id) {
        if (id == null || id.trim().isEmpty()) {
            return 0;
        }

        //ditch the prefix
        if (id.startsWith(PREFIX)) {
            String numPart = id.substring(PREFIX.length());

            try {
                return Integer.parseInt(numPart);
            } catch (NumberFormatException e) {
                System.err.println("Invalid ID form detected: " + id);
                return 0;
            }
        }
        return 0;
    }
}
