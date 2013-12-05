/* -*- tab-width: 8; indent-tabs-mode: t; c-basic-offset: 8 -*- */
package jcurses.widgets;
import jcurses.system.Toolkit;
import jcurses.system.CharColor;
import jcurses.system.InputChar;
import jcurses.util.Rectangle;
import java.util.Vector;
/**
 * This class is a jcurses-internal class, whose task is 
 * to manage jcurses text based windows. It schouldn't be used writing applications. 
 */
class WindowManager {

	private static Vector<Window> __windowsStack = new Vector<Window>();
	private static CharColor __defaultScreenColors = new CharColor(CharColor.BLUE, CharColor.BLUE);
	private static CharColor __colors = getDefaultScreenColors();	public static enum STATE {		UP, DOWN	}; 	private static STATE __state = STATE.DOWN;
	/**
	 * Method to be used by external threads wishing to perform
	 * safe calls to jcurses widgets. Access to this method is
	 * provided from WidgetUtilities.invokeAndWait().
	 *
	 * @param r a <code>Runnable</code> containing the code to be
	 * executed in a thread-safe manner.
	 */
	static synchronized void invokeAndWait(Runnable r) {
		r.run();
	}
	public static CharColor getScreenColors() {
		return __colors;
	}
	public static void setScreenColors(CharColor colors) {
		__colors = colors;
	}
	protected static CharColor getDefaultScreenColors() {
		return __defaultScreenColors; 
	}
	protected static void createWindow(Window w) {
		if (__windowsStack.size() == 0) {
			init();
		}
		if (!__windowsStack.contains(w)) {			__windowsStack.add(w);		}
	}
	protected static void removeWindow(Window w) {
		if (__windowsStack.indexOf(w)!=-1) {
			removeWindowFromScreen(w);
			__windowsStack.remove(w);
			w.closed();			Window tw = getTopWindow();
			if (tw == null) {
				shutdown();
			} else {
				tw.activate();
			}
		}
	}
	protected static void makeWindowVisible(Window w, Window oldTop) {		if (__state == STATE.DOWN){			init();		}
		Toolkit.startPainting();
		if (__windowsStack.indexOf(w)!=-1) {
			int index = __windowsStack.indexOf(w);
			for (int i=index; i<__windowsStack.size(); i++) {
				Window aw = (Window)__windowsStack.elementAt(i);
				if (aw.isVisible()) {
					aw.paint();
				}
			}
		}
		if (getTopWindow()!=oldTop) {
			if (oldTop!=null) {
				oldTop.deactivate();
			}
			getTopWindow().activate();
		}
		Toolkit.endPainting();
	}
	protected static void makeWindowInvisible(Window w, Window oldTop) {
		if (__windowsStack.indexOf(w)!=-1) {
			removeWindowFromScreen(w);
			if (w == oldTop) {
				w.deactivate();
				if (getTopWindow() != null) {
					getTopWindow().activate();
				}
			}
		}
	}
	private static void removeWindowFromScreen(Window w) {
		Toolkit.startPainting();
		int index = __windowsStack.indexOf(w);
		if (!wasPartVisible(index)) {
			Toolkit.endPainting();
			return;
		}
		Rectangle rect = w.getRectangle();
		if (w.hasShadow()) {
			rect = ((Rectangle)rect.clone());
			rect.resize(rect.getWidth()+1, rect.getHeight()+1);
		}
		Toolkit.drawRectangle(rect, getScreenColors());
		for (int i=0; i<index; i++) {
			Window aw = (Window)__windowsStack.elementAt(i);
			if (aw.isVisible()) {
				Rectangle rect2 = aw.getRectangle();
				if (aw.hasShadow()) {
					rect2 = ((Rectangle)rect2.clone());
					rect2.resize(rect2.getWidth()+1, rect2.getHeight()+1);
				}
				Rectangle clipRect = rect.intersection(rect2);
				if ((!clipRect.isEmpty()) && (isToBeRepainted(clipRect, i,index))) {
					Toolkit.setClipRectangle(clipRect);
					aw.repaint();
					Toolkit.unsetClipRectangle();
				}
			}	
		}
		Toolkit.endPainting();
	}
	private static boolean isToBeRepainted(Rectangle clipRect, int index, int endIndex) {
		boolean result = true;
		for (int i=index+1; i<endIndex; i++) {
			Window aw = (Window)__windowsStack.elementAt(i);
			if ((aw.isVisible()) && (aw.getRectangle().contains(clipRect))) {
				result = false;
				break;
			}
		}
		return result;
	}
	private static boolean wasPartVisible(int index) {		Window aw = (Window)__windowsStack.elementAt(index);		Rectangle rect = aw.getRectangle();		if (aw.hasShadow()) {			rect = ((Rectangle)rect.clone());			rect.resize(rect.getWidth()+1, rect.getHeight()+1);		}
		for (int i=index+1; i<__windowsStack.size(); i++) {
			Window aw1 = (Window)__windowsStack.elementAt(i);
			if (aw1.isVisible() && (aw1.getRectangle().contains(rect))) {				return false;
			}
		}
		return true;
	}
	protected static void moveToTop(Window w) {
		Window oldTop = getTopWindow();
		if (__windowsStack.indexOf(w)!=-1) {
			__windowsStack.remove(w);
			__windowsStack.add(w);
			Toolkit.startPainting();
			w.paint();
			Toolkit.endPainting();
			if (oldTop!=null) {
				oldTop.deactivate();
			}
			w.activate();
		}
	}
	protected static Window getTopWindow() {
		for (int i=__windowsStack.size()-1;i>=0;i--) {
			Window window = (Window)__windowsStack.elementAt(i);
			if (window.isVisible()) {
				return window;
			}
		}
		return null;
	}
	public synchronized static void init() {
		Toolkit.clearScreen(getScreenColors());
		startInputThread();		__state = STATE.UP;
	}
	private synchronized static void shutdown() {
		deactivateInputThread();
		Toolkit.shutdown();
		stopInputThread();		__state = STATE.DOWN;
	}
	private static WindowManagerInputThread _inthread = new WindowManagerInputThread();
	private synchronized static void startInputThread() {		_inthread.activate();		if (!_inthread.isAlive()){			_inthread.start();		}
	}
	private synchronized static void stopInputThread() {
		_inthread.end();
	}
	private synchronized static void deactivateInputThread() {
		_inthread.deactivate();
	}
	public static void blockInputThread(WindowManagerBlockingCondition cond) {
		_inthread.block(cond);
	}
	public static boolean isInputThread() {
		return (Thread.currentThread() == _inthread); 
	}
	protected synchronized static void handleInput(InputChar input) {
		Window tw = getTopWindow();
		Toolkit.startPainting();
		if (tw!=null) {
			try {
				tw.handleInput(input);
			} catch (Throwable e) {
				Toolkit.shutdown();
				e.printStackTrace();
				System.exit(1);
			}
		}
		if (_inthread.isAlive()) {
			Toolkit.endPainting();
		}
	}
}
interface WindowManagerBlockingCondition {
	boolean evaluate();
}
