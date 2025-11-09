package services;

import base.Participant;

import java.util.List;

public class ParticipantLookup {

    // lookup functions which seraches the formed teasm to retrieve both the participant's details as well
    //  the team details.

    public Participant findParticipantById(String id, List<List<Participant>> formedTeams) {
        for (List<Participant> team : formedTeams) {
            for (Participant participant : team) {
                if (participant.getId().equalsIgnoreCase(id)) {
                    return participant;
                }
            }
        }
        return null; //if he's not there
    }

    public List<Participant> findTeamByParticipant(String id, List<List<Participant>> formedTeams) {
        for (List<Participant> team : formedTeams) {
            for (Participant participant : team) {
                if (participant.getId().equalsIgnoreCase(id)) {
                    return team;
                }
            }
        }
        return null;
    }
}
