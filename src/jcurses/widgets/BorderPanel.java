package jcurses.widgets;

import jcurses.system.CharColor;
import jcurses.system.Toolkit;
import jcurses.util.Rectangle;

/**
*  This class implements a panel with a border.
*/


public class BorderPanel extends Panel {
	
	private CharColor _colors = getDefaultBorderColors();
	private static CharColor __defaultBorderColors = new CharColor(CharColor.WHITE, CharColor.BLACK);
    
	
	public BorderPanel() {
		super();
	}
	
	
	public BorderPanel(int width, int height) {
		super(width, height);
	}
	
	protected void paintSelf() {
		super.paintSelf();
		Toolkit.drawBorder(getRectangle(), getBorderColors());
	}
	
	
	protected void repaintSelf() {
		super.repaintSelf();
		Toolkit.drawBorder(getRectangle(), getBorderColors());
	}
	
	
	
	public CharColor getBorderColors() {
		return _colors;
	}
	
	public void setBorderColors(CharColor colors) {
		_colors = colors;
	}
	
	
	protected  CharColor getDefaultBorderColors() {
		return __defaultBorderColors; 
	}
	
	
	
	
	protected Rectangle getChildsRectangle() {
		Rectangle rect = (Rectangle)getSize().clone();
		rect.setLocation(1, 1);
		rect.setWidth(rect.getWidth()-2);
		rect.setHeight(rect.getHeight()-2);
		
		return rect;
	}
	
	
	

}
