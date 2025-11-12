package csv;

import base.Participant;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ParticipantCsvReader {
    //The class to mainly read the uploaded file from the organizer.
    // This will also call the helpers of row warnings and participant collectors.
    // Then this utilizes the read result class to output the 2 lists nicely so that its easy to be used after this.

    //Instance to read the rows each
    private final RowHandler rowHandler= new RowHandler();

    //function which will return the output class thing, because we made it so
    public ProcessCsvResult readFile(String path) {
        List<Participant> validParticipants = new ArrayList<>();
        List<CsvRowWarning> warnings = new ArrayList<>();

        //file reader, buffered since then it's easier
        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            String row;
            int rowNumber=1;
            // to record the header, a flag
            boolean isHeaderLine=true;

            while ((row=br.readLine()) !=null){
                //header condtion
                if (isHeaderLine){
                    isHeaderLine=false;
                    rowNumber++;
                    continue;
                }

                //skip empty lines (safeguarding more)
                if (row.isEmpty()){
                    rowNumber++;
                    continue;
                }

                String[] cols = row.split(",");

                RowHandler.Result result = rowHandler.readRow(cols);

                if (result.isValidLine()){
                    validParticipants.add(result.getParticipant()); //adding the object if it's good
                }else {
                    //debugs
                    List<String> rowWarnings = result.getWarnings();
                    System.out.println("DEBUG: Row " + rowNumber + " has " + rowWarnings.size() + " warnings");
                    for (String w : rowWarnings) {
                        System.out.println("  Warning: '" + w + "'");
                    }


                    warnings.add(new CsvRowWarning(rowNumber,result.getWarnings()));
                }
                rowNumber++;
            }
        }
        catch (IOException e){
            warnings.add(new CsvRowWarning(-1,List.of("File could not be read: "+ e.getMessage())));
        }
        return new ProcessCsvResult(validParticipants,warnings);
    }
}
