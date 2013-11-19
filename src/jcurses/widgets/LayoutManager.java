package jcurses.widgets;

/******************************************************
* This interface must be implemented bei layout managers,
* that layout widgets within widget containers.
*/
public abstract class LayoutManager {
	
	protected WidgetContainer _father;
	
	/**
	* The method layouts a widget within a container, dependet of layouting constraints.
    * 
    * @param child widget to layout
    * @param constraint layout constraint
	*/
   public abstract void layout(Widget child, LayoutConstraint constraint);
	
	/**
	*  The method is called by framework by binding the layout manager
    *  to an container. The implementing class must register, whether this is
    * already bound or not, and throw an exception by a try to bind it second time.
    * This is because the framework restricts, that a lyout manager can be bound only 
    * to one container at a time.
    * 
    * @param container container to bind
	*/
	public void bindToContainer(WidgetContainer container) {
		if (_father != null) {
			throw new RuntimeException ("Already bound!!!");
		}
		_father = container;
		_father.setLayoutManager(this);
	}
	
	/**
	* The method is called by framework by unbinding the layout manager
    *  from its container. The implementing class must register, whether this is
    * already bound or not, and throw an exception by a try to bind it second time.
    * This is because the framework restricts, that a lyout manager can be bound only 
    * to one container at a time.
    *
	*/
	public void unbindFromContainer() {
		_father = null;
	}
}
