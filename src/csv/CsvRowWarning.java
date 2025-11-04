package csv;

import java.util.List;

public class CsvRowWarning {
    //  Collects the errors whilst reading a csv file.
    // This will record the row number and the string in a message
    private final int rowNumber;
    private final List<String> messages;

    //Constructor to initialize
    public CsvRowWarning(int rowNumber, List<String> messages) {
        this.rowNumber = rowNumber;
        this.messages = messages;
    }

    //Getters only, no setter since final
    public int getRowNumber() {
        return rowNumber;
    }
    public List<String> getMessages() {
        return messages;
    }

}
