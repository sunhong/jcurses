package jcurses.widgets;

import jcurses.util.Rectangle;

class GridLayoutConstraint extends LayoutConstraint {

	//Column index of top-left point
	private int x;
	//Row index of top-left point
	private int y;
	//Number of columns of painting area
	private int width;
	//Number of rows of painting area
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

	GridLayoutConstraint(int x, int y, int width, int height, int horizontalConstraint, int verticalConstraint) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.horizontalConstraint = horizontalConstraint;
		this.verticalConstraint = verticalConstraint;
	}

	DefaultLayoutConstraint getDefaultLayoutConstraint(Grid grid) {

		Rectangle rect = grid.getRectangle(x,y,width,height);
		return new DefaultLayoutConstraint(rect.getX(), rect.getY(), rect.getWidth(), rect.getHeight(),
				horizontalConstraint, verticalConstraint);
	}
}
