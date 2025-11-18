package cli;

import base.Participant;
import csv.TeamsToCsvWriter;
import services.FormationController;
import services.OrganizerDataPrompter;
import utilities.TeamDisplayer;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;



public class OrganizerCli {
    private final Scanner scanner;

    public OrganizerCli(Scanner scanner) {
        this.scanner = scanner;
    }


    public List<List<Participant>> manageOrganizerFlow() {
        System.out.println("Welcome to Organizer CLI");
        System.out.println("===========================================================");

        try {
            OrganizerDataPrompter organizerDataPrompter = new OrganizerDataPrompter(scanner);

            //calling helper to file path
            String filePath = organizerDataPrompter.promptForPath();

            List<Participant> compiledParticipants = organizerDataPrompter.loadParticipants(filePath);

            if (compiledParticipants == null || compiledParticipants.isEmpty()) {
                System.out.println("\n❌ No valid participants found in the file.");
                System.out.println("💡 Please check the file format and try again.\n");
                return new ArrayList<>();
            }



            //calling the team formation and getting the formed teams.
            FormationController formationController = new FormationController(scanner);

            int teamSize=formationController.promptForTeamSize(compiledParticipants.size());

            List<List<Participant>> formedTeams= formationController.callFormTeams(compiledParticipants,teamSize);

            if (formedTeams == null || formedTeams.isEmpty()) {
                System.out.println("\n❌ Team formation could not be completed.");
                System.out.println("💡 Please check participant data and try again.\n");
                return new ArrayList<>();
            }

            displayAndExportTeams(formedTeams); //for the last steps, no instance because in the same class

            return formedTeams;

        } catch (Exception e){
            System.err.println("\n❌ An unexpected error occurred in organizer flow:");
            System.err.println("   " + e.getMessage());
            System.out.println("💡 Please try again or contact support.\n");
            return new ArrayList<>();
        }

    }


    //separate function to calling the team dispaly and exporting orchestration
    public void displayAndExportTeams(List<List<Participant>> formedTeams) {
        if (formedTeams == null || formedTeams.isEmpty()) {
            System.out.println(" No teams to display or export.\n");
            return;
        }

        try{
            TeamDisplayer.displayTeams(formedTeams);

            //Asking whether he wants it imported.
            System.out.print("Would you like to export these teams to a CSV file? (Y/N): ");
            String exportInp= scanner.nextLine().trim().toLowerCase();

            if (exportInp.equals("y") || exportInp.equals("yes")) {
                try {
                    TeamsToCsvWriter.writeTeamsToCsv(formedTeams);
                    System.out.println("Teams exported successfully!\n");
                } catch (Exception e) {
                    System.err.println("Failed to write formed teams to CSV:");
                    System.err.println("   " + e.getMessage());
                    System.out.println("💡 Teams are formed, but could not write to CSV. failed.\n");
                }
            } else {
                System.out.println("Exporting to CSV skipped.\n");
            }

        } catch (Exception e) {
            System.err.println(" Error writing to teams to file:");
            System.err.println("   " + e.getMessage());
        }

    }

}
