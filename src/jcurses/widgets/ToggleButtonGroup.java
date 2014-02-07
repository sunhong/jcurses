package jcurses.widgets;

import java.util.Enumeration;
import java.util.Vector;

/**
 * This class is a set of ToggleButton 
 * Only one ToggleButton in this group can be selected
 */
public class ToggleButtonGroup {
	
	// the list of buttons participating in this group
    protected Vector<ToggleButton> buttons = new Vector<ToggleButton>();
    
    /**
     * The current selection.
     */
    ToggleButton selection = null;

    /**
     * Creates a new <code>ButtonGroup</code>.
     */
    public ToggleButtonGroup() {}
    
    /**
     * Adds a radio button to the group.
     * @param b the radio button to be added
     */
    public void add(ToggleButton b) {
        if(b == null) {
            return;
        }
        buttons.addElement(b);

        if (b.isSelected()) {
            if (selection == null) {
                selection = b;
            } else {
                b.setSelected(false);
            }
        }

        b.setGroup(this);
    }

    /**
     * Removes the button from the group.
     * @param b the button to be removed
     */
    public void remove(ToggleButton b) {
        if(b == null) {
            return;
        }
        buttons.removeElement(b);
        if(b == selection) {
            selection = null;
        }
        b.setGroup(null);
    }

    /**
     * Clears the selection such that none of the buttons
     * in the <code>ToggleButtonGroup</code> are selected.
     */
    public void clearSelection() {
        if (selection != null) {
            ToggleButton oldSelection = selection;
            selection = null;
            oldSelection.setSelected(false);
        }
    }
    
    /**
     * Returns the number of buttons in the group.
     * @return the button count
     */
    public int getButtonCount() {
        if (buttons == null) {
            return 0;
        } else {
            return buttons.size();
        }
    }
    
    /**
     * Returns all the buttons that are participating in this group.
     * @return an <code>Enumeration</code> of the buttons in this group
     */
    public Enumeration<ToggleButton> getElements() {
        return buttons.elements();
    }

    /**
     * Returns the selected button.
     * @return the selected button
     */
    public ToggleButton getSelection() {
        return selection;
    }

    /**
     * Sets the selected value for the <code>ToggleButton</code>.
     * Only one button in the group may be selected at a time.
     * @param b the <code>ToggleButton</code>
     * @param selected <code>true</code> if this button is to be
     *   selected, otherwise <code>false</code>
     */
    public void setSelected(ToggleButton b, boolean selected) {
        if (selected && b != null && b != selection) {
            ToggleButton oldSelection = selection;
            selection = b;
            if (oldSelection != null) {
                oldSelection.setSelected(false);
            }
            b.setSelected(true);
        }
    }
}
