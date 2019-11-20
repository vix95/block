import java.io.*;
import java.util.Scanner;

public class Block {
    private static String path = System.getProperty("user.dir") + "/test_files";

    public static void main(String[] args) {
        try {
            String key;

            if (args.length > 0)
                System.out.print("You shouldn't provide any arguments, type: java Block\n");
            key = readKey();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static String readKey() {
        Scanner scanner;
        try {
            scanner = new Scanner(new File(path + "/key.txt"));
            try {
                String key = scanner.nextLine().toLowerCase();
                if (!key.equals(key.replaceAll("[^a-zA-Z ]+ ", ""))) {
                    System.out.print("Error: unrecognized key, the key must be a positive number and meet the requirements\n");
                    return null;
                }

                System.out.printf("The key has been loaded: %s\n\n", key);
                return key;
            } catch (Exception e) {
                System.out.print("Error: unrecognized key, the key must be a positive number and meet the requirements\n");
                return null;
            }
        } catch (Exception e) {
            System.out.print("Error: key file not found, can't do any action\n");
        }

        return null;
    }
}