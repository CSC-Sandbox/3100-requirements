import java.util.Scanner;
import edu.calpoly.provided.Encryption;

public class EncryptMessage {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        
        while (true) {
            System.out.print("Enter a message to encrypt (or type 'exit' to quit): ");
            String message = scanner.nextLine();
            
            if (message.equalsIgnoreCase("exit")) {
                System.out.println("Exiting the program.");
                break;
            }

            String encryptedMessage = Encryption.encrypt(message);
            System.out.println("Encrypted message: " + encryptedMessage);
            
            
        }
        scanner.close();
    }
}
