package edu.calpoly.messages;

import java.util.Scanner;
import edu.calpoly.provided.Encryption;
import org.apache.commons.codec.digest.DigestUtils;

public class EncryptMessage {

   
    public static String messageDigest(String message) {
        return DigestUtils.sha256Hex(message);
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        
        while (true) {
            System.out.print("Enter a message to encrypt (or type 'exit' to quit): ");
            String message = scanner.nextLine();
            
            if (message.equalsIgnoreCase("exit")) {
                System.out.println("Exiting the program.");
                break;
            }

            // Existing AES/GCM encryption
            String encryptedMessage = Encryption.encrypt(message);
            System.out.println("Encrypted message: " + encryptedMessage);
            
            // New SHA-256 Digest
            String digest = messageDigest(message);
            System.out.println("SHA-256: " + digest);
        }
        
        scanner.close();
    }
}
