package base;

import utilities.Interest;
import utilities.PersonalityType;
import utilities.Role;

public class Participant {
    //consider making all fields private
    private String id;
    private String fullName;
    //private String username;
    private String email;
    private Interest interest;
    private int skillLevel;
    private Role role;
    private int personalityScore;
    private PersonalityType personalityType;



    //Constructor for now
    public Participant(String id, String fullName, String email,
                       Interest interest, int skillLevel, Role role, int personalityScore,
                       PersonalityType personalityType) {
        this.id=id;
        this.fullName=fullName;
        this.email=email;
        this.interest=interest;
        this.skillLevel=skillLevel;
        this.role=role;
        this.personalityScore=personalityScore;
        this.personalityType=personalityType;


    }

    //Getters for now (Setters must decide depending on the change, mostly won't need)
    public String getId() {return id;}
    public String getFullName() {return fullName;}
    public String getEmail() {return email;}
    public Interest getInterest() {return interest;}
    public int getPersonalityScore() {return personalityScore;}
    public PersonalityType getPersonalityType() {return personalityType;}
    public Role getRole() {return role;}
    public int getSkillLevel() {return skillLevel;}

    // Didn't put the stters yet, have to decide on whether to make the fields final or not.





}

