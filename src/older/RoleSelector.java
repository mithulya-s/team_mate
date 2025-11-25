/*
package older.cli;

import utilities.Role;

import java.util.Scanner;

public class RoleSelector {
    private final Scanner scanner;

    public RoleSelector(Scanner scanner) {
        this.scanner = scanner;
    }

    public Role promptForRole() {
        while (true) {
            displayRoleMenu();
            String input = scanner.nextLine().trim();

            // validations
            if (input.isEmpty()) {
                System.out.println("Please enter a value between 1 and 5\n");
                continue;
            }

            //convert the int
            try {
                int numericChoice = Integer.parseInt(input);
                Role chosenRole = traceValueToRole(numericChoice);

                if (chosenRole == null) {
                    System.out.println("❌ Invalid choice. Please enter a number between 1 and 5.\n");
                }else{
                    return chosenRole;
                }
            }  catch (NumberFormatException e) {
                System.out.println("Invalid choice. Please enter a number between 1 and 5.\n");
            }
        }
    }

    private void displayRoleMenu() {
        System.out.println("-------------------------------------------------------");
        System.out.println("Please select your preferred role: ");
        System.out.println("-------------------------------------------------------");
        System.out.println("1 - Strategist");
        System.out.println("2 - Attacker");
        System.out.println("3 - Defender");
        System.out.println("4 - Supporter");
        System.out.println("5 - Coordinator");
        System.out.println("-------------------------------------------------------");
        System.out.println("\nEnter your choice (1-5): ");

    }

    private Role traceValueToRole(int numericChoice) {
        return switch (numericChoice){
            case 1 -> Role.STRATEGIST;
            case 2 -> Role.ATTACKER;
            case 3 -> Role.DEFENDER;
            case 4 -> Role.SUPPORTER;
            case 5 -> Role.COORDINATOR;
            default -> null;
        };
    }

}

 */
