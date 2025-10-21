package service;

public class PersonalityClassifier {
    int score ; //taken from the scorer, boilerplate for now
    String personalityType;

    public String returnPersonalityType(int score) {
        if (score >= 90 && score <= 100) {
            personalityType = "Leader";
        } else if (score <= 89 && score >= 70) {
            personalityType = "Balanced";
        } else if (score <= 69 && score >= 50) {
            personalityType = "Thinker";
        } else return null; //add the other ones

        return personalityType;
    }


    /*
    public static void main(String[] args) {
        PersonalityClassifier personalityClassifier = new PersonalityClassifier();
        String output = personalityClassifier.returnPersonalityType(90);
        System.out.println(output);
        String output2 = personalityClassifier.returnPersonalityType(100);
        System.out.println(output2);
        String output3 = personalityClassifier.returnPersonalityType(70);
        System.out.println(output3);
        String output4 = personalityClassifier.returnPersonalityType(89);
        System.out.println(output4);
        String output5 = personalityClassifier.returnPersonalityType(50);
        System.out.println(output5);
        String output6 = personalityClassifier.returnPersonalityType(69);
        System.out.println(output6);

    }

     */





}

