/*
package utilities;

import base.Participant;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MetricCalculator {
    private static void displayTeamStatistics(List<List<Participant>> teams) {
        System.out.println("\n📊 Team Formation Statistics:");
        System.out.println("═══════════════════════════════════════════════════════");

        for (int i = 0; i < teams.size(); i++) {
            List<Participant> team = teams.get(i);
            System.out.println("\nTeam " + (i + 1) + " (" + team.size() + " members):");

            Map<PersonalityType, Long> personalityCounts = new HashMap<>();
            Map<Interest, Long> interestCounts = new HashMap<>();
            Map<Role, Long> roleCounts = new HashMap<>();
            double avgSkill = team.stream().mapToInt(Participant::getSkillLevel).average().orElse(0);

            for (Participant p : team) {
                personalityCounts.merge(p.getPersonalityType(), 1L, Long::sum);
                interestCounts.merge(p.getInterest(), 1L, Long::sum);
                roleCounts.merge(p.getRole(), 1L, Long::sum);
            }

            System.out.println("  Personalities: " + personalityCounts);
            System.out.println("  Interests: " + interestCounts);
            System.out.println("  Roles: " + roleCounts);
            System.out.printf("  Avg Skill: %.1f\n", avgSkill);
        }

        System.out.println("═══════════════════════════════════════════════════════\n");
    }
}

 */
