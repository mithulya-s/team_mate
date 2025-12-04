package base;

import java.util.ArrayList;
import java.util.List;

/*
- Depicts a team formed from multiple participants.
- Each team has a unique team number and a list of members.
 */
public class Team {
    private final int teamNumber;
    private final List<Participant> members;

    public Team(int teamNumber) {
        this.teamNumber = teamNumber;
        this.members = new ArrayList<>();
    }

    public int getTeamNumber() {
        return teamNumber;
    }

    public List<Participant> getMembers() {
        return members;
    }

    //Adds the participant to the team if not null
    public void addMember(Participant participant) {
        if (participant != null) members.add(participant);
    }

    //To get the current number of members in the team.
    public int size() {
        return members.size();
    }

    //For display
    @Override
    public String toString() {
        return
                "Team " + teamNumber + " (" + size() + " members)";
    }
}
