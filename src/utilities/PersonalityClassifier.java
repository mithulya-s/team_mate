package utilities;

public class PersonalityClassifier {

    //think about moving this to util and make it static
    public static PersonalityType classifyPersonalityType(int score) {
        if (score >= 90 && score <= 100) {
            return PersonalityType.LEADER;
        } else if (score <= 89 && score >= 70) {
            return  PersonalityType.BALANCED;
        } else if (score <= 69 && score >= 50) {
            return  PersonalityType.THINKER;
        } else if (score < 50 && score >= 0) {
            return  PersonalityType.SUPPORTER;
        } else {
            return null;
        }

    }
}

