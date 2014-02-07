jcurses
=======

Java Curses Library (JCurses)

website: http://javacurses.sourceforge.net/

Run jcurses program in Windows Console, please set Code Page to 850.
```
chcp 850
```

Updates:
------------------

11/19/2013

Fixed bug: using multiple layouts causes 'Unknown LayoutConstraints'

11/20/2013

Fixed bug: In some cases, windows cannot get keyboard input

11/26/2013

1. Actually add a window into Window Stack when it shows up, not in instantiation stage.
2. Before showing a window, check if system state is DOWN. If so, initiate it firstly.
3. When making a window invisible, avoid shutting down the system. Shutting down is made when the last window is removed.

12/02/2013

Changed the algorithm of sorting widgets

12/05/2013

Added setLabel and getLabel methods to Label class

01/22/2014

Rewrite setVisible method in TextComponent.
Now if a TextComponent is set to invisible, it disappears.

01/24/2014

Allows user to pick up items in List using number keys (1 to 9).
Notice: No support to index larger than 9.

02/07/2014

1. Add RadioButton class
2. Add ToggleButton class, this is the base of CheckBox and RadioButton
3. Add ToggleButtonGroup class. It holds a set of ToggleButton, but at most one of them can be selected.

