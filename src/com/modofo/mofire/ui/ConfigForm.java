package com.modofo.mofire.ui;

import java.io.IOException;
import java.util.Enumeration;
import java.util.Hashtable;

import javax.microedition.lcdui.Choice;
import javax.microedition.lcdui.ChoiceGroup;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.Displayable;

import com.modofo.mofire.Lang;
import com.modofo.mofire.MoFire;
import com.modofo.mofire.domain.Config;

public class ConfigForm extends BaseForm {
	private ChoiceGroup langChc;
	private Command saveCmd;
	private Hashtable langs;
	private Displayable backForm;
	public ConfigForm(MoFire mofire){
		super(mofire,"Config");
		langChc = new ChoiceGroup("Language",Choice.EXCLUSIVE);
		
		try {
			langs = Lang.getLangs();
			Enumeration em = langs.keys();
			while(em.hasMoreElements()){
				String langV = (String) em.nextElement();
				langV = new String(langV.getBytes(),mofire.getSystemEncoding());
				//String langL = (String) langs.get(langV);
				langChc.append(langV, null);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		langChc.setSelectedIndex(0, true);
		
		saveCmd	= new Command("Save", Command.OK, 1);
		append(langChc);
		addCommand(saveCmd);
		setCommandListener(this);
	}

	public void commandAction(Command c, Displayable d) {
		if(c == saveCmd){
			Config cfg = new Config();
			String langV = langChc.getString(langChc.getSelectedIndex());
			cfg.setLang((String) langs.get(langV));
			try {
				MoFire.getMgr().saveConfig(cfg);
			} catch (Exception e) {
				e.printStackTrace();
			}
			mofire.setCurrent(backForm);
			mofire.initLang(cfg.getLang());
			mofire.initUI();
		}
	}

}
