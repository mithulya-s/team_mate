package csv;

import java.io.IOException;
import java.util.List;

/*
This class:
      -  Generic contract for writing objects to a CSV file.
      - Provides abstraction for CSV writers, ensuring consistency across different implementations.
      - Helps with separation of concerns so that the logic is encapsulated in implementing classes,
         while higher layers only depend on this interface.
*/
public interface CsvWritable<T> {
    void writeToCsv(List<T> items, String filePath) throws IOException;
}