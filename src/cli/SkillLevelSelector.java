package cli;

import java.util.Scanner;

public class SkillLevelSelector {

    public int promptSkillLevel(int skillLevel) {
        Scanner sc = new Scanner(System.in);
        skillLevel=0;

        //change this question to be more friendly
//        System.out.print("Enter the skill level: ");
//        skillLevel = sc.nextInt();

        while (skillLevel < 1 || skillLevel > 10) {
            System.out.println("Rate your level of experience. (Between 1 to 10) : ");
            skillLevel = sc.nextInt();

            if (skillLevel < 1 || skillLevel > 10) {
                System.out.println("Invalid input. Please enter a number between 1 and 10: ");
            }
        }
        return skillLevel;

    }
}
