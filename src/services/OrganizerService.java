package services;

import base.Participant;
import csv.CsvRowWarning;
import csv.ParticipantCsvReader;
import csv.ProcessCsvResult;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

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
    //the size will be asked inside this
    public List<List<Participant>> getFormedTeams(List<Participant> participants){
        Scanner scanner= new Scanner(System.in); //for the size input
        //Asking for the team size
        System.out.println("Enter desired team size: ");
        int desiredTeamSize = Integer.parseInt(scanner.nextLine().trim()); //since the size can't be zero
        System.out.println("Getting formed teams from "+desiredTeamSize+" participants...");
        List<List<Participant>> formedTeams= TeamBuilder.dummyFormation(participants,desiredTeamSize);
        System.out.println("Teams formed "+formedTeams.size()+" teams.");
        return formedTeams;
    }

    // The returned list must be sent back to the organzier cli, where it will be dispalyed with export (and also pass
    // the team size from there to here)

    //Options and other session metrics.
}
