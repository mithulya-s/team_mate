package utilities;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Authenticator {
    // Singleton instance
    private static Authenticator authInstance;

    // Current session state
    private String currentFullName;
    private String currentUsername;
    private boolean isAuthenticated;

    // Track the attempts
    private static final int ATTEMPT_LIMIT = 5;

    // Authorized organizers (username → [fullName,password])
    private static final Map<String, String[]> AUTHORIZED_ORGANIZERS = new HashMap<>();
    static {
        AUTHORIZED_ORGANIZERS.put("admin", new String[]{"Administrator", "admin"});
    }

    // Private constructor (singleton)
    private Authenticator() {
        this.currentFullName = null;
        this.currentUsername = null;
        this.isAuthenticated = false;
    }

    // Singleton accessor
    public static Authenticator getInstance() {
        if (authInstance == null) {
            authInstance = new Authenticator();
        }
        return authInstance;
    }

    // Login flow
    public boolean login(Scanner scanner) {
        System.out.println("═══════════════════════════════════════════════════════\n");
        System.out.println("\n🔐 Organizer Login");
        System.out.println("═══════════════════════════════════════════════════════");



        int attemptCounter = 0;

        while (attemptCounter < ATTEMPT_LIMIT) {
            System.out.print("Username : ");
            String enteredUsername = scanner.nextLine().trim();

            System.out.print("\nPassword : ");
            String enteredPassword = scanner.nextLine().trim();

            if (enteredUsername.isEmpty() || enteredPassword.isEmpty()) {
                System.out.println("Username and password cannot be empty\n");
                attemptCounter++;
                continue;
            }

            String[] organizerData = AUTHORIZED_ORGANIZERS.get(enteredUsername);

            if (organizerData != null && organizerData[1].equals(enteredPassword)) {
                this.currentUsername = enteredUsername;
                this.currentFullName = organizerData[0];
                this.isAuthenticated = true;
                System.out.println("\n ✔️ Authentication successful!");
                System.out.println(" Welcome, " + currentFullName + ".\n");
                return true;
            } else {
                attemptCounter++;
                int remaining = ATTEMPT_LIMIT - attemptCounter;

                if (remaining > 0) {
                    System.out.println(" ❌ Invalid credentials. " + remaining + " attempt(s) remaining.\n");
                } else {
                    System.out.println(" ❌ Maximum login attempts exceeded. Access denied.\n");
                }
            }
        }

        return false;
    }



    // State checks
    public boolean isAuthenticated() {
        return isAuthenticated;
    }

    /*

    // Logout
    public void logout() {
        if (isAuthenticated) {
            System.out.println("👋 Goodbye, " + currentFullName + "!");
        }
        this.currentFullName = null;
        this.currentUsername = null;
        this.isAuthenticated = false;
    }
    public String getCurrentOrganizerName() {
        return currentFullName;
    }

    public String getCurrentOrganizerUsername() {
        return currentUsername;
    }

    public void requireAuthentication() {
        if (!isAuthenticated) {
            throw new IllegalStateException("Authentication required. Please login first.");
        }
    }

     */
}