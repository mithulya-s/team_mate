package csv;

import java.io.IOException;
import java.util.List;

public interface CsvWritable<T> {
    void writeToCsv(List<T> items, String filePath) throws IOException;
}