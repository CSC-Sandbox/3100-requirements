public class VectorAnalysis implements GazeAnalysisStrategy{

	private Point latestPoint;
	private Vector latestHeadVector;
	private Vector latestTailVector;
	private Point[] pointList;

	private final int MAX_POINTS = 5;


	@Override
	String createMessage(int x, int y) {
		
		if ((pointList.size >= MAX_POINTS) {
			pointList.clear();
		}

		latestPoint = new Point(x, y);
		pointList.append(Point);

		if ((pointList.size == 1) {
			this.latestHeadVector = new Vector(0, 0);
			this.latestTailVector = new Vector(0, 0);
		} else {
			calculateVector(); 
		}

		return "GAZE VECTOR: HEAD_X;HEAD_Y;TAIL_X;TAIL_Y," +
			latestHeadVector.getX() + "," +
			latestHeadVector.getY() + "," +
			latestTailVector.getX() + "," +
			latestTailVector.getY();
	}

	private void calculateVector() {
	
			if ((this.latestTailVector.getX() == 0) &&
				(this.latestTailVector.getY() == 0) {
					this.latestTailVector.setX(this.pointList[0].getX());
					this.latestTailVector.setY(this.pointList[0].getY());
					pointList.remove(0);
			} 

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

			this.latestHeadVector = new Vector(meanX, meanY);

	}
}
