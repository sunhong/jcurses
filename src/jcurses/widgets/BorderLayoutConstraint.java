package jcurses.widgets;

class BorderLayoutConstraint extends LayoutConstraint {
	
	private Widget widget;

	private int position;

	
	public Widget getWidget() {
		return widget;
	}


	public int getPosition() {
		return position;
	}


	BorderLayoutConstraint(Widget widget, int position, int horizontalConstraint, int verticalConstraint) {
		this.widget = widget;
		this.position = position;
		this.horizontalConstraint = horizontalConstraint;
		this.verticalConstraint = verticalConstraint;
	}
}
