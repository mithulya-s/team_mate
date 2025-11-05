package cli;

import java.util.Scanner;

public class OrganizerCli {
    private final Scanner scanner;

    public OrganizerCli(Scanner scanner) {
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

        //Asking for the team size
        System.out.println("Enter desired team size: ");
        int desiredTeamSize = Integer.parseInt(scanner.nextLine().trim()); //since the size can't be zero






    }

}
