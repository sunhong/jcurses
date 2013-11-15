package jcurses.tests;



import jcurses.system.*;

import jcurses.widgets.*;

import jcurses.util.*;

import jcurses.event.*;

import java.awt.Rectangle;

import java.io.*;







public class Test extends Window implements  ItemListener, ActionListener, ValueChangedListener, WindowListener, WidgetsConstants {

	public static void main(String [] args) throws Exception{

		//Protocol initialisieren

        System.setProperty("jcurses.protocol.filename","jcurses.log");

		Protocol.activateChannel(Protocol.DEBUG);

		Protocol.debug("Programm beginnt");

		/*FileOutputStream stream = new FileOutputStream("test.txt");

		OutputStreamWriter writer = new OutputStreamWriter(stream,"Cp850");

		writer.write("Wählen");

		writer.flush();

		writer.close();*/

		Toolkit.beep();

		Window test = new Test(28,20);

		test.addListener((WindowListener)test);

		test.show();

		//Toolkit.clearScreen(new CharColor(CharColor.BLUE, CharColor.BLUE, CharColor.REVERSE));

		

		

		

		

		

	}

    

    

	

	private CheckBox _c1 = null;

	private CheckBox _c2 = null;

	private Label _l1 = null;

	private Label _l2 = null;

	private Button _b1 = null;

	private Button _b2 = null;

	private List _list = null;

	

	

	private TextArea _textArea = new TextArea(-1,-1,"1111\n2222\n3333\n4444\n\n66666\n77777\n888888\n99999999999999999\n1010100101");

	private PasswordField _pass = new PasswordField();

	

	

	public Test(int width, int height) {

		super(width,height, true, "Test");

		

		BorderPanel bp = new BorderPanel();

		

		_c1 = new CheckBox();

		_c2 = new CheckBox(true);

		_l1 = new Label("textfeld");

		_l2 = new Label("checkbox2");

		_b1 = new Button("OK");

		_b1.setShortCut('o');

		_b1.addListener(this);

		_b2 = new Button("Cancel");

		_b2.setShortCut('p');

		_b2.addListener(this);

		

		_list = new List();

		_list.add("item1");

		_list.add("item201234567890123456789");

		_list.add("item3");

		_list.add("item4");

		_list.add("item5");

		_list.addListener(this);
		
		_list.getSelectedItemColors().setColorAttribute(CharColor.BOLD);

		

		

		GridLayoutManager manager1 = new GridLayoutManager(1,1);

		getRootPanel().setLayoutManager(manager1);

		manager1.addWidget(bp,0,0,1,1,ALIGNMENT_CENTER,ALIGNMENT_CENTER);

		

		

		GridLayoutManager manager = new GridLayoutManager(2,5);

		bp.setLayoutManager(manager);

		

		//manager.addWidget(_l1,0,0,1,2,ALIGNMENT_CENTER, ALIGNMENT_CENTER);

		manager.addWidget(_list,0,0,1,4,ALIGNMENT_TOP,ALIGNMENT_CENTER);

		manager.addWidget(_textArea,1,0,1,2,ALIGNMENT_CENTER,ALIGNMENT_CENTER);

		manager.addWidget(_pass,1,2,1,2,ALIGNMENT_CENTER,ALIGNMENT_CENTER);

	    manager.addWidget(_b1,0,4,1,1,ALIGNMENT_CENTER,ALIGNMENT_CENTER);

		manager.addWidget(_b2,1,4,1,1,ALIGNMENT_CENTER,ALIGNMENT_CENTER);

	}

	

	

	

	

	public void actionPerformed(ActionEvent event) {

		Widget w = event.getSource();

	    

		if (w == _b1) {

			Protocol.debug("point1");	

			FileDialog dial = new FileDialog("File wählen");

			Protocol.debug("point2");	

			dial.show();

			Protocol.debug("point3");	

			if (dial.getChoosedFile()!=null) {

				new Message("Meldung!", dial.getChoosedFile().getAbsolutePath()+"", "OK").show();

			}

			Protocol.debug("point4");

			_pass.setVisible(!_pass.isVisible());

			pack();

			paint();

		}  else {

			new Message("Meldung!", "01234567890\nassssssss\naaaaaaa\naaaaaa", "CANCEL").show();

			PopUpMenu menu = new PopUpMenu(53, 5, "test");

		

			for (int i=1; i<100; i++) {

				if ((i == 35) || (i==4) ) {
					menu.addSeparator();
				} else {
					menu.add("item"+i);
				}

			}

			menu.show();

			new Message("meldung",menu.getSelectedItem()+":"+menu.getSelectedIndex(),"OK").show();

		}

		//close();

	}

	

	

	public void stateChanged(ItemEvent e) {

		Protocol.debug("-----------------");

		new Message("meldung",e.getItem()+":"+e.getType(),"OK").show();

	}

	

	

	public void valueChanged(ValueChangedEvent e) {

		

		new Message("Alarm","Geändert in ",""+_list.getSelectedIndex()).show();

	}

	

	

	public void windowChanged(WindowEvent event) {

		Protocol.debug("window event: "+event.getType());

		if (event.getType() == WindowEvent.CLOSING) {

			event.getSourceWindow().close();

		}

	}

	

	

	

	

	

}

