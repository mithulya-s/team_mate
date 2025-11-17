package base;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Organizer {
    //this class holds the onstances of the organizers with the authentication fot them after they go through the main menu
    private final String fullName;
    private final String username;
    private final String password;

    //to store the organizers, little db
    private static final Map<String,Organizer> AUTHORIZED_ORGANIZERS = new HashMap<>();

    //THE people
    static {
        AUTHORIZED_ORGANIZERS.put("admin",
                new Organizer("System Administrator", "admin1","admin"));
        AUTHORIZED_ORGANIZERS.put("organizer1",
                new Organizer("Organizer", "organizer1","org001"));
        AUTHORIZED_ORGANIZERS.put("organizer2",
                new Organizer("Organizer", "organizer2","org002"));
    }

    private  Organizer(String fullName, String username, String password) {
        this.fullName = fullName;
        this.username = username;
        this.password = password;
    }

    //to authenticate at login
    public static Organizer authenticateOrganizer(Scanner scanner) {
        System.out.println("\n🔐 Organizer Login");
        System.out.println("═══════════════════════════════════════════════════════");
        System.out.println("💡 Demo credentials: username='admin' password='admin123'");
        System.out.println("═══════════════════════════════════════════════════════\n");

        int attemptCounter=0;
        final int ATTEMPT_LIMIT=5;

        while (attemptCounter<ATTEMPT_LIMIT) {
            System.out.print("Username: ");
            String enteredUsername = scanner.nextLine().trim();

            System.out.print("Password: ");
            String enteredPassword = scanner.nextLine().trim();

            if (enteredUsername.isEmpty() || enteredPassword.isEmpty()) {
                System.out.println("❌ Username and password cannot be empty.\n");
                attemptCounter++;
                continue;
            }

            Organizer organizer=validateCreds(enteredUsername,enteredPassword);

            if (organizer != null) {
                System.out.println("\n✅ Login successful!");
                System.out.println("👋 Welcome, " + organizer.fullName + "!\n");
                return organizer;
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

        return null;
    }

    //helper
    private static Organizer validateCreds(String username, String password) {
        Organizer organizer = AUTHORIZED_ORGANIZERS.get(username);

        if (organizer!=null && organizer.password.equals(password)) {
            return organizer;
        }
        return null;
    }

    //getters only since the fields are final
    public String getUsername() {
        return username;
    }
    public String getPassword() {
        return password;
    }


    //to string just for safegurad
    @Override
    public String toString() {
        return "Organizer { Username='" + username + "', name='" + fullName + "'}";

    }
}
