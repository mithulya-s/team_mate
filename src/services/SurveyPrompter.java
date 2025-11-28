package services;

import utilities.Interest;
import utilities.PersonalityType;
import utilities.Role;

import java.util.Scanner;

public class SurveyPrompter {
    // has all the prmopter classes together.
    private final Scanner scanner;
    private static final int MIN_EXP_LEVEL=1;
    private static final int MAX_EXP_LEVEL=10;

    //personality variables
    private static final int NUM_OF_QUESTIONS = 5;
    private static final int MAX_Q_SCORE=5;
    private static final int MIN_Q_SCORE=1;

    public SurveyPrompter(Scanner scanner) {
        this.scanner = scanner;
    }


    //Biodata section
    public String promptForFullName(){
        while (true) {
            System.out.print("Enter you full name: ");
            String name = scanner.nextLine().trim();

            // Validation for not empty
            if (name.isEmpty()) {
                System.out.print("Name cannot be empty. Please try again: ");
                continue;
            }

            // For length (just for additional)
            if(name.length() < 2){
                System.out.print("Name must be at least 2 characters long. " +
                        "Please enter your full name: ");
                continue;
            }

            // At least one letter
            if (!name.matches(".*[a-zA-Z].*")) {
                System.out.println("❌ Name must contain at least one letter.");
                continue;
            }

            return name; // good name
        }
    }

    public String promptForEmail() {
        while (true) {
            System.out.print("Enter your university email address: ");
            String email = scanner.nextLine().trim();

            // non - empty
            if (email.isEmpty()) {
                System.out.print("Email cannot be empty. Please try again: ");
                continue;
            }

            //should contain @
            if (!email.contains("@")) {
                System.out.println("Invalid email format. Must contain '@'.");
                continue;
            }

            //must have the dot
            if (!email.contains(".")) {
                System.out.println("Invalid email format. Must contain the domain. (Eg:   '. , .edu')");
                continue;
            }

            //validate the regex to @ place, think about this
            /*
            // Validate: @ comes before .
            int atIndex = email.indexOf("@");
            int dotIndex = email.lastIndexOf(".");
            if (atIndex > dotIndex) {
                System.out.println("❌ Invalid email format. Example: user@university.edu");
                continue;
            }



            // should have length
            / Validate: has characters before @ and after .
            if (atIndex < 1 || dotIndex >= email.length() - 1) {
                System.out.println("❌ Invalid email format. Example: user@university.edu");
                continue;
            }

             */

            return email;
        }

    }


    //Personality section
    public int promptForPersonality() {
        int rawScore =0;

        System.out.println("\n📋 Personality Assessment");
        System.out.println("Please answer the following questions on a value of 1-5:");
        System.out.println("(1 = Strongly Disagree, 5 = Strongly Agree)\n");

        // The questions stored in an array since it's fixed
        String[] questionList={
                "I enjoy taking the lead and guiding others during group activities.",
                "I prefer analyzing situations and coming up with strategic solutions.",
                "I work well with others and enjoy collaborative teamwork.",
                "I am calm under pressure and can help maintain team morale.",
                "I like making quick decisions and adapting in dynamic situations."
        };

        //displaying the questions
        for (int i = 0; i < NUM_OF_QUESTIONS; i++) {
            System.out.println("Question " + (i + 1) + ":" +  questionList[i]);
            int ans= promptForPersonalityAnswer(i+1);
            rawScore+=ans;
        }

        //final score
        int totalScore=(rawScore*4);

        System.out.println("Personality Assessment completed!");
        //System.out.println("Your score: " + totalScore + "/100\n");

        //no need to return the persoality type since the class returns from the top,so we're returning the score only.
        return totalScore;
    }

    //Helper
    private int promptForPersonalityAnswer(int qNum) {
        while (true){
            System.out.print("Enter your answer (1-5): ");
            String answerStr =scanner.nextLine().trim();

            //validations for the entered values
            if (answerStr.isEmpty()){
                System.out.println("Invalid input.Please enter a number between 1-5.");
                continue;
            }

            //trying to get the int to go, to convert the string
            try{
                int ansValue =Integer.parseInt(answerStr);

                //validation
                if (ansValue < MIN_Q_SCORE || ansValue > MAX_Q_SCORE){
                    System.out.println("Invalid input. Please enter a number between 1-5.");
                    continue;
                }
                return ansValue;

            }catch(NumberFormatException e){
                System.out.println("Invalid input. Please enter a number between 1-5.");
            }
        }
    }


    //think about moving this to util and make it static, classifier class
    public PersonalityType classifyPersonalityType(int score) {
        if (score<0 || score>100){
            return null;
        }

        if (score >=90){
            return PersonalityType.LEADER;
        } else  if (score >=70){
            return PersonalityType.BALANCED;
        } else {
            return PersonalityType.THINKER; //0 -69 band
        }
    }






    //Interest
    public Interest promptForInterest() {
        displayInterestMenu();
        while (true) {
            String userInp = scanner.nextLine().trim();

            //vldations
            if (userInp.isEmpty()) {
                System.out.println("Please enter a value between 1 and 6\n");
                continue;
            }

            //change the type
            try {
                int value = Integer.parseInt(userInp);
                Interest selectedInterest = mapValueToInterest(value);

                if (selectedInterest == null) {
                    System.out.println("Invalid choice. Please enter a value between 1 and 6\n");
                }else  {
                    return selectedInterest;
                }

            } catch (NumberFormatException e) {
                System.out.println("Invalid choice. Please enter a value between 1 and 6\n");
            }
        }
    }

    private void displayInterestMenu() {
        System.out.println("-------------------------------------------------------");
        System.out.println("Please enter the number of your preferred interest:    ");
        System.out.println("-------------------------------------------------------");
        System.out.println("1 - FIFA");
        System.out.println("2 - CS:GO");
        System.out.println("3 - Valorant");
        System.out.println("4 - Dota 2");
        System.out.println("5 - Chess");
        System.out.println("6 - Basketball");
        System.out.println("-------------------------------------------------------");


    }

    private Interest mapValueToInterest(int value) {
        return switch (value) {
            case 1 -> Interest.FIFA;
            case 2 -> Interest.CSGO;
            case 3 -> Interest.VALORANT;
            case 4 -> Interest.DOTA2;
            case 5 -> Interest.CHESS;
            case 6 -> Interest.BASKETBALL;
            default -> null;
        };
    }




    //Skill section
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



    //Role section
    public Role promptForRole() {
        while (true) {
            displayRoleMenu();
            String input = scanner.nextLine().trim();

            // validations
            if (input.isEmpty()) {
                System.out.println("Please enter a value between 1 and 5\n");
                continue;
            }

            //convert the int
            try {
                int numericChoice = Integer.parseInt(input);
                Role chosenRole = traceValueToRole(numericChoice);

                if (chosenRole == null) {
                    System.out.println("❌ Invalid choice. Please enter a number between 1 and 5.\n");
                }else{
                    return chosenRole;
                }
            }  catch (NumberFormatException e) {
                System.out.println("Invalid choice. Please enter a number between 1 and 5.\n");
            }
        }
    }

    private void displayRoleMenu() {
        System.out.println("-------------------------------------------------------");
        System.out.println("Please select your preferred role: ");
        System.out.println("-------------------------------------------------------");
        System.out.println("1 - Strategist");
        System.out.println("2 - Attacker");
        System.out.println("3 - Defender");
        System.out.println("4 - Supporter");
        System.out.println("5 - Coordinator");
        System.out.println("-------------------------------------------------------");
        System.out.println("\nEnter your choice (1-5): ");

    }

    private Role traceValueToRole(int numericChoice) {
        return switch (numericChoice){
            case 1 -> Role.STRATEGIST;
            case 2 -> Role.ATTACKER;
            case 3 -> Role.DEFENDER;
            case 4 -> Role.SUPPORTER;
            case 5 -> Role.COORDINATOR;
            default -> null;
        };
    }

}
