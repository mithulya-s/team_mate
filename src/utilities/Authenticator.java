package utilities;

import base.Organizer;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Authenticator {
    //A single instance of the authenticator class following the singleton pattern
    private static Authenticator authInstance;

    //current session state
    private Organizer currentOrganizer;
    private boolean isAuthenticated;

    //track the attempts
    private static final int ATTEMPT_LIMIT = 5;

    private static final Map<String, Organizer> AUTHORIZED_ORGANIZERS = new HashMap<>();
    static {
        AUTHORIZED_ORGANIZERS.put("admin", new Organizer("System Administrator", "admin", "admin"));
        AUTHORIZED_ORGANIZERS.put("organizer1", new Organizer("Organizer", "organizer1", "org001"));
        AUTHORIZED_ORGANIZERS.put("organizer2", new Organizer("Organizer", "organizer2", "org002"));
    }


    //private constrcutor to protect it from being instanciated
    private Authenticator() {
        this.currentOrganizer = null;
        this.isAuthenticated = false;
    }

    //gets the instance, create of it isnt' there
    public static Authenticator getInstance() {
        if (authInstance == null) {
            authInstance = new Authenticator();
        }
        return authInstance;
    }

    //display
    public boolean login(Scanner scanner) {
        System.out.println("\n🔐 Organizer Login");
        System.out.println("═══════════════════════════════════════════════════════");
        System.out.println("💡 Demo credentials: username='admin' password='admin'");
        System.out.println("═══════════════════════════════════════════════════════\n");

        int attemptCounter = 0;

        while (attemptCounter < ATTEMPT_LIMIT) {
            System.out.print("Username: ");
            String enteredUsername = scanner.nextLine().trim();

            System.out.print("Password: ");
            String enteredPassword = scanner.nextLine().trim();

            if (enteredUsername.isEmpty() || enteredPassword.isEmpty()) {
                System.out.println("❌ Username and password cannot be empty.\n");
                attemptCounter++;
                continue;
            }

            // Delegate to Organizer's authentication method
            Organizer organizer = AUTHORIZED_ORGANIZERS.get(enteredUsername);

            if (organizer != null) {
                // Successful login
                this.currentOrganizer = organizer;
                this.isAuthenticated = true;
                System.out.println("\n✅ Login successful!");
                System.out.println("👋 Welcome, " + organizer.getFullName() + "!\n");
                return true;
            } else {
                attemptCounter++;
                int remaining = ATTEMPT_LIMIT - attemptCounter;

                if (remaining > 0) {
                    System.out.println("❌ Invalid credentials. " + remaining + " attempt(s) remaining.\n");
                } else {
                    System.out.println("❌ Maximum login attempts exceeded. Access denied.\n");
                }
            }
        }

        return false;
    }


    public void logout() {
        if (isAuthenticated) {
            System.out.println("👋 Goodbye, " + currentOrganizer.getFullName() + "!");
        }
        this.currentOrganizer = null;
        this.isAuthenticated = false;
    }


    public boolean isAuthenticated() {
        return isAuthenticated;
    }


    public Organizer getCurrentOrganizer() {
        return currentOrganizer;
    }


    public void requireAuthentication() {
        if (!isAuthenticated) {
            throw new IllegalStateException("Authentication required. Please login first.");
        }
    }

}
