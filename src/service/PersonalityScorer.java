package service;

public class PersonalityScorer {
    int score;
    int qOne;
    int qTwo;
    int qThree;
    int qFour;
    int qnFive;

    public int calculateScore(int qOne, int qTwo, int qThree, int qFour, int qFive) {
        int initialScore= qOne + qTwo + qThree+qFour+qFive;
        return initialScore*4;
    }

    public static void   main(String[] args){
        PersonalityScorer ps = new PersonalityScorer();
        System.out.println(ps.calculateScore(1,1,1,1,1));

    }



}
