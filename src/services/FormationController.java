package services;

import base.Participant;

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
                    System.out.println("Team size cannot be empty. Please enter a valid team size.");
                    continue;
                }

                int teamSize = Integer.parseInt(userInp);

                if (teamSize <= 0) {
                    System.out.println("Team size must be greater than zero. Try again.\n");
                    continue;
                }

                if (teamSize > totalParticipants) {
                    System.out.println("Team size (" + teamSize + ") is larger than total participants (" + totalParticipants + ").");
                    System.out.println("💡 Maximum team size is " + totalParticipants + ". Try again.\n");
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
                System.out.println("This will create approximately " + expectedTeams + " teams.\n");

                return teamSize;

            } catch (NumberFormatException e) {
                System.out.println(" Invalid input. Please enter a valid number.\n");
            }

        }
    }


    public List<List<Participant>> callFormTeams(List<Participant> participants, int teamSize) {
        if (participants == null || participants.isEmpty()) {
            System.err.println("❌ Error: No participants provided for team formation.");
            return new ArrayList<>();
        }

        if (teamSize <= 0) {
            System.err.println("❌ Error: Invalid team size (" + teamSize + ").");
            return new ArrayList<>();
        }

        if (participants.size() < teamSize) {
            System.out.println("⚠️  Warning: Not enough participants (" + participants.size()
                    + ") to form a complete team of size " + teamSize + ".");
        }

        System.out.println("\n⚙️  Forming teams...");

        try {
            TeamBuilderAlgorithm.TeamFormationResult result = TeamBuilderAlgorithm.formTeams(participants, teamSize);

            if (result == null) {
                System.err.println("❌ Team formation returned null result.");
                return new ArrayList<>();
            }

            List<List<Participant>> formedTeams = result.getFormedTeams();

            if (formedTeams == null || formedTeams.isEmpty()) {
                System.err.println("❌ Team formation failed. No teams created.");

                if (result.hasPooledParticipants()) {
                    System.out.println("ℹ️  All " + result.getPooledParticipants().size()
                            + " participant(s) were pooled (insufficient for complete teams).");
                }

                return new ArrayList<>();
            }

            System.out.println("✅ Successfully formed " + formedTeams.size() + " team(s).");

            if (result.hasPooledParticipants()) {
                List<Participant> pooled = result.getPooledParticipants();
                System.out.println("\n⚠️  " + pooled.size() + " participant(s) pooled (not assigned to teams):");
                for (Participant p : pooled) {
                    System.out.println("    • " + p.getId() + " - " + p.getFullName());
                }
                System.out.println();
            }

            return formedTeams;

        } catch (Exception e) {
            System.err.println("\n❌ Error during team formation:");
            System.err.println("   " + e.getMessage());
            System.out.println("💡 Please check participant data and try again.\n");
            return new ArrayList<>();
        }
    }


}
