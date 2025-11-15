package cli;

import base.Participant;
import csv.TeamsToCsvWriter;
import services.OrganizerService;
import utilities.TeamDisplayer;

import java.util.List;
import java.util.Scanner;



public class OrganizerInput {
    private final Scanner scanner;

    public OrganizerInput(Scanner scanner) {
        this.scanner = scanner;
    }
    public List<List<Participant>> manageOrganizerFlow() {
        System.out.println("Welcome to Organizer CLI");

        //calling helper to file path
        String filePath = promptForPath();
        int teamSize=promptForTeamSize();

        OrganizerService organizerService = new OrganizerService();
        List<Participant> compiledParticipants = organizerService.loadParticipants(filePath);

        if (compiledParticipants.isEmpty()) {
            System.out.println("No valid participants found. Exiting...");
            return null;
        }

        //calling the team formation and getting the formed teams.
        List<List<Participant>> formedTeams= organizerService.callFormTeams(compiledParticipants,teamSize);

        displayAndExportTeams(formedTeams); //for the last steps, no instance because in the same class

        return formedTeams;
    }

    //helper for path
    private String promptForPath() {
        System.out.println("Please enter the path to the participants records " +
                "or press Enter to use the default.");
        String inp= scanner.nextLine().trim();
        return inp.isEmpty()? "participants.csv" : inp;
    }

    //helper for size
    private int promptForTeamSize() {
        System.out.println("Enter the desired team size: ");
        while (true) {
            try {
                int teamSize = Integer.parseInt(scanner.nextLine().trim()); //since no zero
                if (teamSize>0) {
                    return teamSize;
                }
                System.out.println("Team size must be greater than zero. Try again: ");
            }
            catch (NumberFormatException e) {
                System.out.println("Invalid value for team size. Try again: ");
            }
        }
    }


    //separate function to calling the team dispaly and exporting orchestration
    public void displayAndExportTeams(List<List<Participant>> formedTeams) {
        TeamDisplayer.displayTeams(formedTeams);

        //Asking whether he wants it imported.
        System.out.print("Would you like to export these teams to a CSV file? (Y/N): ");
        String exportInp= scanner.nextLine().trim().toLowerCase();

        if (exportInp.equals("y")) {
            TeamsToCsvWriter.writeTeamsToCsv(formedTeams);

            //redundant sentence so removed it.
            //System.out.println("Formed Teams written to CSV file successfully.");
        }
    }
}
