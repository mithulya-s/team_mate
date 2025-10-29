package services;

import java.util.Scanner;

public class InterestClassifier {
    //dummy var for now, so that it will be replaced later, with the participant attribute
    String interest;


    public void chooseInterest(String role) { //void since it only changes attribute, not show anything
        Scanner sc = new Scanner(System.in);
        System.out.println("-------------------------------------------------------");
        System.out.println("Please enter the number of your preferred userInp: ");
        System.out.println("-------------------------------------------------------");
        System.out.print("1 - FIFA");
        System.out.print("2 - CS : GO");
        System.out.print("3 - Valorant");
        System.out.print("4 - Dota");
        System.out.print("5 - Chess");
        System.out.print("6 - Basketball");
        System.out.println("-------------------------------------------------------");

        int userInp = sc.nextInt();
        if (userInp == 1) {
            this.interest = "FIFA";
        } else if (userInp == 2) {
            this.interest = "CS : GO";
        } else if (userInp == 3) {
            this.interest = "Valorant";
        } else if (userInp == 4) {
            this.interest = "Dota";
        } else if (userInp == 5) {
            this.interest = "Chess";
        } else if (userInp == 6) {
            this.interest = "Basketball";
        } else {
            System.out.println("Invalid Input");
        }
    }

}
