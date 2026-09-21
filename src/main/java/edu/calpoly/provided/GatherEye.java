package edu.calpoly.provided;

public class GatherEye {

	// Most recent x and y coordinate.
	// When info is sent to broker, updates this
	private float x;
	private float y;

	public GatherEye(){}

	public void displayGUI(){
		GatherEyeGUI gui = new GatherEyeGUI(this);
	}

	protected void sendBrokerCoords(float x, float y){
		String message = "GAZE," + x + "," + y;
		Broker broker = new Broker("localhost", 5000);
		broker.send(message);
		this.x = x;
	       	this.y = y;
	}

	public float getX(){return this.x;}
	public float getY(){return this.y;}

}
