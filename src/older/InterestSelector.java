/*
package older.cli;

import utilities.Interest;

import java.util.Scanner;

public class InterestSelector {
    private final Scanner scanner;

    public InterestSelector(Scanner scanner) {
        this.scanner = scanner;
        //takes in the stuff from the survey class
    }

    // older.cli functions for all the parts of the interest section of the survey

    public Interest promptForInterest() {
        displayInterestMenu();
        while (true) {
            String userInp = scanner.nextLine().trim();

            //vldations
            if (userInp.isEmpty()) {
                System.out.println("Please enter a value between 1 and 6\n");
                continue;
            }

            //change the type
            try {
                int value = Integer.parseInt(userInp);
                Interest selectedInterest = mapValueToInterest(value);

                if (selectedInterest == null) {
                    System.out.println("Invalid choice. Please enter a value between 1 and 6\n");
                }else  {
                    return selectedInterest;
                }

            } catch (NumberFormatException e) {
                System.out.println("Invalid choice. Please enter a value between 1 and 6\n");
            }
        }
    }

    private void displayInterestMenu() {
        System.out.println("-------------------------------------------------------");
        System.out.println("Please enter the number of your preferred interest:    ");
        System.out.println("-------------------------------------------------------");
        System.out.println("1 - FIFA");
        System.out.println("2 - CS:GO");
        System.out.println("3 - Valorant");
        System.out.println("4 - Dota 2");
        System.out.println("5 - Chess");
        System.out.println("6 - Basketball");
        System.out.println("-------------------------------------------------------");


    }

    private Interest mapValueToInterest(int value) {
        return switch (value) {
            case 1 -> Interest.FIFA;
            case 2 -> Interest.CSGO;
            case 3 -> Interest.VALORANT;
            case 4 -> Interest.DOTA2;
            case 5 -> Interest.CHESS;
            case 6 -> Interest.BASKETBALL;
            default -> null;
        };
    }
}

 */

