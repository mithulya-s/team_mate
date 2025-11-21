package base;

public class Organizer {
    private final String fullName;
    private final String username;
    private final String password;

    public Organizer(String fullName, String username, String password) {
        this.fullName = fullName;
        this.username = username;
        this.password = password;
    }

    public String getFullName() { return fullName; }
    public String getUsername() { return username; }
    //public String getPassword() { return password; }

    @Override
    public String toString() {
        return "Organizer { Username='" + username + "', name='" + fullName + "'}";
    }
}