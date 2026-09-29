import edu.calpoly.provided.Broker;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import java.awt.GridLayout;
import javax.swing.Timer;
import java.awt.Color;

public class MonitorAvailability extends JFrame{
    private static volatile long robotLastReceived = 0;
    private static volatile long gazeLastReceived = 0;
    private static volatile long affectLastReceived = 0;
    private static volatile long lidarLastReceived = 0;

    private JLabel robotLabel;
    private JLabel gazeLabel;
    private JLabel affectLabel;
    private JLabel lidarLabel;

    // constructor to create the GUI
    public MonitorAvailability() {
        setTitle("Monitor Availability");
        setSize(400, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridLayout(4, 2, 10, 10));
        add(new JLabel("Robot"));
        robotLabel = new JLabel("Robot: Not Available", SwingConstants.CENTER);
        add(robotLabel);
        add(new JLabel("Gaze"));
        gazeLabel = new JLabel("Gaze: Not Available", SwingConstants.CENTER);
        add(gazeLabel);
        add(new JLabel("Affect"));
        affectLabel = new JLabel("Affect: Not Available", SwingConstants.CENTER);
        add(affectLabel);
        add(new JLabel("Lidar"));
        lidarLabel = new JLabel("Lidar: Not Available", SwingConstants.CENTER);
        add(lidarLabel);
        Timer statusTimer = new Timer(100, event -> updateStatus());
        statusTimer.start();
    }

    public static void main(String[] args) {
        MonitorAvailability monitor = new MonitorAvailability();
        monitor.setVisible(true);
        Broker broker = new Broker("localhost", 5000);
        while (true) {
            String message = broker.receive();
            String[] parts = message.split(",");
            String source = parts[0];
            long currentTime = System.currentTimeMillis();

            if (source.equals("ROBOT")) {
                robotLastReceived = currentTime;
            }else if(source.equals("GAZE")) {
                gazeLastReceived = currentTime;
            }else if(source.equals("AFFECT")){
                affectLastReceived = currentTime;
            }else if (source.equals("LIDAR")) {
                lidarLastReceived = currentTime;
            }
            System.out.println(source);
        }
    }
    public void updateStatus(){
        long currentTime = System.currentTimeMillis();
        updateLabel(robotLabel, currentTime - robotLastReceived <= 1000);
        updateLabel(gazeLabel, currentTime - gazeLastReceived <= 1000);
        updateLabel(affectLabel, currentTime - affectLastReceived <= 1000);
        updateLabel(lidarLabel, currentTime - lidarLastReceived <= 1000);        
    }
    public void updateLabel(JLabel label, boolean available){
        if (available){
            label.setText("AVAILABLE");
            label.setForeground(Color.GREEN);
        }else {
            label.setText("UNAVAILABLE");
            label.setForeground(Color.RED);
        }

    }

}
