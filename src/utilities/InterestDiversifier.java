package utilities;

import base.Participant;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InterestDiversifier {
    //ensure team diversity
    public static void constraintInterestDiversity(List<List<Participant>> teams){
        for (List<Participant> team:teams){
            Map<Interest,Integer> interestCount = new HashMap<Interest,Integer>();

            for (Participant p:team){
                Interest interest=p.getInterest();
                interestCount.put(interest,interestCount.getOrDefault(interest,0)+1);
            }
            for (Map.Entry<Interest,Integer> entry:interestCount.entrySet()){
                if (entry.getValue()>2){
                    System.out.println("Interest '" + entry.getKey() + "' appears " + entry.getValue() +
                            " times in a team. Consider rebalancing.");
                }
            }
        }
    }
}
