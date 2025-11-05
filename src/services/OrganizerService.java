package services;

import base.Participant;
import csv.CsvRowWarning;
import csv.ParticipantCsvReader;
import csv.ProcessCsvResult;

import java.util.ArrayList;
import java.util.List;

public class OrganizerService {
    private final ParticipantCsvReader fileReader= new ParticipantCsvReader();

    public List<Participant> loadParticipants(String filePath){
        System.out.println("Loading participants from: "+filePath+"...");

        ProcessCsvResult result=fileReader.readFile(filePath);
        List<Participant> participants=result.getValidParticipants();
        List<CsvRowWarning> warnings=result.getWarnings();

        if (!warnings.isEmpty()) {
            System.out.println("Following errors in the participant file was detected while reading: ");
            for (CsvRowWarning warning : warnings) {
                    System.out.println("Row " + warning.getRowNumber() + ": "
                            + String.join("; ", warning.getMessages()));
                }
            }
        if  (participants.isEmpty()) {
            System.out.println("No valid participants found. Please check the format and try again.");

        } else{
            System.out.println("Participant successfully loaded" + participants.size() + " participants.");
        }
        return participants;
    }

    // Get the returned particiapnts and build the teams with the size
    public List<List<Participant>> getFormedTeams(List<Participant> participants, int teamSize){
        System.out.println("Getting formed teams from "+teamSize+" participants...");
        List<List<Participant>> formedTeams= TeamBuilder.dummyFormation(participants,teamSize);
        System.out.println("Teams formed "+formedTeams.size()+" teams.");
        return formedTeams;
    }

    // The returned list must be sent back to the organzier cli, where it will be dispalyed with export
    //Options and other session metrics.
}
