package utilities;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

/*
 - Handles organizer authentication for secure access to team management features and to stop participants
    from accessing formation functionalities.
 - Implemented as a Singleton to ensure only one authentication state exists during runtime.
 */

public class Authenticator {
    // Singleton instance
    private static Authenticator authInstance;

    //To store the current session state
    private String currentFullName;
    private String currentUsername;
    private boolean isAuthenticated;

    // Login attempt limit
    private static final int ATTEMPT_LIMIT = 5;

    // Authorized organizer credentials (username → [fullName, password])
    private static final Map<String, String[]> AUTHORIZED_ORGANIZERS = new HashMap<>();
    static {
        AUTHORIZED_ORGANIZERS.put("admin", new String[]{"Administrator", "admin"});
    }

    // Private constructor to stop external instantiation
    private Authenticator() {
        this.currentFullName = null;
        this.currentUsername = null;
        this.isAuthenticated = false;
    }

    // Singleton accessor which returns the Singleton instance
    public static Authenticator getInstance() {
        if (authInstance == null) {
            authInstance = new Authenticator();
        }
        return authInstance;
    }

    // Handles login flow for organizers
    //Validates credentials against authorized list and tracks attempts.
    public boolean login(Scanner scanner) {
        System.out.println("\n================================================================================  ");
        System.out.println("                              ORGANIZER AUTHENTICATION                               ");
        System.out.println("================================================================================    ");



        int attemptCounter = 0;

        while (attemptCounter < ATTEMPT_LIMIT) {
            System.out.print("\nUSERNAME : ");
            String enteredUsername = scanner.nextLine().trim();

            System.out.print("PASSWORD : ");
            String enteredPassword = scanner.nextLine().trim();

            if (enteredUsername.isEmpty() || enteredPassword.isEmpty()) {
                System.out.println("Username and password cannot be empty.\n");
                attemptCounter++;
                continue;
            }

            String[] organizerData = AUTHORIZED_ORGANIZERS.get(enteredUsername);

            if (organizerData != null && organizerData[1].equals(enteredPassword)) {
                this.currentUsername = enteredUsername;
                this.currentFullName = organizerData[0];
                this.isAuthenticated = true;
                System.out.println("\n✔️Authentication successful");
                System.out.println("Welcome " + currentFullName + ".\n");
                return true;
            } else {
                attemptCounter++;
                int remaining = ATTEMPT_LIMIT - attemptCounter;

                if (remaining > 0) {
                    System.out.println("Invalid credentials. " + remaining + " attempt(s) remaining.\n");
                } else {
                    System.out.println("Maximum login attempts exceeded. Access denied.\n");
                }
            }
        }
        return false;
    }

    public String getCurrentOrganizerName() {return currentFullName;}
    public String getCurrentOrganizerUsername() {return currentUsername;}

    // State checks
    public boolean isAuthenticated() {return isAuthenticated;}
}