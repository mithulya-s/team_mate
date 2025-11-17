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

    public static Organizer authenticate(String username, String password) {
        Organizer organizer = AUTHORIZED_ORGANIZERS.get(username);

        if (organizer != null && organizer.password.equals(password)) {
            return organizer;
        }
        return null;
    }

    //getters
    public String getFullName() {
        return fullName;
    }
    public String getUsername() {
        return username;
    }



    //to string just for safegurad
    @Override
    public String toString() {
        return "Organizer { Username='" + username + "', name='" + fullName + "'}";

    }
}
