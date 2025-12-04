package services;

import utilities.Interest;
import utilities.PersonalityType;
import utilities.Role;

import java.util.Scanner;

/*
 - Handles all user prompts during the survey process.
 - Has methods which collects and validates biodata, personality assessment, interest,skill level and role.
 - Keeps survey input logic separate from SurveyService, to improve modularity and readability.
 */

public class SurveyPrompter {
    private final Scanner scanner;

    //Skill level boundaries
    private static final int MIN_EXP_LEVEL=1;
    private static final int MAX_EXP_LEVEL=10;

    //Personality assessment constants
    private static final int NUM_OF_QUESTIONS = 5;
    private static final int MAX_Q_SCORE=5;
    private static final int MIN_Q_SCORE=1;

    public SurveyPrompter(Scanner scanner) {
        this.scanner = scanner;
    }


    //Biodata section

    //Prompts the user for their full name with validation.
    public String promptForFullName(){
        while (true) {
            System.out.print("Enter you full name: ");
            String name = scanner.nextLine().trim();

            if (name.isEmpty()) {
                System.out.print("Name cannot be empty.\n");
                continue;
            }


            if(name.length() < 2){
                System.out.print("Name must be at least 2 characters long.\n");
                continue;
            }


            if (!name.matches(".*[a-zA-Z].*")) {
                System.out.println("Name must contain at least one letter.\n");
                continue;
            }

            if (!name.matches("[a-zA-Z\\s.'-]+")) {
                System.out.println("Name can only contain letters, spaces, apostrophes ('), hyphens (-), or dots (.)\n");
                continue;
            }


            return name;
        }
    }

    //Prompts the user for their email address with validation.
    public String promptForEmail() {
        while (true) {
            System.out.print("Enter your university email address: ");
            String email = scanner.nextLine().trim();


            if (email.isEmpty()) {
                System.out.print("Email cannot be empty. Please try again: \n");
                continue;
            }


            if (!email.contains("@")) {
                System.out.println("Invalid email format. Must contain '@'. Please try again: \n");
                continue;
            }


            if (!email.contains(".")) {
                System.out.println("Invalid email format. Must contain the domain. (Eg:   '. , .edu'). Please try again: \n");
                continue;
            }

            return email;
        }

    }


    //Personality section

    //Conducts the personality assessment using fixed 5 questions.
    public int promptForPersonality() {
        int rawScore =0;

        System.out.println("\n======================================================================");
        System.out.println("                    PERSONALITY ASSESSMENT                              ");
        System.out.println("======================================================================  ");
        System.out.println("Please answer the following questions on a value of 1-5\n");
        System.out.println("(1 = Strongly Disagree, 5 = Strongly Agree)\n");


        String[] questionList={
                "\nI enjoy taking the lead and guiding others during group activities.",
                "\nI prefer analyzing situations and coming up with strategic solutions.",
                "\nI work well with others and enjoy collaborative teamwork.",
                "\nI am calm under pressure and can help maintain team morale.",
                "\nI like making quick decisions and adapting in dynamic situations."
        };


        for (int i = 0; i < NUM_OF_QUESTIONS; i++) {
            System.out.println("\nQuestion " + (i + 1) + ":" +  questionList[i]);
            int ans= promptForPersonalityAnswer(i+1);
            rawScore+=ans;
        }


        int totalScore=(rawScore*4);

        System.out.println("\nPersonality Assessment completed!");

        return totalScore;
    }

    //Helper for personality answers (validates numeric input)
    private int promptForPersonalityAnswer(int qNum) {
        while (true){
            System.out.print("Enter your answer (1-5): ");
            String answerStr =scanner.nextLine().trim();


            if (answerStr.isEmpty()){
                System.out.println("Invalid input.Please enter a number between 1-5:\n");
                continue;
            }


            try{
                int ansValue =Integer.parseInt(answerStr);


                if (ansValue < MIN_Q_SCORE || ansValue > MAX_Q_SCORE){
                    System.out.println("Invalid input. Please enter a number between 1-5:\n ");
                    continue;
                }
                return ansValue;

            }catch(NumberFormatException e){
                System.out.println("Invalid input. Please enter a number between 1-5:\n ");
            }
        }
    }


    //Classifies personality type based on score bands.
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


    //Interest section

    //Gives the user to select a gaming interest from the list.
    public Interest promptForInterest() {

        System.out.println("\n======================================================================");
        System.out.println("                        INTEREST SELECTION                              ");
        System.out.println("======================================================================  ");
        System.out.println(" 1  : FIFA                                                              ");
        System.out.println(" 2  : CS:GO                                                             ");
        System.out.println(" 3  : Valorant                                                          ");
        System.out.println(" 4  : Dota 2                                                            ");
        System.out.println(" 5  : Chess                                                             ");
        System.out.println(" 6  : Basketball                                                        ");
        System.out.println("======================================================================  ");
        System.out.println("Choose your preferred interest: ");

        while (true) {
            String userInp = scanner.nextLine().trim();


            if (userInp.isEmpty()) {
                System.out.println("Please enter a value between 1 and 6: \n");
                continue;
            }


            try {
                int value = Integer.parseInt(userInp);
                Interest selectedInterest = mapValueToInterest(value);

                if (selectedInterest == null) {
                    System.out.println("Invalid choice. Please enter a value between 1 and 6: \n");
                }else  {
                    return selectedInterest;
                }

            } catch (NumberFormatException e) {
                System.out.println("Invalid choice. Please enter a value between 1 and 6: \n");
            }
        }
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

    //Prompts the user to rate their skill level
    public int promptForSkillLevel() {
        System.out.println("\n======================================================================");
        System.out.println("                       SKILL LEVEL ASSESSMENT                           ");
        System.out.println("======================================================================  ");
        System.out.println("Rate your experience level:                                             ");
        System.out.println("  1-3   : Beginner                                                      ");
        System.out.println("  4-6   : Intermediate                                                  ");
        System.out.println("  7-8   : Advanced                                                      ");
        System.out.println("  9-10  : Expert                                                        ");
        System.out.println("======================================================================  ");

        while(true) {
            System.out.print("Enter an experience level (1-10) : ");
            String userInp = scanner.nextLine().trim();


            if (userInp.isEmpty()) {
                System.out.println("Please enter a value between 1 and 10:\n ");
                continue;
            }


            try{
                int skillLevel = Integer.parseInt(userInp);
                if (skillLevel < MIN_EXP_LEVEL || skillLevel > MAX_EXP_LEVEL) {
                    System.out.println("Invalid range. Please enter a value between 1 and 10:\n ");
                    continue;
                }


                String band=classifySkillLevel(skillLevel);
                System.out.println("Skill Level: "+skillLevel+" ("+band+")\n");

                return skillLevel;

            }catch (NumberFormatException e){
                System.out.println("Invalid Input. Please enter a value between 1 and 10:\n ");
            }
        }
    }

    //helper to classify value into bands
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

    //Prompts the user to select their preferred team role.
    public Role promptForRole() {
        System.out.println("\n======================================================================");
        System.out.println("                            ROLE SELECTION                              ");
        System.out.println("======================================================================  ");
        System.out.println("Select your preferred role: ");
        System.out.println(" 1 : Strategist");
        System.out.println(" 2 : Attacker");
        System.out.println(" 3 : Defender");
        System.out.println(" 4 : Supporter");
        System.out.println(" 5 : Coordinator");
        System.out.println("======================================================================  ");
        System.out.println("Enter your choice (1-5): ");
        while (true) {

            String input = scanner.nextLine().trim();

            if (input.isEmpty()) {
                System.out.println("\nPlease enter a value between 1 and 5: ");
                continue;
            }

            try {
                int numericChoice = Integer.parseInt(input);
                Role chosenRole = traceValueToRole(numericChoice);

                if (chosenRole == null) {
                    System.out.println("Invalid choice. Please enter a number between 1 and 5:\n");
                }else{
                    return chosenRole;
                }
            }  catch (NumberFormatException e) {
                System.out.println("Invalid choice. Please enter a number between 1 and 5:\n");
            }
        }
    }

    // To map the numeric choice for the Role enum
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
