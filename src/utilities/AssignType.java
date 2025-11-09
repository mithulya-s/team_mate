package utilities;

import base.Participant;
import base.Team;

import java.util.Comparator;
import java.util.List;

public class AssignType {

    public static void assignByTpe(List<List<Participant>> teams,
                                    List<Participant> pool, PersonalityType personalityType,
                                    int maxPerTeam) {
        int teamIndex=0;
        for (Participant p : pool) {
            boolean appointed=false;

            for (int i = 0; i < teams.size(); i++) {
                List<Participant> team = teams.get(teamIndex);
                long count=team.stream()
                        .filter(member->member.getPersonalityType()==personalityType)
                        .count();
                if  (count<maxPerTeam && team.size()<maxPerTeam) {
                    team.add(p);
                    appointed=true;
                    break;
                }
                teamIndex=(teamIndex+1)%teams.size();
            }
            if (!appointed) {
                teams.stream()
                        .min(Comparator.comparingInt(List::size))
                        .ifPresent(team->team.add(p));
            }
        }

    }
}
