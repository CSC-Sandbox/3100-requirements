package edu.calpoly.messages;

import java.util.Scanner;
import org.apache.commons.codec.digest.DigestUtils;

/**
 * Console entry point for encrypting messages and displaying their SHA-256 digests.
 *
 * @author Anthony Soto
 * @version 1.0
 */
public class EncryptMessage {
    private static final System.Logger LOGGER =
            System.getLogger(EncryptMessage.class.getName());

    // Computes the SHA-256 digest of a message.
    
    public static String messageDigest(String message) {
        return DigestUtils.sha256Hex(message);
    }

   
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        MessageSecurityProcessor securityProcessor =
                new DiagnosticsMessageSecurityDecorator(
                        new EncryptionMessageSecurity(),
                        event -> LOGGER.log(System.Logger.Level.INFO, event));
        
        while (true) {
            System.out.print("Enter a message to encrypt (or type 'exit' to quit): ");
            String message = scanner.nextLine();
            
            if (message.equalsIgnoreCase("exit")) {
                System.out.println("Exiting the program.");
                break;
            }

            String encryptedMessage = securityProcessor.process(message);
            System.out.println("Encrypted message: " + encryptedMessage);
            
            String digest = messageDigest(message);
            System.out.println("SHA-256: " + digest);
        }
        
        scanner.close();
    }
}
