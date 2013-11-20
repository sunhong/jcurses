package jcurses.widgets;

import jcurses.util.Rectangle;

/**
 *  This is a default layout manager. The constraints state for each widget 
 * to layout a coordinates of the rectangle, within that the widget is placed
 * and the alignment of the widget, if its preferred size is smaller as the rectangle's
 * size-
 */
public class DefaultLayoutManager extends LayoutManager implements WidgetsConstants {

	//private WidgetContainer _father = null;

	public void layout(Widget widget, LayoutConstraint constraint) {
		if (! (constraint instanceof DefaultLayoutConstraint)) {
			throw new RuntimeException("unknown constraint: "+constraint.getClass().getName());
		}

		DefaultLayoutConstraint cstr = (DefaultLayoutConstraint)constraint;

		Rectangle prefSize = widget.getPreferredSize();

		int prefWidth = prefSize.getWidth();
		int prefHeight = prefSize.getHeight();

		if (prefWidth<=0) {
			prefWidth = cstr.getWidth();
		}

		if (prefHeight<=0) {
			prefHeight = cstr.getHeight();
		}

		int width = 0;
		int height = 0;

		if (prefWidth < cstr.getWidth()) {
			widget.setX(getAlignedCoordinate(prefWidth, cstr.getWidth(), cstr.getX(), cstr.getHorizontalConstraint()));
			width = prefWidth; 
		} else {
			widget.setX(cstr.getX());
			width = cstr.getWidth();
		}


		if (prefHeight < cstr.getHeight()) {
			widget.setY(getAlignedCoordinate(prefHeight, cstr.getHeight(), cstr.getY(), cstr.getVerticalConstraint()));
			height = prefHeight; 
		} else {
			widget.setY(cstr.getY());
			height = cstr.getHeight();
		}

		widget.setSize(new Rectangle(width, height));
	}


	private int getAlignedCoordinate(int prefG, int contG, int contC, int alignment) {

		if (alignment == ALIGNMENT_CENTER) {
			alignment = 0;
		} else if ((alignment == ALIGNMENT_BOTTOM) || (alignment == ALIGNMENT_RIGHT)) {
			alignment = 1;
		} else {
			alignment = 2;
		}

		int result = 0;
		if (alignment == 2) {
			result = contC;
		} else if (alignment == 1) {
			result = contC+contG-prefG;
		} else {
			result = contC+(contG-prefG)/2;
		}
		return result;
	}


	/**
	 *  Adds a widget to the boundeb container
	 * 
	 * @param widget widget to be added
	 * @param x the x coordinate of the top left corner of the rectangle, within that the widget is placed
	 * @param y the y coordinate of the top left corner of the rectangle, within that the widget is placed
	 * @param width the width of the rectangle, within that the widget is placed
	 * @param height the hight of the rectangle, within that the widget is placed
	 * @param verticalConstraint vertical alignment constraint. Following values a possible: 
	 * <code>WidgetConstraints.ALIGNMENT_CENTER</code>,<code>WidgetConstraints.ALIGNMENT_TOP</code>,<code>WidgetConstraints.ALIGNMENT_BOTTOM</code>
	 * @param horizontalConstraint vertical alignment constraint, Following values are possible:
	 *  * <code>WidgetConstraints.ALIGNMENT_CENTER</code>,<code>WidgetConstraints.ALIGNMENT_LEFT</code>,<code>WidgetConstraints.ALIGNMENT_RIGHT</code>
	 */

	public void addWidget(Widget widget, int x, int y, int width, int height, int verticalConstraint, int horizontalConstraint) 
	throws IllegalArgumentException{
		if (x<0) throw new IllegalArgumentException("x is negative");
		if (y<0) throw new IllegalArgumentException("y is negative");
		if (width<0) throw new IllegalArgumentException("width is negative");
		if (height<0) throw new IllegalArgumentException("height is negative");
		if (_father==null) throw new NullPointerException("Root container is null");
		_father.addWidget(widget, new DefaultLayoutConstraint(x, y, width, height, horizontalConstraint, verticalConstraint));
	}


	/**
	 *  Removes a widget from the container
	 * 
	 * @param widget widget to be removed
	 */

	public void removeWidget(Widget widget) {
		_father.removeWidget(widget);
	}
}
