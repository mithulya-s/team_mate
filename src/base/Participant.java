package base;

import utilities.Interest;
import utilities.PersonalityType;
import utilities.Role;


public class Participant {
    //Represents a participant in the forming system, this is the template for creating a participant.

    private final String id;
    private final String fullName;
    private final String email;
    private final Interest interest;
    private final int skillLevel;
    private final Role role;
    private final int personalityScore;
    private final PersonalityType personalityType;



    //Constructor to keep data integrity by validations as well.
    public Participant(String id, String fullName, String email,
                       Interest interest, int skillLevel, Role role, int personalityScore,
                       PersonalityType personalityType) {

        //Validations to keep invalid data out
        validateId(id);
        validateFullName(fullName);
        validateEmail(email);
        validateSkillLevel(skillLevel);
        validatePersonalityScore(personalityScore);

        if (interest == null) {
            throw new IllegalArgumentException("Interest cannot be null");
        }
        if (role == null) {
            throw new IllegalArgumentException("Role cannot be null");
        }
        if (personalityType == null) {
            throw new IllegalArgumentException("Personality type cannot be null");
        }

        this.id=id;
        this.fullName=fullName;
        this.email=email;
        this.interest=interest;
        this.skillLevel=skillLevel;
        this.role=role;
        this.personalityScore=personalityScore;
        this.personalityType=personalityType;

    }

    //Getters only, no setters since the fields are immutable.
    public String getId() {return id;}
    public String getFullName() {return fullName;}
    public String getEmail() {return email;}
    public Interest getInterest() {return interest;}
    public int getPersonalityScore() {return personalityScore;}
    public PersonalityType getPersonalityType() {return personalityType;}
    public Role getRole() {return role;}
    public int getSkillLevel() {return skillLevel;}


    // Helpers to validate
    private void validateId(String id) {
        if (id == null || id.trim().isEmpty()) {
            throw new IllegalArgumentException("ID cannot be null or empty");
        }
    }
    private void validateFullName(String fullName) {
        if (fullName == null || fullName.trim().isEmpty()) {
            throw new IllegalArgumentException("Full name cannot be null or empty");
        }
    }
    private void validateEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            throw new IllegalArgumentException("Email cannot be null or empty");
        }
        if (!email.contains("@") || !email.contains(".")) {
            throw new IllegalArgumentException("Invalid email format: " + email);
        }
    }
    private void validateSkillLevel(int skillLevel) {
        if (skillLevel < 1 || skillLevel > 10) {
            throw new IllegalArgumentException("Skill level must be between 1 and 10. Received: " + skillLevel);

        }
    }
    private void validatePersonalityScore(int personalityScore) {
        if (personalityScore < 0 || personalityScore > 100) {
            throw new IllegalArgumentException(
                    "Personality score must be between 0 and 100. Received: " + personalityScore);
        }
    }


    //Method override s
    @Override
    public String toString() {
        return String.format("Participant{ID='%s', Name='%s', Interest=%s, Skill Level=%s, Role=%s, Personality Type=%s}",
                id, fullName, interest,skillLevel, role, personalityType);
    }

    /*
    // For participant console output. and to print in places where its created.
    public String displayParticipantString(){
        return String.format("%s - %s (%s, Skill: %d, %s)",
                id, fullName, interest, skillLevel, personalityType);
    }

     */






}

