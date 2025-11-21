package services;

import base.Participant;

import java.util.List;

public class ParticipantLookup {
    // lookup functions which seraches the formed teasm to retrieve both the participant's details as well
    //  the team details.







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
