package cli;

import java.util.Scanner;

public class BiodataPrompter {
    private final Scanner scanner;

    public BiodataPrompter(Scanner scanner) {
        this.scanner = scanner; //uses the scanner we passed
    }

    //separate function to prompt each section
    public String promptForFullName(){
        while (true) {
            System.out.print("Enter you full name: ");
            String name = scanner.nextLine().trim();

            // Validation for not empty
            if (name.isEmpty()) {
                System.out.print("Name cannot be empty. Please try again: ");
                continue;
            }

            // For length (just for additional)
            if(name.length() < 2){
                System.out.print("Name must be at least 2 characters long. " +
                        "Please enter your full name: ");
                continue;
            }

            // At least one letter
            if (!name.matches(".*[a-zA-Z].*")) {
                System.out.println("❌ Name must contain at least one letter.");
                continue;
            }

            return name; // good name
        }
    }




    public String promptForEmail() {
        while (true) {
            System.out.print("Enter your university email address: ");
            String email = scanner.nextLine().trim();

            // non - empty
            if (email.isEmpty()) {
                System.out.print("Email cannot be empty. Please try again: ");
                continue;
            }

            //should contain @
            if (!email.contains("@")) {
                System.out.println("Invalid email format. Must contain '@'.");
                continue;
            }

            //must have the dot
            if (!email.contains(".")) {
                System.out.println("Invalid email format. Must contain the domain. (Eg:   '. , .edu')");
                continue;
            }

            //validate the regex to @ place, think about this
            /*
            // Validate: @ comes before .
            int atIndex = email.indexOf("@");
            int dotIndex = email.lastIndexOf(".");
            if (atIndex > dotIndex) {
                System.out.println("❌ Invalid email format. Example: user@university.edu");
                continue;
            }



            // should have length
            / Validate: has characters before @ and after .
            if (atIndex < 1 || dotIndex >= email.length() - 1) {
                System.out.println("❌ Invalid email format. Example: user@university.edu");
                continue;
            }

             */

            return email;
        }

    }
}
