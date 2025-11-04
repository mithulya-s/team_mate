package cli;

import utilities.PersonalityClassifier;
import utilities.PersonalityType;

import java.util.Scanner;

public class PersonalityScorer {
    public PersonalityScorer(Scanner surveyScanner) {
    }

    public int promptForPersonality() {
        Scanner sc = new Scanner(System.in);
        int score =0;

        System.out.println("Please answer the following questions: ");

        //looping and calcualting the total
        for (int i = 1; i <= 5; i++) {
            System.out.print("Question " + i + ": ");
            int answer = sc.nextInt();
            while (answer <1 || answer > 5) {
                System.out.println("Invalid answer. Please enter a number between 1 and 5");
                answer = sc.nextInt();
            }
            //if the answer is correct, addign to the total
            score += answer;
        }
        //normalizing the score for the final score
        int personalityScore = score*4;


        //Assigning the personality score
        PersonalityType personality = PersonalityClassifier.classifyPersonalityType(personalityScore);

        //no need to return the persoality type since the class returns from the top,so we're returning the score only.
        return personalityScore;


    }
}
