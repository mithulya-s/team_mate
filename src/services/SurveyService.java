package services;

import base.Participant;
import csv.ParticipantCsvWriter;

import java.util.Scanner;

public class SurveyService {
    private final Scanner scanner;

    public SurveyService(Scanner scanner) {
        this.scanner = scanner;
    }

    public void initiateSurvey() {
        System.out.println("Welcome to the University Game Club Survey!");
        System.out.println("Please answer the following questions " +
                "to help us assign you to the best team!\n");

        try{
            //Create the participant
            ParticipantCreator builder=new ParticipantCreator(scanner);
            Participant participant=builder.buildParticipant();

            //Try to save it to CSV
            try{
                ParticipantCsvWriter.saveParticipantToCsv(participant);

                //Sucess with writing the participant to the CSV
                System.out.println("\n✅ Survey completed successfully!");
                System.out.println("📋 Your participant ID is: " + participant.getId());
                System.out.println("💡 Please save this ID — you'll need it to view your assigned team later.");
                System.out.println("✅ Response has been securely saved!");
                System.out.println("\nThank you. Have a wonderful day!");

            } catch (Exception csvWriteError){
                //If writing failed but the participant was created.
                System.err.println("\n⚠️ Warning: Survey completed but failed to write to file.");
                System.err.println("Error details: " + csvWriteError.getMessage());
                System.out.println("\n📋 Your participant ID is: " + participant.getId());
                System.out.println("💡 Please save this ID and contact an organizer.");

            }


        } catch (IllegalArgumentException validationError){
            //Participant creation failed due to raised validations
            System.err.println("\n❌ Failed to create participant profile.");
            System.err.println("Error: " + validationError.getMessage());
            System.out.println("Please try filling the survey again.");


        }catch (Exception sysError){
            // Something else with the system went wrong
            System.err.println("\n❌ An unexpected error occurred during the survey.");
            System.err.println("Error details: " + sysError.getMessage());
            System.out.println("Please try again or contact support.");
        }
    }

}
