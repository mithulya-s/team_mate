package csv;

import base.Participant;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class ParticipantCsvReader {
    private final RowHandler rowHandler = new RowHandler();

    public ProcessCsvResult readFile(String path) {
        List<Participant> validParticipants = new ArrayList<>();
        List<CsvRowWarning> warnings = new ArrayList<>();

        if (path == null || path.trim().isEmpty()) {
            warnings.add(new CsvRowWarning(-1, List.of("File path cannot be empty.")));
            return new ProcessCsvResult(validParticipants, warnings);
        }


        //think about this a bit

        if (!Files.exists(Paths.get(path))) {
            warnings.add(new CsvRowWarning(-1, List.of("File not found: " + path)));
            return new ProcessCsvResult(validParticipants, warnings);
        }

        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            String row;
            int rowNumber = 1;
            boolean isHeaderLine = true;

            while ((row = br.readLine()) != null) {
                if (isHeaderLine) {
                    isHeaderLine = false;
                    rowNumber++;
                    continue;
                }

                if (row.trim().isEmpty()) {
                    rowNumber++;
                    continue;
                }

                String[] cols = row.split(",", -1);

                try {
                    RowHandler.Result result = rowHandler.readRow(cols);

                    if (result.isValidLine()) {
                        validParticipants.add(result.getParticipant());
                    } else {
                        warnings.add(new CsvRowWarning(rowNumber, result.getWarnings()));
                    }
                } catch (Exception e) {
                    warnings.add(new CsvRowWarning(rowNumber,
                            List.of("Unexpected error processing row: " + e.getMessage())));
                }

                rowNumber++;
            }

        } catch (FileNotFoundException e) {
            warnings.add(new CsvRowWarning(-1, List.of("File not found: " + path)));
        } catch (IOException e) {
            warnings.add(new CsvRowWarning(-1, List.of("Error reading file: " + e.getMessage())));
        } catch (Exception e) {
            warnings.add(new CsvRowWarning(-1, List.of("Unexpected error: " + e.getMessage())));
        }

        return new ProcessCsvResult(validParticipants, warnings);
    }
}