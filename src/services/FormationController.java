package services;

import base.Participant;
import base.Team;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/*
    Coordinates the team formation process.
    Flow:
       - Prompts organizer for desired team size.
       - Delegates team formation to FormationRunner.
       - Provides feedback on formed teams and participants.
 */
public class FormationController {
    private final Scanner scanner;

    public FormationController(Scanner scanner) {
        this.scanner = scanner;
    }


    // To prompt for team size with validations
    public int promptForTeamSize(int totalParticipants) {
        System.out.println("Total valid participants loaded: "+ totalParticipants);


        while (true) {
            System.out.println("\nEnter the desired team size: ");

            try {
                String userInp= scanner.nextLine().trim();

                if (userInp.isEmpty()) {
                    System.out.println("Team size cannot be empty. Please enter a valid team size.\n");
                    continue;
                }

                int teamSize = Integer.parseInt(userInp);

                if (teamSize <= 0) {
                    System.out.println("Team size must be greater than zero.Please enter a valid team size.\n");
                    continue;
                }

                if (teamSize > totalParticipants) {
                    System.out.println("Team size (" + teamSize + ") is larger than total participants (" + totalParticipants + ").");
                    System.out.println("Maximum team size is " + totalParticipants + ". Try again.\n");
                    continue;
                }

                //warnings for tiny team
                if (teamSize == 1) {
                    System.out.print("⚠️ Team size of 1 means no teaming. Continue? (Y/N): ");
                    String confirm = scanner.nextLine().trim().toLowerCase();
                    if (!confirm.equals("y")) {
                        continue;
                    }
                }


                //warning for large team
                if (teamSize > 10) {
                    System.out.print("⚠️ Large team size (" + teamSize + "). Are you sure? (Y/N): ");
                    String confirm = scanner.nextLine().trim().toLowerCase();
                    if (!confirm.equals("y")) {
                        continue;
                    }
                }


                int expectedTeams = (int) Math.ceil((double) totalParticipants / teamSize);

                return teamSize;

            } catch (NumberFormatException e) {
                System.out.println(" Invalid input. Please enter a valid team size.\n");
            }
        }
    }



     //Calls FormationRunner to form teams from participants and displays summary of formed teams and pool.
    public List<Team> callFormTeams(List<Participant> participants, int teamSize) {

        if (participants == null || participants.isEmpty()) {
            System.out.println("No participants provided for team formation.");
            return new ArrayList<>();
        }

        System.out.println("\nRunning parallel team formation attempts...");

        // Thread pool config to form the teams
        FormationRunner runner =new FormationRunner(8, 4);

        TeamBuilder.TeamFormationResult result = runner.runFormationThreads(participants, teamSize);

        //Getting the list of the final formed teams
        List<Team> formedTeams = result.getFormedTeams();


        System.out.println("Formed " + formedTeams.size()+ " teams." );
        System.out.println("Pooled leftover size: " + result.getPooledParticipants().size());

        //Send the teams back to the calling 'organizerService' class
        return formedTeams;
    }
}
