package cli;

import utilities.Role;

import java.util.Scanner;

public class RoleSelector {

    public RoleSelector(Scanner surveyScanner) {
    }

    public Role promptForRole(){
        Scanner sc = new Scanner(System.in);
        //to store the final result
        Role chosenRole= null;

        //prompt repeatedly
        while(true){
            System.out.println("-------------------------------------------------------");
            System.out.println("Please select your preferred role: ");
            System.out.println("-------------------------------------------------------");
            System.out.println("1 - Strategist");
            System.out.println("2 - Attacker");
            System.out.println("3 - Defender");
            System.out.println("4 - Supporter");
            System.out.println("5 - Coordinator");
            System.out.println("-------------------------------------------------------");

            int choice = sc.nextInt();
            chosenRole = switch (choice){
                case 1 -> Role.STRATEGIST;
                case 2 -> Role.ATTACKER;
                case 3 -> Role.DEFENDER;
                case 4 -> Role.SUPPORTER;
                case 5 -> Role.COORDINATOR;
                default -> null;
            };

            if(chosenRole == null){
                System.out.println("Invalid choice. Please try again.");
            }else {
                break; //exiting the loop if valid
            }
        }
        return chosenRole;
    }
}
