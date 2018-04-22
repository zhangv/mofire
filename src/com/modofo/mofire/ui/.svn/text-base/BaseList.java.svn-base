package com.modofo.mofire.ui;
import java.util.Vector;

import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.List;
import javax.microedition.midlet.MIDlet;

import com.modofo.mofire.MoFire;
import com.modofo.mofire.MoFireEvent;
import com.modofo.mofire.MoFireListener;


public abstract class BaseList extends List implements CommandListener{
	protected MoFire mofire;
	protected Vector listeners = new Vector();
	protected MIDlet midlet;
	public BaseList(){
		super("", List.EXCLUSIVE);
	}
	
	public BaseList(MoFire mofire,String title){
		super(title, List.EXCLUSIVE);
		this.mofire = mofire;
		listeners.addElement(this.mofire);
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
	public void setMIDlet(MIDlet mdl){
		this.midlet = mdl;
	}
}
