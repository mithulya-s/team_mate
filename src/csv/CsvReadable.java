package csv;

/*
 - This class:
        - Generic contract for reading objects from a CSV file.
        - Provides abstraction for CSV readers, ensuring consistency across different implementations.
        - Helps with separation of concerns so that the parsing logic is encapsulated in implementing classes,
            while higher layers only depend on this interface.
 */
public interface CsvReadable<T> {
    T readFromCsv(String filePath);
}