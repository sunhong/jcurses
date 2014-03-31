package jcurses.widgets;

import jcurses.system.CharColor;
import jcurses.system.Toolkit;
import jcurses.util.Rectangle;

import java.util.StringTokenizer;

/**
*  This class implements a label widget
*/
public class Label extends Widget {
	
    private String _label = null; 
	
	private static CharColor __labelDefaultColors = new CharColor(CharColor.WHITE, CharColor.BLACK);
	
	public CharColor getDefaultColors() {
		return __labelDefaultColors;
	}
	
    /**
    *  The constructor
    * 
    * @param label label's text
    * param colors label's colors
    */
	public Label(String label, CharColor colors) {
		if (label!=null) {
			_label = label;
		} else {
			_label = "";
		}
		setColors(colors);
	}
	
    /**
    *  The constructor
    * 
    * @param label label's text
    */
	public Label(String label) {
		this(label, null);
	}
	
	/**
	 * Clear text
	 */
	protected void clear() {
		Rectangle rect = this.getSize();
		rect.setLocation(getAbsoluteX(), getAbsoluteY());
		Toolkit.drawRectangle(rect, __labelDefaultColors);
	}
	
	/**
	 * Set label
	 * @param label
	 */
	public void setLabel(String label) {
		this.clear();
		this._label = label;
		Rectangle rect = this.getPreferredSize();
		this.setSize(rect);
		rect.setLocation(getAbsoluteX(), getAbsoluteY());
		Toolkit.printString(_label, rect, getColors());
	}
	
	/**
	 * Get label
	 * @param label
	 * @return
	 */
	public String getLabel() {
		return this._label;
	}
	
	protected Rectangle getPreferredSize() {
		if (_label.indexOf("\n") == -1) {
			return new Rectangle(_label.length(),1);
		} else {
			StringTokenizer tokenizer = new StringTokenizer(_label,"\n");
			int width=0;
			int height=0;
			while (tokenizer.hasMoreElements()) {
				String token = tokenizer.nextToken();
				height++;
				if (token.length()>width) {
					width = token.length();
				}
			}
			height = (height == 0)?1:height;
			return new Rectangle(width, height);
		}
	}
	
	protected void doPaint() {
		Rectangle rect = (Rectangle)this.getSize();
		rect.setLocation(getAbsoluteX(), getAbsoluteY());
		Toolkit.drawRectangle(rect, __labelDefaultColors);
		Toolkit.printString(_label, rect, getColors());
	}
	
	protected void doRepaint() {
		doPaint();
	}
}
