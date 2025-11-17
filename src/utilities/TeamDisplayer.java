package utilities;

import base.Participant;

import java.util.List;

public class TeamDisplayer {

    public static void displayTeams(List<List<Participant>> formedTeams) {
        if (formedTeams == null || formedTeams.isEmpty()) {
            System.out.println("\n⚠️  No teams to display.");
            return;
        }

        System.out.println("\n📊 Formed Teams:");
        System.out.println("═══════════════════════════════════════════════════════");

        int teamCount = 0;
        for (int i = 0; i < formedTeams.size(); i++) {
            List<Participant> team = formedTeams.get(i);

            if (team == null || team.isEmpty()) {
                continue;
            }

            teamCount++;
            System.out.println("\n🏆 Team " + (i + 1) + " (" + team.size() + " members):");
            System.out.println("───────────────────────────────────────────────────────");

            for (Participant p : team) {
                if (p == null) {
                    continue;
                }

                try {
                    System.out.printf("  • %s | %s | %s | %s (Skill: %d) | %s | Score: %d (%s)%n",
                            p.getId(),
                            p.getFullName(),
                            p.getEmail(),
                            p.getInterest(),
                            p.getSkillLevel(),
                            p.getRole(),
                            p.getPersonalityScore(),
                            p.getPersonalityType()
                    );
                } catch (Exception e) {
                    System.out.println("  • [Error displaying participant: " + e.getMessage() + "]");
                }
            }
        }

        System.out.println("\n═══════════════════════════════════════════════════════");
        System.out.println("Total teams displayed: " + teamCount);
        System.out.println("═══════════════════════════════════════════════════════\n");
    }
}