package com.modofo.mofire.ui;

import java.util.Vector;

import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Form;
import javax.microedition.lcdui.StringItem;
import javax.microedition.lcdui.TextBox;

import com.modofo.mofire.MoFire;

public class LogForm extends TextBox implements CommandListener {
	Form backForm;
	Command backCmd;
	public LogForm(String title, String text, int maxSize, int constraints) {
		super(title, text, maxSize, constraints);
		backCmd = new Command("Back", Command.BACK, 0);
		addCommand(backCmd);
		setCommandListener(this);
	}
	public void setBackForm(Form backForm){
		this.backForm = backForm;
	}
	private static LogForm instance;

	public Vector logs = new Vector();

	public void log(String log) {
		logs.addElement(log);
		this.insert(log, this.getCaretPosition());
	}

	public void commandAction(Command arg0, Displayable arg1) {
		if(arg0 == backCmd){
			//MoFire.getInstance().setCurrent(backForm);
		}
		
	}
}
