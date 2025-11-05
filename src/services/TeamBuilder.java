package services;

import base.Participant;

import java.util.ArrayList;
import java.util.List;

public class TeamBuilder {
    //Contains teaming strategy

    public static List<List<Participant>> dummyFormation(List<Participant> participants, int teamSize) {
        List<List<Participant>> formedTeams = new ArrayList<>();

        //just putting them into chunks
        for (int i = 0; i < participants.size(); i += teamSize) {
            int end = Math.min(i + teamSize, participants.size());
            formedTeams.add(new ArrayList<>(participants.subList(i, end)));
        }

        return formedTeams;
    }
}
