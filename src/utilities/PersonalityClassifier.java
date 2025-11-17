package utilities;

public class PersonalityClassifier {

    //think about moving this to util and make it static
    public static PersonalityType classifyPersonalityType(int score) {
        if (score<0 || score>100){
            return null;
        }

        if (score >=90){
            return PersonalityType.LEADER;
        } else  if (score >=70){
            return PersonalityType.BALANCED;
        } else {
            return PersonalityType.THINKER; //0 -69 band
        }
    }
}

