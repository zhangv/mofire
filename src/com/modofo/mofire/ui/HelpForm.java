package com.modofo.mofire.ui;

import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Form;

import com.modofo.mofire.MoFire;

public class HelpForm extends BaseForm implements CommandListener{
	Command backCmd;
	Displayable parent;
	MoFire mofire;
	public HelpForm(MoFire mofire,Displayable parent) {
		super(mofire,"Help");
		this.parent = parent;
		backCmd = new Command("Back",Command.BACK,1);
		addCommand(backCmd);
	}

	public void commandAction(Command arg0, Displayable arg1) {
		if(arg0 == backCmd){
			mofire.setCurrent(parent);
		}
	}

}
