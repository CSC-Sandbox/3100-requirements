public class AreaAnalysis implements GazeAnalysisStrategy{

	private Point latestPoint;
	private Rectangle latestRectangle;
	private Point[] pointList;

	private final int BORDER_SIZE = 3;


	@Override
	String createMessage(int x, int y) {
		
		latestPoint = new Point(x, y);
		pointList.append(Point);

		if (1 < pointList.length < 6) {
			this.latestRectangle = calculateRectangle();
		} else {
			pointList.clear();
			this.latestRectangle = new Rectangle(
				latestPoint.getX() - BORDER_SIZE,
				latestPoint.getY() - BORDER_SIZE,
				BORDER_SIZE * 2,
				BORDER SIZE * 2
			);
		}

		return "GAZE AREA: X;Y;WIDTH;HEIGHT," +
			latestRectangle.getX() + "," +
			latestRectangle.getY() + "," +
			latestRectangle.getWidth() + "," + 
			latestRectangle.getHeight();
	}

	private calculateRectangle() {

		int totalX = 0;
		int totalY = 0;

		float meanX = 0;
		float meanY = 0;


		for (point:pointList) {
			totalX += point.getX();
			totalY += point.getY();
			
		}

		meanX = totalX / pointList.length();
		meanY = totalY / pointList.length();
		
		float deviationX = 0;
		float deviationY = 0;

		for (point:pointList) {
			deviationX += Math.abs(point.getX() - meanX);
			deviationY += Math.abs(point.getY() - meanY);
		}
		
		float rectWidth = (deviationX / pointList.length());
		float rectHeight = (deviationY / pointList.length());
		float rectX = meanX - deviationX / 2;
		float rectY = meanY - deviationY / 2;

		return new Rectangle(
			rectX;
			rectY;
			rectWidth;
			rectHeight;
		)
	}

}
