package services;

import base.Participant;
import cli.*;
import utilities.*;

import java.util.Scanner;

public class ParticipantCreator {
    //this class orchestrates the prompters and build the participant and sends it back to the survey to
    // give confirmation about the participant

    private final Scanner surveyScanner;

    public ParticipantCreator(Scanner surveyScanner) {
        this.surveyScanner = surveyScanner;
    }

    //the method that wires the specialized prompters and returns the completed participant object
    // added validations to handles issues with the prompters as well.
    public Participant buildParticipant() {
        try{

            //Generate the automatic participantId and store it
            String participantId = IdGenerator.generateId();


            BiodataPrompter biodataPrompter = new BiodataPrompter(surveyScanner);
            //Variables to catch what the prompter returns
            String enteredFullName =biodataPrompter.promptForFullName();
            //String username=biodataPrompter.promptForUsername(); //rethink about this
            String enteredEmail =biodataPrompter.promptForEmail();


            PersonalityScorer personalityScorer = new PersonalityScorer(surveyScanner);
            //Variable to store the score.
            int score=personalityScorer.promptForPersonality();


            //Calling the personality classifier, by sending the score we calculated earlier
            PersonalityType personalityType= PersonalityClassifier.classifyPersonalityType(score);



            //out of range thing happened (IF), but think of removing this.
            // its messy also
            if (personalityType == null) {
                System.err.println("⚠️ Warning: Personality score " + score + " is outside normal range.");
                System.out.println("Defaulting to BALANCED personality type.");
                personalityType = PersonalityType.BALANCED;
            }


            //Interest section
            InterestSelector  interestSelector = new InterestSelector(surveyScanner);
            //stroing the selectedInterest
            Interest selectedInterest = interestSelector.promptForInterest();


            //Prompting for skill level
            SkillLevelSelector levelSelector = new SkillLevelSelector(surveyScanner);
            //Stroing the skill level
            int selectedSkillLevel =levelSelector.promptForSkillLevel();


            //Role selector
            RoleSelector  roleSelector = new RoleSelector(surveyScanner);
            //store the selected role
            Role selectedRole=roleSelector.promptForRole();



            //Try to build the new participant
            try {
                return new Participant(participantId, enteredFullName, enteredEmail, selectedInterest,
                        selectedSkillLevel, selectedRole, score, personalityType);
            } catch (IllegalArgumentException e) {
                //Participant constructor failed
                System.err.println("\n❌ Participant validation failed:");
                System.err.println("   " + e.getMessage());
                throw e; // Re-throw to be handled by SurveyService
            }

        //catches the process errrors not the building errors.
        } catch (IllegalArgumentException e) {
            throw e; //this will be caught again
        } catch (RuntimeException e) {
            //catch all the other things that happens
            System.err.println("\n❌ An error occurred while building participant:");
            System.err.println("   " + e.getMessage());
            throw new RuntimeException("Failed to build participant: " + e.getMessage(), e);
        }
    }
}
