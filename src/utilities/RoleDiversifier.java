package utilities;

import base.Participant;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class RoleDiversifier {

    //ensurign role diversity
    public static void enforceRoleDiversity(List<List<Participant>> teams, int teamSize){
        if (teamSize<=5){
            return;
        }
        for  (List<Participant> team : teams){
            Set<Role> roles = new HashSet<Role>();
            for (Participant p : team){
                roles.add(p.getRole());
            }
            if (roles.size()<3){
                System.out.println("Team has a limited role diversity: "+roles);

            }
        }

    }
}
