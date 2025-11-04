package services;

import base.Participant;

import java.util.Scanner;

public class SurveyService {
    private final Scanner scanner;

    public SurveyService(Scanner scanner) {
        this.scanner = scanner;
    }

    public void initiateSurvey() {
        System.out.println("Welcome to the UniGameClub Survey!");
        System.out.println("Please answer the following questions " +
                "to help us assign you to the best team!.\n");

        //Create the participant
        ParticipantCreator builder=new ParticipantCreator(scanner);
        Participant participant=builder.buildParticipant();

        //Displaying confirmation
        System.out.println("\nSurvey completed successfully!");
        System.out.println("Your participant ID is: " + participant.getId());
        System.out.println("Please save this ID — you'll need it to view your team or log in later.");
        System.out.println("\nThank you. Have a wonderful day!");


    }
}
