package com.modofo.mofire.ui;
import java.util.Vector;

import javax.microedition.lcdui.Alert;
import javax.microedition.lcdui.AlertType;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Form;
import javax.microedition.midlet.MIDlet;

import com.modofo.mofire.MoFire;
import com.modofo.mofire.MoFireEvent;
import com.modofo.mofire.MoFireListener;


public abstract class BaseForm extends Form implements CommandListener{
	protected MoFire mofire;
	protected Vector listeners = new Vector();
	protected MIDlet midlet;
	public BaseForm(){
		super("");
	}
	
	public BaseForm(MoFire mofire,String title){
		super(title);
		this.mofire = mofire;
		this.midlet = MoFire.getMidlet();
		listeners.addElement(mofire);
	}
	
	public BaseForm(MIDlet midlet,String title){
		super(title);
		this.midlet = midlet;
	}
	
	public void addListener(MoFireListener listener){
		listeners.addElement(listener);
	}
	
	public void fireEvent(MoFireEvent evt){
		for(int i=0;i<listeners.size();i++){
			MoFireListener l = (MoFireListener) listeners.elementAt(i);
			l.onEvent(evt);
		}
	}
	
	protected void showConfirmDialog(String title, String text,String oklabel,String cancellabel,CommandListener listener) {
        Alert alert = new Alert(title, text, null, AlertType.CONFIRMATION);
        alert.addCommand(new Command (oklabel, Command.OK, 1));
        alert.addCommand(new Command(cancellabel, Command.CANCEL, 1));
        alert.setCommandListener(listener);
        Display.getDisplay(midlet).setCurrent(alert, this);
    }
	
	public void setMIDlet(MIDlet mdl){
		this.midlet = mdl;
	}
}
