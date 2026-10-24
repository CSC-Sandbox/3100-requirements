public class Rectangle {

	private int x;
	private int y;
	private int width;
	private int height;

	public Rectangle(int x, int y, int width, int height) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
	}

	public Rectangle() {
		this.x = 0;
		this.y = 0;
		this.width = 0;
		this.height = 0;
	}

	public String getX(){
		return this.x;
	}
	public String getY(){
		return this.y;
	}
	public String getWidth(){
		return this.width;
	}
	public String getHeight(){
		return this.height;
	}

	public void setX(int x){
		this.x = x;
	}
	public void setY(int x){
		this.y = x;
	}
	public void setWidth(int x){
		this.width = x;
	}
	public void setHeight(int x){
		this.height = x;
	}


}
