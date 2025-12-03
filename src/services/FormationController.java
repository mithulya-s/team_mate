package services;

import base.Participant;
import base.Team;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class FormationController {
    private final Scanner scanner;

    public FormationController(Scanner scanner) {
        this.scanner = scanner;
    }


    //helper for size
    public int promptForTeamSize(int totalParticipants) {
        System.out.println("\n Total valid participants loaded: "+ totalParticipants);


        while (true) {
            System.out.println("Enter the desired team size: ");

            try {
                String userInp= scanner.nextLine().trim();

                if (userInp.isEmpty()) {
                    System.out.println("Team size cannot be empty. Please enter a valid team size.\n");
                    continue;
                }

                int teamSize = Integer.parseInt(userInp);

                if (teamSize <= 0) {
                    System.out.println("Team size must be greater than zero. Try again.\n");
                    continue;
                }

                if (teamSize > totalParticipants) {
                    System.out.println("Team size (" + teamSize + ") is larger than total participants (" + totalParticipants + ").");
                    System.out.println("Maximum team size is " + totalParticipants + ". Try again.\n");
                    continue;
                }

                if (teamSize == 1) {
                    System.out.print("⚠️ Team size of 1 means no teaming. Continue? (Y/N): ");
                    String confirm = scanner.nextLine().trim().toLowerCase();
                    if (!confirm.equals("y")) {
                        continue;
                    }
                }

                //think about this a bot
                /*
                if (teamSize > 10) {
                    System.out.print("⚠️ Large team size (" + teamSize + "). Are you sure? (Y/N): ");
                    String confirm = scanner.nextLine().trim().toLowerCase();
                    if (!confirm.equals("y")) {
                        continue;
                    }
                }

                 */

                int expectedTeams = (int) Math.ceil((double) totalParticipants / teamSize);
                //System.out.println("This will create approximately " + expectedTeams + " teams.\n");

                return teamSize;

            } catch (NumberFormatException e) {
                System.out.println(" Invalid input. Please enter a valid number.\n");
            }

        }
    }

    public List<Team> callFormTeams(List<Participant> participants, int teamSize) {

        if (participants == null || participants.isEmpty()) {
            System.err.println("❌ No participants provided for team formation.");
            return new ArrayList<>();
        }

        System.out.println("\n⚙️ Running team formation (parallel attempts)...");

        // parallel attempts
        FormationRunner runner =
                new FormationRunner(
                        8,
                        4
                );

        TeamBuilder.TeamFormationResult result =
                runner.runFormationThreads(participants, teamSize);

        List<Team> formedTeams = result.getFormedTeams();

        System.out.println("✅ Best result chosen:");
        System.out.println(" - Formed teams: " + formedTeams.size());
        System.out.println(" - Pooled leftover: " + result.getPooledParticipants().size());

        return formedTeams;
    }







}
