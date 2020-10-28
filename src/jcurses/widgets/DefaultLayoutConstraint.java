package jcurses.widgets;

class DefaultLayoutConstraint extends LayoutConstraint {
	
	//X coordinate of top-left point
	private int x;
	//Y coordinate of top-left point
	private int y;
	//Width of painting area
	private int width;
	//Height of painting area
	private int height;

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	DefaultLayoutConstraint(int x, int y, int width, int height, int horizontalConstraint, int verticalConstraint) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.horizontalConstraint = horizontalConstraint;
		this.verticalConstraint = verticalConstraint;
	}
}
