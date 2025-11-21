package csv;

public interface CsvReadable<T> {
    T readFromCsv(String filePath);
}