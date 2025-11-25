/*
package older.cli;

import java.util.Scanner;

public class SkillLevelSelector {
    private final Scanner scanner;
    private static final int MIN_EXP_LEVEL=1;
    private static final int MAX_EXP_LEVEL=10;

    public SkillLevelSelector(Scanner scanner) {
        this.scanner = scanner;
    }

    public int promptForSkillLevel() {
        System.out.println("=======================================================");
        System.out.println("              SKILL LEVEL ASSESSMENT                   ");
        System.out.println("=======================================================");
        System.out.println("Rate your experience level:");
        System.out.println("  1-3   : Beginner");
        System.out.println("  4-6   : Intermediate");
        System.out.println("  7-8   : Advanced");
        System.out.println("  9-10  : Expert");
        System.out.println("=======================================================");

        while(true) {
            System.out.print("Enter an experience level (1-10) : ");
            String userInp = scanner.nextLine().trim();

            //validations
            if (userInp.isEmpty()) {
                System.out.println("Please enter a value between 1 and 10.");
                continue;
            }

            //trying to parse the int
            try{
                int skillLevel = Integer.parseInt(userInp);
                if (skillLevel < MIN_EXP_LEVEL || skillLevel > MAX_EXP_LEVEL) {
                    System.out.println("Invalid range. Please enter a value between 1 and 10.");
                    continue;
                }

                // show confirmation
                String band=classifySkillLevel(skillLevel);
                System.out.println("Skill Level: "+skillLevel+" ("+band+")\n");

                return skillLevel;

            }catch (NumberFormatException e){
                System.out.println("Invalid Input. Please enter a value between 1 and 10.");
            }
        }
    }

    //helper to map the val to category
    private String classifySkillLevel(int skillLevel) {
        if (skillLevel<=3){
            return "Beginner";
        }else if (skillLevel<=6){
            return "Intermediate";
        }else if (skillLevel<=8){
            return "Advanced";
        }else
            return "Expert";
    }
}

 */
