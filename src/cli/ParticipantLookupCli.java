package cli;

import base.Participant;
import services.ParticipantLookup;

import java.util.List;
import java.util.Scanner;

public class ParticipantLookupCli {
    private final Scanner scanner;

    public ParticipantLookupCli(Scanner scanner) {
        this.scanner = scanner;
    }

    public void manageLookupFlow(List<List<Participant>> formedTeams) {
        System.out.println("Welcome to participant search.");

        if (formedTeams == null || formedTeams.isEmpty()) {
            System.out.println("Sorry. Teams have not been formed yet. Please check back later.");
            return;
        }

        System.out.println("Please enter your participant ID: ");
        String participantId = scanner.nextLine().trim();

        ParticipantLookup lookupFunctions = new ParticipantLookup();
        List<Participant> assignedTeam = lookupFunctions.findTeamByParticipant(participantId, formedTeams);

        if (assignedTeam==null){
            System.out.println("Sorry. No team found with that participant ID. " +
                    "Please verify ID and try again.");

        }else {
            int teamNumber= formedTeams.indexOf(assignedTeam)+1;
            System.out.println("\n You are in Team " + teamNumber + " with:");


            for (Participant p : assignedTeam) {
                //highlight the one who looked for and show the others
                if (p.getId().equalsIgnoreCase(participantId)) {
                    System.out.printf(" %s (YOU) - %s, %s\n",
                            p.getFullName(),
                            p.getRole(),
                            p.getInterest());
                } else {
                    System.out.printf("  - %s (%s, %s)\n",
                            p.getFullName(),
                            p.getRole(),
                            p.getInterest());
                }
            }
            System.out.println("\n"); //for niceness.
        }

    }
}
