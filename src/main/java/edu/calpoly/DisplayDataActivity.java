// TASK #73: IMPLEMENT MESSAGE RECEPTION AND CLASSIFICATION
/** 
 * RECIPE:
 * Receives msg through the course-provided Broker Class
 * Then it would identify the data source represneted by each msg
 * Then expected message format will follow:
 *  TYPE, data...
 * ROBOT, GAZE, AFFECT, and LIDAR
*/

// SubTask #73(a): create the course-provided Broker Connection
package edu.calpoly;
import edu.calpoly.provided.Broker;

// SubTasl #73(b): Receive msg from the system

public class DisplayDataActivity {
    public static void main(String[] args) {
        Broker broker = new Broker("localhost", 5000); // Creates a broker to received msg from the local services
        
        

        while (true) {
            // wait for and store
            String msg = broker.receive();


            //SubTask #73(c): Handle invalid formats
            if (msg == null || msg.isBlank()) { // Check if the message is empty, blank, or
                System.out.println("Invalid Message"); // if it does invalid
                continue;
            } 

            int commaPos = msg.indexOf(","); // therefore, check if there is commas

            ////If not, then  find the first comma
            if (commaPos == -1) { // if not then, it would print invalid

                System.out.println("Invalid Format.");
                continue;

            } 

            String msgType = msg.substring(0, commaPos).trim(); // then extract the text with trim() before the comma

            if (msgType.isBlank()) {
                System.out.println("INVALID SOURCE TYPE.");
                continue;
            }

            // SubTask #73(d): Classify the message to each source
            switch (msgType) {
                case "ROBOT":
                    System.out.println("Classified as Robot");
                   
                    break;
                
                case "GAZE":
                    System.out.println("Classified as Gaze");

                    break;

                case "AFFECT":
                    System.out.println("Classified as Affect");

                    break;

                case "LIDAR":
                    System.out.println("Classified as LiDAR");

                    break;
            
                default:
                    System.out.println("Unknown Source Type: " + msgType);

                    break;
            }

            

            // then display the msg
            System.out.println("Received: " + msg); //concadination
        }

        
        
        

    }


}










