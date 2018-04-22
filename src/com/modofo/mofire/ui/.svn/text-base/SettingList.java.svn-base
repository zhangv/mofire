package com.modofo.mofire.ui;

import java.util.Vector;

import javax.microedition.lcdui.Alert;
import javax.microedition.lcdui.AlertType;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Displayable;
import javax.microedition.midlet.MIDlet;
import javax.microedition.rms.RecordStoreException;
import javax.microedition.rms.RecordStoreFullException;
import javax.microedition.rms.RecordStoreNotFoundException;

import com.modofo.jmeutil.Utils;
import com.modofo.mofire.MoFire;
import com.modofo.mofire.MoFireEvent;
import com.modofo.mofire.MoFireEventType;
import com.modofo.mofire.MoFireListener;
import com.modofo.mofire.Res;
import com.modofo.mofire.call.CallFactory;
import com.modofo.mofire.call.ValidateCall;
import com.modofo.mofire.domain.Setting;

public class SettingList extends BaseList implements CommandListener,MoFireListener{
	private Vector settings;
	private Command okCmd,editCmd,newCmd,exitCmd,deleteCmd,validateCmd,
		helpCmd;
	private Setting selectedSetting;
	public SettingList(MoFire midlet) {
		super(midlet,"Settings");
		okCmd = 		new Command("Use", Command.OK, 1);
		editCmd = 		new Command("Edit", Command.SCREEN, 2);
		newCmd = 		new Command("New", Command.SCREEN, 3);
		exitCmd = 		new Command("Exit", Command.EXIT, 1);
		deleteCmd = 	new Command("Delete", Command.SCREEN, 4);
		validateCmd = 	new Command("Validate Setting", Command.SCREEN, 5);
		helpCmd = 		new Command("Help",Command.SCREEN,6);
		
		addCommand(editCmd);
		addCommand(newCmd);
		addCommand(exitCmd);
		addCommand(deleteCmd);
		addCommand(okCmd);
		addCommand(validateCmd);
		addCommand(helpCmd);
		
		setCommandListener(this);
	}
	public Vector getSettings() {
		return settings;
	}
	public void setSettings(Vector settings) {
		this.settings = settings;
		if(this.settings == null) return;
		this.deleteAll();
		int selectedIdx = 0;
		int settingsize = settings.size();
		for(int i=0;i<settingsize;i++){
			Setting set = (Setting)settings.elementAt(i);
			this.append(set.getName(),null);
			if(selectedSetting!=null
					&& set.getId() == selectedSetting.getId()) selectedIdx = i;
		}
		//set selected back, if first time, make "default" selected
		//if "default" is just deleted, make the first one selected
		if(settings.size()>0 && selectedIdx < settings.size())
		this.setSelectedIndex(selectedIdx, true);
	}

	
	public void commandAction(Command arg0, Displayable arg1) {
		if(arg0 == editCmd) doEditCmd();
		else if(arg0 == deleteCmd) doDeleteCmd();
		else if(arg0 == okCmd) doOkCmd();
		else if(arg0 == newCmd) doNewCmd();
		else if(arg0 == exitCmd) showExitConfirmation();
		else if(arg0 == validateCmd) doValidate();
	}
	
	private void showExitConfirmation() {
		Alert alert = new Alert("Confirmation", "Are you sure to exit the application?", null, AlertType.CONFIRMATION);
        alert.addCommand(new Command ("Yes", Command.OK, 1));
        alert.addCommand(new Command("No", Command.CANCEL, 1));
        alert.setCommandListener(new CommandListener() {
            public void commandAction(Command c, Displayable d) {
                if (c.getLabel().equals("Yes")) {
                	doExitCmd();
                }
                if (c.getLabel().equals("No")) {
                	Display.getDisplay(midlet).setCurrent(SettingList.this);
                }
            }
        });
        Display.getDisplay(midlet).setCurrent(alert, this);
	}
	private void doExitCmd() {
		mofire.exit();
	}
	private void doNewCmd() {
		mofire.getSettingForm().setSetting(new Setting());
		mofire.showSettingForm();
	}
	private void doDeleteCmd() {
		if(settings.size()==0) return;
		if(getSelectedIndex()<0 || getSelectedIndex()> settings.size()) return;
		showConfirmation("Confirmation",Res.getString("setting.delete.confirm"));
		
	}
	private void showConfirmation(String title, String text) {
		Alert alert = new Alert(title, text, null, AlertType.CONFIRMATION);
        alert.addCommand(new Command ("Yes", Command.OK, 1));
        alert.addCommand(new Command("No", Command.CANCEL, 1));
        alert.setCommandListener(new CommandListener() {
            public void commandAction(Command c, Displayable d) {
                if (c.getLabel().equals("Yes")) {
                	selectedSetting =(Setting) settings.elementAt(SettingList.this.getSelectedIndex());
            		try {
            			MoFire.getMgr().delSetting(selectedSetting.getId());
            			refreshList();
            		} catch (Exception e) {
            			e.printStackTrace();
            		} 
            		//set selected setting to null
            		selectedSetting = null;
                }
                mofire.setCurrent(SettingList.this);
            }
        });
        mofire.getDisplay().setCurrent(alert, this);
	}
	private void refreshList() throws RecordStoreFullException, RecordStoreNotFoundException, RecordStoreException {
		settings = MoFire.getMgr().getSettings(null);
		setSettings(settings);
	}
	private void doEditCmd() {
		if(settings.size()==0) return;
		if(getSelectedIndex()<0 || getSelectedIndex()> settings.size()) return;
		selectedSetting =(Setting) settings.elementAt(this.getSelectedIndex());
		mofire.getSettingForm().setSetting(selectedSetting);
		mofire.showSettingForm();
	}
	private void doOkCmd(){
		if(settings.size()==0) return;
		if(getSelectedIndex()<0 || getSelectedIndex()> settings.size()) return;
		selectedSetting =(Setting) settings.elementAt(this.getSelectedIndex());
		mofire.getPostForm().setCurSetting(selectedSetting);
		mofire.showPostForm();
	}
	public void doValidate(){
		//check if the xmlrpc setting is reachable
		if(settings.size()==0) return;
		if(getSelectedIndex()<0 || getSelectedIndex()> settings.size()) return;
		selectedSetting =(Setting) settings.elementAt(this.getSelectedIndex());
		ValidateCall vc = (ValidateCall) CallFactory.getValidateCall(selectedSetting);
		vc.addListener(this);
		vc.go();
	}
	public void onEvent(MoFireEvent evt) {
		if(evt.getType() == MoFireEventType.SETTING_VALIDATE_ERROR){
			Utils.error(midlet,"The url for xmlrpc.php is not valid");
		}else if(evt.getType() == MoFireEventType.SETTING_VALIDATE_SUCCESS){
			Utils.info(midlet,evt.getMessage());
		}
	}
}
