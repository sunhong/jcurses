package jcurses.widgets;

import jcurses.util.Rectangle;

public class Grid {
	
	private int [] _widths;
	private int [] _heights;

	/**
	 * Constructor of Grid
	 * @param rect Rectangular area
	 * @param width Number of columns
	 * @param height Number of rows
	 */
	Grid(Rectangle rect, int width, int height) {
		if (((rect.getWidth()/width) <1) || ((rect.getHeight()/height) <1)) {
			throw new RuntimeException (" the grid is to fine: "+rect.getWidth()+":"+rect.getHeight()+":"+width+":"+height);
		}

		_widths = new int [width];
		_heights = new int [height];

		fillArray(_widths, rect.getWidth(), width);
		fillArray(_heights, rect.getHeight(), height);
	}

	private void fillArray(int [] array, int rectWidth, int width) {

		int mod = rectWidth%width;
		int cellWidth = rectWidth/width;

		for (int i=0; i<width; i++) {
			if (mod > 0) {
				array [i] = cellWidth+1;
				mod --;
			} else {
				array [i] = cellWidth;
			}
		}
	}

	/**
	 * Get the rectangle composed by one or more grids
	 * @param x Row index of top-left grid
	 * @param y Column index of top-left grid
	 * @param width Number of columns 
	 * @param height Number of rows
	 * @return
	 */
	Rectangle getRectangle(int x, int y, int width, int height) {
		return new Rectangle(getWidth(_widths, 0,x), getWidth(_heights, 0,y), getWidth(_widths, x,x+width), 
				getWidth(_heights, y, y+height));
	}

	private int getWidth(int [] array, int begin, int end) {
		int width = 0;
		for (int i=begin; i<end; i++) {
			width+=array[i];
		}
		return width;
	}
}
