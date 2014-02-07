package jcurses.widgets;

import jcurses.event.ValueChangedEvent;
import jcurses.event.ValueChangedListener;
import jcurses.event.ValueChangedListenerManager;
import jcurses.system.CharColor;
import jcurses.system.InputChar;
import jcurses.system.Toolkit;
import jcurses.util.Rectangle;

/**
 * The base class of RadioButton and CheckBox
 */
public abstract class ToggleButton extends Widget {
	
	protected ValueChangedListenerManager _listenerManager = new ValueChangedListenerManager();
	
	protected ToggleButtonGroup group = null;
	
	protected boolean _selected = false;

	protected static CharColor _defaultColors = new CharColor(CharColor.WHITE, CharColor.BLACK);

	protected static CharColor _focusedDefaultColors = new CharColor(CharColor.BLUE, CharColor.WHITE, CharColor.REVERSE);
	
	protected CharColor _focusedColors = getFocusedDefaultColors();
	
	public CharColor getDefaultColors() {
		return _defaultColors;
	}

	private CharColor getFocusedDefaultColors() {
		return _focusedDefaultColors;
	}

	/**
	 *  @return focused colors
	 */
	public CharColor getFocusedColors() {
		return _focusedColors;
	}

	/**
	 *  Sets colors of the toggle button in focused state.
	 *  @param colors colors when it is focused
	 */
	public void setFocusedColors(CharColor colors) {
		_focusedColors = colors;
	}
	
	public void setGroup(ToggleButtonGroup group) {
		this.group = group;
	}
	
	protected boolean isFocusable() {
		return true;
	}

	@Override
	protected Rectangle getPreferredSize() {
		return new Rectangle(3,1);
	}
	
	public boolean isSelected() {
		return this._selected;
	}
	
	public void setSelected(boolean selected) {
		boolean oldValue = _selected;
		this._selected = selected;
		if (oldValue != _selected) {
			_listenerManager.handleEvent(new ValueChangedEvent(this));
		}
		paint();
		if (selected && group!=null){
			ToggleButton oldSelection = group.getSelection();
			if (oldSelection!=null && oldSelection!=this){
				oldSelection.setSelected(false);
			}
			group.setSelected(this, true);
		}
	}
	
	protected static InputChar _changeStatusChar = new InputChar(' ');
	
	private void changeColors() {
		CharColor colors = hasFocus()?getFocusedColors():getColors();
		Toolkit.changeColors(getRectangle(),colors);
	}

	protected void focus() {
		changeColors();
	}

	protected void unfocus() {
		changeColors();
	}

	@Override	
	protected void doRepaint() {
		doPaint();
	}
	
	/**
	 *  Adds listener to the checkbox to track states changes
	 * @param listener listener to add
	 */
	public void addListener(ValueChangedListener listener) {
		_listenerManager.addListener(listener);
	}

	/**
	 *  Removes listener from the checkbox
	 * @param listener to remove
	 */
	public void removeListener(ValueChangedListener listener) {
		_listenerManager.removeListener(listener);
	}

}
