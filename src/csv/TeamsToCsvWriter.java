package csv;

import base.Participant;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

public class TeamsToCsvWriter {
    //class which takes the formed teams and writes them structured into a csv file
    private static final String FILENAME = "formed_teams.csv";

    //File writing functionality
    public static void writeTeamsToCsv(List<List<Participant>> formedTeams){
        //used a printwriter since it's easier with formatted strings
        try(PrintWriter fileWriter = new PrintWriter(new FileWriter(FILENAME))){
            //headers
            fileWriter.println("Team Number,Participant ID,Name,Email,Interest,SkillLevel,Role,PersonalityScore,PersonalityType");

            for (int i=0; i<formedTeams.size(); i++){
                List<Participant> formedTeam = formedTeams.get(i);
                int teamNumber=i+1;

                for (Participant p : formedTeam){
                    fileWriter.printf("%d,%s,%s,%s,%s,%d,%s,%d,%s%n",
                            teamNumber,
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
            }
            System.out.println("\n Teams successfully written to " + FILENAME);
        }
        catch(IOException e){
            System.out.println("Error writing teams to file:  " + e.getMessage());
        }
    }
}
