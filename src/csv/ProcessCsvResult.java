package csv;

import base.Participant;

import java.util.Collections;
import java.util.List;
import java.util.Map;


/*
 - This class encapsulates the result of processing a participant CSV file.
 - Further:
    - Stores valid Participant objects parsed from the file.
    - Collects warnings mapped to row numbers for invalid or malformed data.

    - Fields are final and exposed only through getters.
    - Defensive copies ensures null inputs are replaced with empty collections.
    - Provides unmodifiable access to warnings to preserve integrity across
 */

public class ProcessCsvResult {

    private final List<Participant> validParticipants;
    private final Map<Integer, List<String>> warningsByRow;

    public ProcessCsvResult(List<Participant> validParticipants,
                            Map<Integer, List<String>> warningsByRow) {
        this.validParticipants = validParticipants != null ? validParticipants : List.of();
        this.warningsByRow = warningsByRow != null ? warningsByRow : Collections.emptyMap();
    }

    public List<Participant> getValidParticipants() {
        return validParticipants;
    }

    public Map<Integer, List<String>> getWarningsByRow() {
        return Collections.unmodifiableMap(warningsByRow);
    }

}
