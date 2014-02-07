package jcurses.event;

/**
*  The interface has to be impelemented to listen on instances of <code>WindowEvent</code>
*/
public interface WindowListener extends Listener {
    /**
    *  The method will be called by an widget, generating <code>WindowEvent</code> instances,
    * if the listener has been registered by it.
    * 
    * @param event the event occured
    */    
	public abstract void windowChanged(WindowEvent event);

}
