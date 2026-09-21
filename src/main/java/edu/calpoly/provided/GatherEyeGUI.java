package edu.calpoly.provided;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JLabel;
import java.awt.Component;

import java.awt.Color;
import java.awt.Font;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class GatherEyeGUI {

	private GatherEye receiver;

	private int WINDOW_SIZE_X = 1280;
	private int WINDOW_SIZE_Y = 720;
	private int BORDER_BUFFER = 10;
	private int CLICKER_PANEL_SIZE = WINDOW_SIZE_Y - BORDER_BUFFER * 8;

	private Font WINDOW_FONT =  new Font("Monospaced", Font.PLAIN, 15);

	private String WINDOW_TITLE = "GatherEye - Click to Generate Gaze Data";

	private String GAZE_POSITION = "<html>Gaze Position:<br>X:<br>Y:</html>";
	private String BROKER_STATUS = "Broker Status:";
	private String INSTRUCTIONS = """
			<html>1. Click anywhere in screen area<br>
			2. The x and y values (0.0 - 1.0) will be shown<br>
			3. The gaze data will be sent to the
			communication service using Broker
			""";

	private JLabel gazeLabel;

	// ----------------------

	public GatherEyeGUI(GatherEye receiver){
		this.receiver = receiver;

		JFrame frame = new JFrame();
		createFrame(frame);

		addClickerPanel(frame);
		createLabels(frame);

		frame.setVisible(true);

	}

	private void createLabels(JFrame frame){
		int LABEL_OFFSET_X = WINDOW_SIZE_Y + BORDER_BUFFER * 2;
		int LABEL_OFFSET_Y = BORDER_BUFFER * 5;
		int LABEL_SIZE_X = 300;
		int LABEL_SIZE_Y = 100;

		addLabel(
				frame,
				LABEL_OFFSET_X, LABEL_OFFSET_Y,
				LABEL_SIZE_X, LABEL_SIZE_Y * 2,
				GAZE_POSITION
			);

		addLabel(
				frame,
				LABEL_OFFSET_X, LABEL_OFFSET_Y * 4,
				LABEL_SIZE_X, LABEL_SIZE_Y * 5,
				INSTRUCTIONS
			);

		addLabel(
				frame,
				LABEL_OFFSET_X, LABEL_OFFSET_Y * 8,
				LABEL_SIZE_X, LABEL_SIZE_Y,
				BROKER_STATUS
			);

	}

	private void createFrame(JFrame frame){
		frame.setTitle(WINDOW_TITLE);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(false);
		frame.setSize(WINDOW_SIZE_X, WINDOW_SIZE_Y);
		frame.setLayout(null);
	}

	private void addClickerPanel(JFrame frame){
		int panel_x, panel_y; panel_x = panel_y = BORDER_BUFFER;

		JPanel clickerPanel = new JPanel();
		clickerPanel.setBackground(Color.LIGHT_GRAY);
		clickerPanel.setBounds(panel_x, panel_y, CLICKER_PANEL_SIZE, CLICKER_PANEL_SIZE);
		clickerPanel.setVisible(true);

		frame.add(clickerPanel);
		listenForMouse(frame, clickerPanel);

	}

	private void listenForMouse(JFrame frame, JPanel clickerPanel){
		clickerPanel.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				float x = (float) e.getX() / CLICKER_PANEL_SIZE;
				float y = (float) e.getY() / CLICKER_PANEL_SIZE;

				GAZE_POSITION =
					"<html>Gaze Position:" +
					"<br>X:" + x +
					"<br>Y:" + y +
					"</html>";
				gazeLabel.setText(GAZE_POSITION);
				frame.revalidate();
				frame.repaint();

				receiver.sendBrokerCoords(x, y);
				// System.out.println("Mouse clicked at: " + x + ", " + y);
			}
		});

	}

	private void addLabel(JFrame frame, int x, int y, int width, int height, String text){
		JLabel label = new JLabel();
		label.setText(text);
		label.setBounds(x, y, width, height);
		label.setOpaque(true);
		label.setFont(WINDOW_FONT);

		if (text.equals(GAZE_POSITION)){
			gazeLabel = label;
		}

		frame.add(label);
	}

}
