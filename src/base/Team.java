package base;

import utilities.Interest;
import utilities.Role;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

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
        if (participant != null) members.add(participant);
    }

    public int size() {
        return members.size();
    }


   /*
    public boolean isFull(int teamSize) {
        return size() >= teamSize;
    }

    public double averageSkill() {
        return members.stream().mapToInt(Participant::getSkillLevel).average().orElse(0.0);
    }

    public Set<Role> uniqueRoles() {
        return members.stream().map(Participant::getRole).collect(Collectors.toSet());
    }

    public Set<Interest> uniqueInterests() {
        return members.stream().map(Participant::getInterest).collect(Collectors.toSet());
    }

     */

    @Override
    public String toString() {
        return "Team " + teamNumber + " (" + size() + " members)";
    }
}
