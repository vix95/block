import java.io.*;
import java.util.Scanner;

public class Block {
    private static String images_path = System.getProperty("user.dir") + "/images";
    private static String key_path = System.getProperty("user.dir") + "/key";

    public static void main(String[] args) {
        try {
            String key;

            if (args.length > 0)
                System.out.print("You shouldn't provide any arguments, type: java Block\n");

            key = readKey();
            BlockCipher blockCipher = new BlockCipher(key);
            blockCipher.ecbEncrypt(images_path);
            blockCipher.cbcEncrypt(images_path);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static String readKey() {
        Scanner scanner;
        try {
            File file = new File(key_path + "/key.txt");
            boolean file_exists = file.exists();

            if (file_exists) {
                scanner = new Scanner(file);
                try {
                    String key = scanner.nextLine().toLowerCase();
                    if (!key.equals(key.replaceAll("[^a-zA-Z ]+ ", ""))) {
                        System.out.print("Error: unrecognized key, the key must be a positive number and meet the requirements\n");
                        return null;
                    }

                    System.out.printf("The key has been loaded: %s\n", key);
                    return key;
                } catch (Exception e) {
                    System.out.print("Error: unrecognized key, the key must be a positive number and meet the requirements\n");
                    return null;
                }
            }

            return null;
        } catch (Exception e) {
            System.out.print("Error: key file not found, can't do any action\n");
        }

        return null;
    }
}