package jcurses.widgets;

import jcurses.system.CharColor;
import jcurses.system.InputChar;
import jcurses.system.Toolkit;

import jcurses.util.Rectangle;

/*
 *  This class implements a checkbox widget. This checkboxes state is modified
 * by typing a special char (default 'space'). You can register listeners by this
 * widget to track state changes.
 **/
public class CheckBox extends ToggleButton {

	/*
	 *  The constructor.
	 * 
	 * @param checked true, if the checkbox is checked at first time, false otherwise
	 **/
	public CheckBox(boolean selected) {
		this._selected = selected;
	}


	/*
	 *  The constructor creates an unchecked checkbox
	 **/
	public CheckBox() {
		this(false);
	}

	protected void doPaint() {
		Rectangle rect = (Rectangle)getSize().clone();
		rect.setLocation(getAbsoluteX(), getAbsoluteY());
		String text = "["+((_selected)?"X":" ")+"]";
		CharColor colors = hasFocus()?getFocusedColors():getColors();
		Toolkit.printString(text,rect, colors);
	}
	
	protected boolean handleInput(InputChar ch) {
		if (ch.equals(_changeStatusChar)) {
			setSelected(!_selected);
			return true;
		}else{
			return false;
		}
	}
}
