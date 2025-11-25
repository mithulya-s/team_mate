import base.Participant;
import services.OrganizerService;
import services.ParticipantLookup;
import services.SurveyService;
import utilities.Authenticator;

import java.util.List;
import java.util.Scanner;

public class Main {
    private static List<List<Participant>> formedTeams; // shared single state

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("\n🎮 Welcome to the University Gaming Club!");

        while (true) {
            System.out.println("\nWho are you?");
            System.out.println("1. Participant");
            System.out.println("2. Organizer");
            System.out.println("0. Exit");

            int choice = processChoice(scanner);

            if (choice == 1) {
                initiateParticipantFlow(scanner);
            } else if (choice == 2) {
                initiateOrganizerFlow(scanner);
            } else {
                System.out.println("\nExiting. See you next time!");
                break;
            }
        }

        scanner.close();
    }

    private static void initiateParticipantFlow(Scanner scanner) {
        while (true) {
            System.out.println("\n👤 Participant Menu");
            System.out.println("1. Fill survey");
            System.out.println("2. Lookup assigned team");
            System.out.println("0. Back to main menu");

            int choice = processChoice(scanner);

            if (choice == 1) {
                SurveyService surveyService = new SurveyService(scanner);
                surveyService.initiateSurvey();
            } else if (choice == 2) {
                ParticipantLookup searcher = new ParticipantLookup(scanner);
                searcher.manageLookupFlow(formedTeams);
            } else {
                break;
            }
        }
    }

    private static void initiateOrganizerFlow(Scanner scanner) {

        //Authentication
        Authenticator auth = Authenticator.getInstance();
        if (!auth.isAuthenticated()) {
            boolean loginSuccess = auth.login(scanner);
            if (!loginSuccess) {
                System.out.println("Authentication failed. Returning to main menu.");
                return;
            }
        }

        // Menu loop
        while (true) {
            System.out.println("\n🛠️ Organizer Menu");
            System.out.println("1. Upload participant records and form teams");
            System.out.println("2. Save formed teams"); //add the view maybe
            System.out.println("0. Back to main menu");

            int choice = processChoice(scanner);
            OrganizerService orgCli = new OrganizerService(scanner);

            if (choice == 1) {
                formedTeams = orgCli.manageFormationFlow(); // store teams
            } else if (choice == 2) {
                orgCli.displayAndExportTeams(formedTeams);
            } else {
                break;
            }
        }
    }

    private static int processChoice(Scanner scanner) {
        while (true) {
            System.out.print("Enter your choice: ");
            try {
                int choice = Integer.parseInt(scanner.nextLine().trim());
                if (choice >= 0 && choice <= 2) return choice;
                System.out.println("Invalid choice. Please enter a number between 0 and " + 2 + ".");
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number between 0 and " + 2 + ".");
            }
        }
    }
}