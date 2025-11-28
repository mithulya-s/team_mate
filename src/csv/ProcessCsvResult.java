package csv;

import base.Participant;

import java.util.Collections;
import java.util.List;
import java.util.Map;


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

    /*
    public boolean hasWarnings() {
        return !warningsByRow.isEmpty();
    }

    public boolean hasValidParticipants() {
        return !validParticipants.isEmpty();
    }

     */
}
