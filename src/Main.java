import base.Participant;
import cli.OrganizerInput;
import cli.ParticipantLookupCli;
import services.SurveyService;

import java.util.List;
import java.util.Scanner;

public class Main {
    private static List<List<Participant>> formedTeams; // shared state

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("🎮 Welcome to the University Gaming Club!");

        while (true) {
            System.out.println("\nWho are you?");
            System.out.println("1. Participant");
            System.out.println("2. Organizer");
            System.out.println("0. Exit");

            int choice = processChoice(scanner, 2);

            if (choice == 1) {
                initiateParticipantFlow(scanner);
            } else if (choice == 2) {
                initiateOrganizerFlow(scanner);
            } else {
                System.out.println("👋 Exiting. See you next time!");
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

            int choice = processChoice(scanner, 2);

            if (choice == 1) {
                SurveyService surveyService = new SurveyService(scanner);
                surveyService.initiateSurvey(); // assuming this method exists
            } else if (choice == 2) {
                ParticipantLookupCli lookupCli = new ParticipantLookupCli(scanner);
                lookupCli.setFormedTeams(formedTeams); // inject team state
                lookupCli.manageLookupFlow();
            } else {
                break;
            }
        }
    }

    private static void initiateOrganizerFlow(Scanner scanner) {
        OrganizerInput orgInput = new OrganizerInput(scanner);

        while (true) {
            System.out.println("\n🛠️ Organizer Menu");
            System.out.println("1. Upload participant file and form teams");
            System.out.println("2. Export formed teams");
            System.out.println("0. Back to main menu");

            int choice = processChoice(scanner, 2);

            if (choice == 1) {
                formedTeams = orgInput.manageOrganizerFlow(); // store teams
            } else if (choice == 2) {
                if (formedTeams == null || formedTeams.isEmpty()) {
                    System.out.println("❌ No teams formed yet. Please form teams first.");
                } else {
                    orgInput.displayAndExportTeams(formedTeams);
                }
            } else {
                break;
            }
        }
    }

    private static int processChoice(Scanner scanner, int maxOption) {
        while (true) {
            System.out.print("Enter your choice: ");
            try {
                int choice = Integer.parseInt(scanner.nextLine().trim());
                if (choice >= 0 && choice <= maxOption) return choice;
                System.out.println("Invalid choice. Please enter a number between 0 and " + maxOption + ".");
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number.");
            }
        }
    }
}