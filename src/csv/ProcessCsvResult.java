package csv;

import base.Participant;

import java.util.ArrayList;
import java.util.List;

public class ProcessCsvResult {
    // This will take all the read and stored things and will give it out nicely so the particiapnts and the issues
    // can be passed and moved around nicely


    private final List<Participant> validParticipants;//storing the correct rows
    private final List<CsvRowWarning> warnings; //Stroing the issues caught when reading the file

    public ProcessCsvResult(List<Participant> validParticipants, List<CsvRowWarning> warnings) {
        this.validParticipants = validParticipants != null ? validParticipants : new ArrayList<>();
        this.warnings = warnings != null ? warnings : new ArrayList<>();
    }

    //Getters
    public List<Participant> getValidParticipants() {
        return validParticipants;
    }
    public List<CsvRowWarning> getWarnings() {
        return warnings;
    }


    public boolean hasWarnings() {
        return !warnings.isEmpty();
    }

    public boolean hasValidParticipants() {
        return !validParticipants.isEmpty();
    }
}
