package com.modofo.mofire.ui;

import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.TextField;

import com.modofo.jmeutil.Utils;
import com.modofo.mofire.DuplicateSettingException;
import com.modofo.mofire.MoFire;
import com.modofo.mofire.MoFireEvent;
import com.modofo.mofire.MoFireEventType;
import com.modofo.mofire.Res;
import com.modofo.mofire.domain.Setting;

public class SettingForm extends BaseForm implements CommandListener{
	private Command saveCmd,cancelCmd;
	private TextField urlFld,userFld,passFld;
	
	private Setting setting=new Setting();
	
	public SettingForm(MoFire midlet,String title) {
		super(midlet,title);
		urlFld=  new TextField("URL", "zhangv.com/wordpress/xmlrpc.php",200,TextField.URL);
		userFld= new TextField("User", "zhangv",200,TextField.ANY);
		passFld= new TextField("Password", "woshi1630",200,TextField.PASSWORD);
		
		append(urlFld);
		append(userFld);
		append(passFld);
		
		saveCmd = new Command("Save", Command.OK, 1);
		cancelCmd = new Command("Cancel", Command.BACK, 0);
		
		addCommand(saveCmd);
		addCommand(cancelCmd);
		
		setCommandListener(this);
		addListener(mofire); //add default listener
	}

	public void commandAction(Command arg0, Displayable arg1) {
		if(arg0 == saveCmd) doSaveCmd();
		else if(arg0 == cancelCmd) doCancelCmd();
	}

	private void doCancelCmd() {
		mofire.showPostForm();
	}

	private void doSaveCmd() {
		String tempurl = urlFld.getString().trim();
		if(!tempurl.startsWith("http://"))
			tempurl = "http://"+tempurl;
		setting.setUrl(tempurl);
		setting.setUser(userFld.getString().trim());
		setting.setPwd(passFld.getString().trim());
		
		String status=Res.getString("setting.save.success");
		boolean saved = true;
		try {
			int newid = MoFire.getMgr().saveSetting(setting);
			setting.setId(newid);
		} catch (DuplicateSettingException e) {
			status = e.getMessage();
			saved=false;
			e.printStackTrace();
		} catch (Exception e) {
			status = Res.getString("setting.save.error");
			saved=false;
			e.printStackTrace();
		} 
		if(saved){
			MoFireEvent evt = new MoFireEvent();
			evt.setType(MoFireEventType.SETTING_SAVE_SUCCESS);
			evt.setObj1(setting);
			fireEvent(evt);
		}else{
			Utils.alert(midlet,status, "INFO", 3000);
		}
	}

	public Setting getSetting() {
		return setting;
	}


	public void setSetting(Setting setting) {
		this.setting = setting;
		userFld.setString(setting.getUser());
		urlFld.setString(setting.getUrl());
		passFld.setString(setting.getPwd());
	}
}
