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
        /* asked in the cli part
        if  (participants.isEmpty()) {
            System.out.println("No valid participants found. Please check the format and try again.");

        } else{
            System.out.println("Participant successfully loaded" + participants.size() + " participants.");
        }

         */

        System.out.println(" Loaded " + participants.size() + " valid participants.");
        return participants;

    }

    //wrapper for the breaking algo
    public List<List<Participant>> callFormTeams(List<Participant> participants, int teamSize){
        List<List<Participant>> formedTeams= TeamBuilder.formTeams(participants, teamSize);
        System.out.println("Teams formed "+formedTeams.size()+" teams.");
        return formedTeams;
    }
}

