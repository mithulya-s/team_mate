import base.Team;
import services.OrganizerService;
import services.ParticipantLookup;
import services.SurveyService;
import utilities.Authenticator;

import java.util.List;
import java.util.Scanner;

/*
 - Entry point for the TEAM_MATE application.
 - This directs control flow between Participant and Organizer modules.
 - Modularized design to keep the Participant and Organizer flows separate.
 - Concerns are separated - Survey, lookup, formation and export flows are delegated to the controllers
    for orchestration.
 */

public class Main {

    // Stores the list of formed teams
    private static List<Team> formedTeams;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("\n================================================================================    ");
        System.out.println("                        🎮 Welcome to TEAM-MATE! 🎮                                   ");
        System.out.println("================================================================================\n    ");


        // Main menu loop
        while (true) {

            System.out.println(" \nPlease select your role:                                                         ");
            System.out.println("================================================================================    ");
            System.out.println("        1  :  Participant                                                           ");
            System.out.println("        2  :  Organizer                                                             ");
            System.out.println("        0  :  Exit                                                                  ");
            System.out.println("================================================================================    ");

            int choice = processChoice(scanner);

            if (choice == 1) {
                initiateParticipantFlow(scanner);
            } else if (choice == 2) {
                initiateOrganizerFlow(scanner);
            } else {
                System.out.println("\nExiting TEAM-MATE. See you next time!");
                break;
            }
        }

        scanner.close();
    }


    /*
     - Handles the participant menu flow.
     - Main does not handle survey or lookup logic directly, but delegates to SurveyService and ParticipantLookup.
     */

    private static void initiateParticipantFlow(Scanner scanner) {

        // Participant menu loop
        while (true) {
            System.out.println("\n============================================================================  ");
            System.out.println("                         PARTICIPANT MENU                                       ");
            System.out.println("============================================================================    ");
            System.out.println("    1 : Fill out the survey                                                     ");
            System.out.println("    2 : Lookup your assigned team                                               ");
            System.out.println("    0 : Return to main menu                                                     ");
            System.out.println("============================================================================    ");

            int choice = processChoice(scanner);

            if (choice == 1) {
                SurveyService surveyService = new SurveyService(scanner);
                surveyService.initiateSurvey();
            } else if (choice == 2) {
                ParticipantLookup searcher = new ParticipantLookup(scanner);
                searcher.manageLookupFlow(formedTeams);
            } else {
                System.out.println("\nReturning to main menu...");
                break;
            }
        }
    }


    /*
     - Handles the organizer menu flow.
     - Includes authentication before allowing access to team formation and export features.
     - Main is modularized so it delegates to the organizerService class for flow orchestration
        and handling.
     */
    private static void initiateOrganizerFlow(Scanner scanner) {

        //Authentication check
        Authenticator auth = Authenticator.getInstance();
        if (!auth.isAuthenticated()) {
            boolean loginSuccess = auth.login(scanner);
            if (!loginSuccess) {
                System.out.println("\nAuthentication failed. Returning to main menu...");
                return;
            }
        }

        // Organizer menu loop
        while (true) {
            System.out.println("\n================================================================================  ");
            System.out.println("                             ORGANIZER MENU                                          ");
            System.out.println("================================================================================    ");
            System.out.println("   1 : Upload participant records and form teams                                    ");
            System.out.println("   2 : View and export formed teams                                                 ");
            System.out.println("   0 : Return to main menu                                                          ");
            System.out.println("================================================================================    ");

            int choice = processChoice(scanner);
            OrganizerService orgCli = new OrganizerService(scanner);

            if (choice == 1) {
                formedTeams = orgCli.manageFormationFlow(); // store teams
            } else if (choice == 2) {
                orgCli.displayAndExportTeams(formedTeams);
            } else {
                System.out.println("\nReturning to main menu...");
                break;
            }
        }
    }


    /*
    - Utility to safely handle menu input and choices.
    - Encapsulated validation logic in a reusable method to improve readability and
        reduce redundancy.
     */

    private static int processChoice(Scanner scanner) {
        while (true) {
            System.out.print("Enter your choice :  ");
            try {
                int choice = Integer.parseInt(scanner.nextLine().trim());
                if (choice >= 0 && choice <= 2) return choice;
                System.out.println("Invalid input. Please enter a number between 0 and 2. ");
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number between 0 and 2. ");
            }
        }
    }
}