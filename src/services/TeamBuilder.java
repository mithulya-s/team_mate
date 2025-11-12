package services;

import base.Participant;
import utilities.AssignType;
import utilities.InterestDiversifier;
import utilities.PersonalityType;
import utilities.RoleDiversifier;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class TeamBuilder {
    //Contains teaming strategy

    public static List<List<Participant>> formTeams(List<Participant> participants, int teamSize) {
        //Shuffling participants to avoid bias
        Collections.shuffle(participants);

        //arrays to store the different personalities
        List<Participant> leaders= new ArrayList<>();
        List<Participant> thinkers = new ArrayList<>();
        List<Participant> balanced = new ArrayList<>();

        for (Participant p : participants) {
            switch (p.getPersonalityType()){
                case LEADER ->  leaders.add(p);
                case THINKER -> thinkers.add(p);
                case BALANCED -> balanced.add(p);
            }
        }

        //Calculate the number of teams.
        int totalTeams=(int)Math.ceil((double) participants.size()/teamSize); //why ceil?
        List<List<Participant>> teams = new ArrayList<>();
        for (int i = 0; i < totalTeams; i++) {
            teams.add(new ArrayList<>());
        }

        //Using the helepers to assign people to the teams
        //personalitites
        AssignType.assignByTpe(teams,leaders, PersonalityType.LEADER,1);
        AssignType.assignByTpe(teams,thinkers, PersonalityType.THINKER,2);
        AssignType.assignByTpe(teams,balanced,PersonalityType.BALANCED,teamSize);

        //keep interest constraints
        InterestDiversifier.constraintInterestDiversity(teams);

        //keep role constrsints
        RoleDiversifier.enforceRoleDiversity(teams,teamSize);

        //added this to hide the empty teams from being returned
        teams.removeIf(List::isEmpty);

        return teams;


    }
}
