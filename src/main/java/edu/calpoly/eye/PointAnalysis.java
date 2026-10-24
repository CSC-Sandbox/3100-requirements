public class PointAnalysis implements GazeAnalysisStrategy {

	Point latestPoint = new Point();
	String message;

	@Override
	String createMessage() {
		return "GAZE," + this.latestPoint.getX() + "," + this.latestPoint.getY();
	}
	String createMessage(int x, int y) {
		return "GAZE," + x + "," + y;
	}

	public void setX(int x) {
		this.latestPoint.setX(x);
	}

	public void setY(int y) {
		this.latestPoint.setY(y);
	}

}
