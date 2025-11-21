package services;

import base.Participant;
import csv.FormedTeamsCsvReader;

import java.util.List;
import java.util.Scanner;

public class ParticipantLookup {
    // lookup functions which seraches the formed teasm to retrieve both the participant's details as well
    //  the team details
    private final Scanner scanner;

    public ParticipantLookup(Scanner scanner) {
        this.scanner = scanner;
    }

    public void manageLookupFlow(List<List<Participant>> formedTeams) {
        System.out.println("Welcome to participant search.");

        if (formedTeams == null || formedTeams.isEmpty()) {
            try {
                formedTeams = new FormedTeamsCsvReader().readDefaultFile();
                if (formedTeams.isEmpty()) {
                    System.out.println("Sorry. Teams have not been formed yet.");
                    return;
                }
            } catch (Exception e) {
                System.out.println("No formed teams file found. Please check back later.");
                return;
            }
        }


        System.out.println("Please enter your participant ID: ");
        String participantId = scanner.nextLine().trim();

        //error handling
        if (participantId.isEmpty()){
            System.out.println("ID cannot be empty. Please enter your participant ID: ");
            return;
        }

        //if in wrong format
        if (participantId.length()<4){
            System.out.println("Invalid ID format. ID should be at least 4 characters (Eg: P001).\n ");
            return;
        }

        //searching functionality
        try {
            Participant participant = findParticipantInTeams(participantId, formedTeams);
            List<Participant> assignedTeam = findTeamByParticipant(participantId, formedTeams);

            if (participant == null || assignedTeam == null) {
                System.out.println("\n❌ No team assignment found for ID: " + participantId);
                System.out.println("\nPossible reasons:");
                System.out.println("  • ID might be incorrect (check for typos)");
                System.out.println("  • You may not have filled the survey yet");
                System.out.println("  • Teams may not have been formed with your data");
                System.out.println("\n💡 Please verify your ID or contact the organizer.\n");
            } else {
                int teamNumber = getTeamNum(assignedTeam, formedTeams);

                System.out.println("\n✅ Team Assignment Found!");
                System.out.println("=======================================================");
                System.out.println("📋 You are assigned to Team " + teamNumber);
                System.out.println("=======================================================");
                System.out.println("\n👥 Your Teammates:");

                //printing the mates
                for (Participant p : assignedTeam) {
                    if (p == null) {
                        continue;
                    }

                    if (p.getId().equalsIgnoreCase(participantId)) {
                        System.out.printf("  👉 %s (YOU)\n", p.getFullName());
                        System.out.printf("      Role: %s | Interest: %s | Skill: %d\n",
                                p.getRole(), p.getInterest(), p.getSkillLevel());
                    } else {
                        System.out.printf("  • %s\n", p.getFullName());
                        System.out.printf("      Role: %s | Interest: %s | Skill: %d\n",
                                p.getRole(), p.getInterest(), p.getSkillLevel());
                    }

                    System.out.println("\n===============================================");
                }
            }
        }
        catch (Exception e) {
            System.err.println("An error occurred during lookup: " + e.getMessage());
        }
    }

    public Participant findParticipantInTeams(String id, List<List<Participant>> formedTeams) {
        // method to get the particiapnt from the formed teams, validate erros first
        if (id == null || formedTeams.isEmpty()){
            return null;
        }

        String searchId=id.trim();

        //loop to iterate and get
        for (List<Participant> team : formedTeams) {
            if (team==null || team.isEmpty()){
                continue;
            }

            for  (Participant p : team) {
                if (p !=null && p.getId().equalsIgnoreCase(searchId)) {
                    return p;
                }
            }
        }
        return null;
    }

    // to get the team where the particiapnt is in
    public List<Participant> findTeamByParticipant(String id, List<List<Participant>> formedTeams) {
        if (id == null || id.trim().isEmpty()){
            return null;
        }
        if (formedTeams==null || formedTeams.isEmpty()){
            return null;
        }
        String searchId=id.trim();
        for (List<Participant> team : formedTeams) {
            if (team==null || team.isEmpty()){
                continue;
            }
            for  (Participant p : team) {
                if (p !=null && p.getId().equalsIgnoreCase(searchId)) {
                    return team; //this gets the team.
                }
            }
        }
        return null;
    }

    //helper
    public int getTeamNum(List<Participant> teamOfParticipant, List<List<Participant>> formedTeams ) {
        if (teamOfParticipant==null || formedTeams==null){
            return -1;
        }

        int teamNum = 1;
        for (List<Participant> team : formedTeams) {
            if (team==teamOfParticipant){
                return teamNum;
            }
            teamNum++;
        }
        return -1;
    }
}
