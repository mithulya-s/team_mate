package cli;

import java.util.Scanner;

public class BiodataPrompter {

    //separate function to prompt each section

    // Full name
    //no arg since the caller doesnt provide anything directly, this function finds its own data through
    // an external source in this case the system.in
    public String promptForFullName(){
        Scanner input = new Scanner(System.in);
        System.out.print("Enter your full name: ");
        return input.nextLine().trim();

        //consider more strict formatting

    }

    public String promptForUsername(){
        Scanner input = new Scanner(System.in);
        System.out.print("Enter a username. Must contain no spaces, or numbers: ");
        return input.nextLine().trim();
    }

    //add more constraints
    public String promptForEmail(){
        Scanner input = new Scanner(System.in);
        String email=""; //to get it checked easily

        while (!email.contains("@")|| !email.contains(".")){
            System.out.print("Enter email address associated with university: ");
            email = input.nextLine().trim();

            if (!email.contains("@") || (!email.contains("."))){
                System.out.print("Invalid email format. Please try again: ");
            }
        }
        return email;
    }
}
