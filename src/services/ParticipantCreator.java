package services;

import base.Participant;
import cli.*;
import utilities.*;

import java.util.Scanner;

public class ParticipantCreator {
    //this class wires all the componenets and stores what the user gave into a participant objec

    private final Scanner surveyScanner;
    public ParticipantCreator(Scanner surveyScanner) {
        this.surveyScanner = surveyScanner;
    }

    //the method that wires the specialized prompters and returns the completed particiapnt object
    public Participant buildParticipant() {

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

        //Build the new participant
        return new Participant(participantId, enteredFullName, enteredEmail,
                                selectedInterest, selectedSkillLevel,selectedRole,score,personalityType);



    }
}
