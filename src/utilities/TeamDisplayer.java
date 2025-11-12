package utilities;

import base.Participant;

import java.util.List;

public class TeamDisplayer {

    //function to display formed teams
    public static void displayTeams(List<List<Participant>> formedTeams){
        System.out.println("\nFormed Teams:");

        for (int i = 0; i<formedTeams.size(); i++){
            List<Participant> team = formedTeams.get(i);
            System.out.print("Team " + (i+1) + ": \n");

            for (Participant p : team){
                System.out.printf("%s,%s,%s,%s,%d,%s,%d,%s%n",
                        p.getId(),
                        p.getFullName(),
                        p.getEmail(),
                        p.getInterest(),
                        p.getSkillLevel(),
                        p.getRole(),
                        p.getPersonalityScore(),
                        p.getPersonalityType()
                );
            }
            System.out.println(); //the blnak line for the team breaks
        }
    }
}
