package jcurses.widgets;

import jcurses.util.Rectangle;

/**
 * This class is a layout manager that works like as the <code>DefaultLayoutManager</code>
 * with an difference: the painting rectangle is shared in many grid cells and the constraints
 * are stated not in real coodinates on the painting rectangle, but in 'grid-coordinates'
 */
public class GridLayoutManager extends LayoutManager implements WidgetsConstants {

	private DefaultLayoutManager _defLayout = new DefaultLayoutManager();
	//private WidgetContainer _father = null;

	private int _width = 0;
	
	private int _height = 0;

	private Grid _grid = null;

	/**
	 * The constructor 
	 * @param width the width of the grid ( in cells )
	 * @param height the height of the grid ( in cells )
	 * 
	 */
	public GridLayoutManager(int width, int height) {
		_width = width;
		_height = height;
	}

	public void layout(Widget widget, LayoutConstraint constraint) {
		
		if (! (constraint instanceof GridLayoutConstraint)) {
			throw new RuntimeException("unknown constraint: " + constraint.getClass().getName());
		}
		GridLayoutConstraint layoutConstraint = (GridLayoutConstraint)constraint;
		Rectangle rect = (_father.getChildsRectangle() == null)?_father.getSize():_father.getChildsRectangle();
		_grid = new Grid(rect, _width, _height);
		_defLayout.layout(widget, layoutConstraint.getDefaultLayoutConstraint(_grid));
	}

	/**
	 *  Adds a widget to the boundeb container
	 * 
	 * @param widget widget to be added
	 * @param x the column index of the top left corner of the rectangle, within that the widget is placed
	 * @param y the row index of the top left corner of the rectangle, within that the widget is placed
	 * @param width the number of columns, within that the widget is placed
	 * @param height the number of rows, within that the widget is placed
	 * @param verticalConstraint vertical alignment constraint. Following values a possible: 
	 * <code>WidgetConstraints.ALIGNMENT_CENTER</code>,<code>WidgetConstraints.ALIGNMENT_TOP</code>,<code>WidgetConstraints.ALIGNMENT_BOTTOM</code>
	 * @param horizontalConstraint vertical alignment constraint, Following values are possible:
	 * <code>WidgetConstraints.ALIGNMENT_CENTER</code>,<code>WidgetConstraints.ALIGNMENT_LEFT</code>,<code>WidgetConstraints.ALIGNMENT_RIGHT</code>
	 */
	public void addWidget(Widget widget, int x, int y, int width, int height, int verticalConstraint, int horizontalConstraint) 
	throws IllegalArgumentException{
		if (x<0) throw new IllegalArgumentException("x is negative");
		if (x+width>this._width) throw new IllegalArgumentException("x plus width is out of range");
		if (y<0) throw new IllegalArgumentException("y is negative");
		if (y+height>this._height) throw new IllegalArgumentException("y plus height is out of range");
		if (_father==null) throw new NullPointerException("Root container is null");
		_father.addWidget(widget, new GridLayoutConstraint(x, y, width, height, horizontalConstraint, verticalConstraint));
	}

	/**
	 * Removes a widget 
	 * @param widget widget to remove
	 */
	public void removeWidget(Widget widget) {
		_father.removeWidget(widget);
	}
}