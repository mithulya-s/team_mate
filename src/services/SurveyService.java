package services;

import base.Participant;
import csv.ParticipantCsvWriter;
import utilities.IdGenerator;
import utilities.Interest;
import utilities.PersonalityType;
import utilities.Role;

import java.util.Scanner;


/*
 This class:
    - Handles the survey process for participants.
    - Guides the user through survey prompts.
    - Validates and constructs a Participant object.
 - This class acts as a controller, delegating input collection to SurveyPrompter and storage to ParticipantCsvWriter.
 */

public class SurveyService {
    private final Scanner scanner;

    public SurveyService(Scanner scanner) {
        this.scanner = scanner;
    }


    /*
     This orchestrates the full survey flow:
        - Unique ID generation
        - Prompts the user for survey inputs.
        - Builds the validated Participant object.
        - Saves the participant to CSV.
        - Gives user-friendly feedback and error handling.
     */

    public void initiateSurvey() {
        System.out.println("\nWelcome to the University gaming club Survey!");
        System.out.println("Please answer the following questions to help us assign you to the best team.\n");

        try{
            //Generate unique ID
            String participantId = IdGenerator.getInstance().generateNextId();

            // Collect survey inputs via the helper class methods
            SurveyPrompter prompter = new SurveyPrompter(scanner);
            String enteredFullName =prompter.promptForFullName();
            String enteredEmail =prompter.promptForEmail();
            int score=prompter.promptForPersonality();
            PersonalityType personalityType= prompter.classifyPersonalityType(score);
            Interest selectedInterest = prompter.promptForInterest();
            int selectedSkillLevel =prompter.promptForSkillLevel();
            Role selectedRole=prompter.promptForRole();


            //Build the participant object, with the constructor enforcing validation
            Participant participant= new Participant(
                    participantId,
                    enteredFullName,
                    enteredEmail,
                    selectedInterest,
                    selectedSkillLevel,
                    selectedRole,
                    score,
                    personalityType
            );


            //Attempt to write the participant data to csv
            try{
                ParticipantCsvWriter writer = new ParticipantCsvWriter();
                writer.saveParticipantToCsv(participant);


                //Feedback for success
                System.out.println("\n✔️ Survey completed successfully!");
                System.out.println("✔️ Response has been securely saved.");
                System.out.println("📋 Your participant ID is : " + participant.getId());
                System.out.println("💡 Please save this ID — you'll need it to view your assigned team later.");

                System.out.println("⭐ Thank you!");

            } catch (Exception csvWriteError){
                //Participant was created but writing failed.
                System.out.println("\n⚠️ Warning: Survey completed but failed to write to file.");
                System.out.println("\n📋 Your participant ID is: " + participant.getId());
                System.out.println("❕Please save this ID and contact an administrator.");

            }


        } catch (IllegalArgumentException validationError){
            //Participant creation failed due to invalid inputs
            System.out.println("\nFailed to create participant profile.");
            System.out.println("Please try filling the survey again.");


        }catch (Exception sysError){
            // Unexpected sys errors
            System.out.println("\nAn unexpected error occurred during the survey.");
            System.out.println("Please try again or contact support.");
        }
    }

}
