package jcurses.widgets;

/**
 * The base class of GridLayoutConstraint, BorderLayoutConstraint and DefaultLayoutConstraint
 * @author sunhong
 *
 */
public abstract class LayoutConstraint {

	protected int horizontalConstraint;

	protected int verticalConstraint;

	public int getHorizontalConstraint() {
		return horizontalConstraint;
	}

	public int getVerticalConstraint() {
		return verticalConstraint;
	}
}
