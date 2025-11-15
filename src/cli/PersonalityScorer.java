package cli;

import java.util.Scanner;

public class PersonalityScorer {
    private final Scanner scanner;
    private static final int NUM_OF_QUESTIONS = 5;
    private static final int MAX_Q_SCORE=5;
    private static final int MIN_Q_SCORE=1;

    public PersonalityScorer(Scanner scanner) {
        this.scanner = scanner;
    }

    public int promptForPersonality() {
        int rawScore =0;

        System.out.println("\n📋 Personality Assessment");
        System.out.println("Please answer the following questions on a value of 1-5:");
        System.out.println("(1 = Strongly Disagree, 5 = Strongly Agree)\n");

        // The questions stored in an array since it's fixed
        String[] questionList={
                "I enjoy taking charge and leading others",
                "I prefer working collaboratively in a team",
                "I analyze situations carefully before acting",
                "I adapt easily to changing circumstances",
                "I support and encourage my teammates"
        };

        //displaying the questions
        for (int i = 0; i < NUM_OF_QUESTIONS; i++) {
            System.out.println("Question " + (i + 1) + ":" +  questionList[i]);
            int ans=promptForAnswer(i+1);
            rawScore+=ans;
        }

        //final score
        int totalScore=(rawScore*4);

        System.out.println("Personality Assessment completed!");
        System.out.println("Your score: " + totalScore + "/100\n");

        //no need to return the persoality type since the class returns from the top,so we're returning the score only.
        return totalScore;
    }

    //Helper
    private int promptForAnswer(int questionNumber) {
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
}
