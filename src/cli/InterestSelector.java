package cli;

import utilities.Interest;

import java.util.Scanner;

public class InterestSelector {
    // cli functions for all the parts of the interest section of the survey

    public Interest promptInterest() {
        Scanner sc = new Scanner(System.in);
        Interest selectedInterest = null;

        while (selectedInterest == null) {
            System.out.println("-------------------------------------------------------");
            System.out.println("Please enter the number of your preferred interest:    ");
            System.out.println("-------------------------------------------------------");
            System.out.println("1 - FIFA");
            System.out.println("2 - CS:GO");
            System.out.println("3 - Valorant");
            System.out.println("4 - Dota");
            System.out.println("5 - Chess");
            System.out.println("6 - Basketball");
            System.out.println("-------------------------------------------------------");

            int userInp = sc.nextInt();
            selectedInterest = switch (userInp) {
                case 1 -> Interest.FIFA;
                case 2 -> Interest.CSGO;
                case 3 -> Interest.VALORANT;
                case 4 -> Interest.DOTA;
                case 5 -> Interest.CHESS;
                case 6 -> Interest.BASKETBALL;
                default -> null;
            };

            if (selectedInterest == null) {
                System.out.println("Invalid input. Please try again.");
            }
        }

        return selectedInterest;
    }
}
