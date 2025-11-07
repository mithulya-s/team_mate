package cli;

import base.Participant;
import csv.TeamsToCsvWriter;
import services.OrganizerService;

import java.util.List;
import java.util.Scanner;

import static utilities.TeamDisplayer.displayTeams;

public class OrganizerInput {
    private final Scanner scanner;

    public OrganizerInput(Scanner scanner) {
        this.scanner = scanner;
    }
    public void welcomeOrganizer() {
        System.out.println("Welcome to Organizer CLI");

        //Asking for the file
        System.out.println("Enter file path for the participant records or press " +
                "Enter to use the existing participant file:\n ");
        String filePath = scanner.nextLine().trim();

        //if the file path is empty get the particiapnt csv file
        if (filePath.isEmpty()) filePath="participants.csv";

        //first load the participants and store it
        OrganizerService organizerService = new OrganizerService();
        List<Participant> readParticipants= organizerService.loadParticipants(filePath);

        //calling the team formation and getting the formed teams.
        List<List<Participant>> formedTeams= organizerService.getFormedTeams(readParticipants);

        //callig the team dsipalyer for the initial display tot he cosole
        displayTeams(formedTeams);

        //Asking whether he wants it imported.
        System.out.print("Would you like to export these teams to a CSV file? (Y/N): ");
        String exportInp= scanner.nextLine().trim().toLowerCase();

        if (exportInp.equals("y")) {
            TeamsToCsvWriter.writeTeamsToCsv(formedTeams);
            System.out.println("Formed Teams written to CSV file successfully.");
        }



    }

}
