public class Vector {

	private int x;
	private int y;

	public Vector(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public Vector() {
		this.x = 0;
		this.y = 0;
	}

	public String getX(){
		return this.x;
	}
	public String getY(){
		return this.y;
	}

	public void setX(int x){
		this.x = x;
	}
	public void setY(int x){
		this.y = x;
	}

}
