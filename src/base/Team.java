package base;

import utilities.Interest;
import utilities.Role;

import java.util.ArrayList;
import java.util.List;

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

    public void addMember(Participant participant) {
        if (participant != null) {
            members.add(participant);
        }
    }

    public int size() {
        return members.size();
    }

    public String summary() {
        return "Team " + teamNumber + " (" + size() + " members)";
    }

    // === Helper methods for OOP clarity ===
    public double averageSkillLevel() {
        if (members.isEmpty()) return 0;
        return members.stream().mapToInt(Participant::getSkillLevel).average().orElse(0);
    }


}