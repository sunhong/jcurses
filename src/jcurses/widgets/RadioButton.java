package jcurses.widgets;

import jcurses.system.CharColor;
import jcurses.system.InputChar;
import jcurses.system.Toolkit;
import jcurses.util.Rectangle;

public class RadioButton extends ToggleButton {
	
	public RadioButton(boolean selected){
		this._selected = selected;
	}
	
	public RadioButton(){
		this(false);
	}

	protected void doPaint() {
		Rectangle rect = (Rectangle)getSize().clone();
		rect.setLocation(getAbsoluteX(), getAbsoluteY());
		String text = "("+((_selected)?"O":" ")+")";
		CharColor colors = hasFocus()?getFocusedColors():getColors();
		Toolkit.printString(text,rect, colors);
	}
	
	protected boolean handleInput(InputChar ch) {
		if (ch.equals(_changeStatusChar)) {
			if (!_selected) this.setSelected(true);
			return true;
		}else{
			return false;
		}
	}

}
