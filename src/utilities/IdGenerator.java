package utilities;

public class IdGenerator {
    //Upgrade to iterate with the CSV later. After the decison of DB has been done

    private static int counter = 1;

    public static String generateId() {
        String id = String.format("P%03d", counter);
        counter++;
        return id;
    }
}
