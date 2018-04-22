package com.modofo.mofire;

import java.io.IOException;
import java.util.Vector;

import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.TextField;
import javax.microedition.midlet.MIDlet;
import javax.microedition.midlet.MIDletStateChangeException;

import com.modofo.jmeutil.Utils;
import com.modofo.mofire.call.CallFactory;
import com.modofo.mofire.call.ValidateCall;
import com.modofo.mofire.domain.Config;
import com.modofo.mofire.domain.Post;
import com.modofo.mofire.domain.Setting;
import com.modofo.mofire.ui.ConfigForm;
import com.modofo.mofire.ui.LogForm;
import com.modofo.mofire.ui.PostForm;
import com.modofo.mofire.ui.PostList;
import com.modofo.mofire.ui.SettingForm;
import com.modofo.mofire.ui.SettingList;

public class MoFire implements MoFireListener {
	
	private static MIDlet midlet;
	private Display display;
	private static MoFireMgr mgr;

	private PostForm postForm;
	private SettingList settingList;
	private PostList postList;
	private SettingForm settingForm;

	private Vector settings = new Vector();
	private LogForm logForm;

	private boolean cameraSupported = false;
	private boolean recorderSupported = false;
	private String systemEncoding = "iso-8859-1";
	private Config config = null;
	private ConfigForm configForm;
//	private Twitter twitter;
	private boolean isFirstTime = false;

	public MoFire(MIDlet midlet) {
		this.midlet =  midlet;
		display = Display.getDisplay(midlet);
		mgr = new MoFireMgr();
//		twitter = new Twitter();
		// check whether camera is supported on the phone
		String videoSupport = System.getProperty("supports.video.capture");
		cameraSupported = "true".equalsIgnoreCase(videoSupport);
		
		String recordingSupport = System.getProperty("supports.recording");
		recorderSupported = "true".equalsIgnoreCase(recordingSupport);
		
		String sysencoding = System.getProperty("microedition-encoding");
		if(sysencoding!=null) systemEncoding = sysencoding;
	}
	
	protected void startApp() throws MIDletStateChangeException {
		initLang("EN");
		initUI();
	}
	
	public void initLang(String lang){
		// init language resources
		try {
			Res.init("Resources_"+lang);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void initUI() {
		postForm = new PostForm(this, "");
		settingForm = new SettingForm(this, "");
		loadSettings();
		// check if there's any defaultsetting
		Setting tmp = null;
		Setting defaultSetting = null;
		int settingCount = settings.size();
		if (settingCount == 0) {
			showSettingForm();
			return;
		} else {
			for (int i = 0; i < settingCount; i++) {
				tmp = (Setting) settings.elementAt(i);
				if (tmp.isDefault()) {
					defaultSetting = tmp;
					break;
				}
			}
		}
		if (defaultSetting == null) {
			showSettingForm();
		} else {
			postForm.setTitle(defaultSetting.getName());
			postForm.setCurSetting(defaultSetting);
			showPostForm();
		}
	}

	private void loadSettings() {
		try {
			settings = mgr.getSettings(null);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void setCurrent(Displayable nextDisplayable) {
		display.setCurrent(nextDisplayable);
	}

	public PostForm getPostForm() {
		return postForm;
	}

	public SettingList getSettingList() {
		if (settingList == null) {
			settingList = new SettingList(this);
		}
		return settingList;
	}

	public PostList getPostList() {
		if (postList == null) {
			postList = new PostList(this);
		}
		return postList;
	}

	public SettingForm getSettingForm() {
		return settingForm;
	}

	public void exit() {
		((MoFireMIDlet)midlet).exit();
	}

	public static MoFireMgr getMgr() {
		return mgr;
	}

	public boolean isCameraSupported() {
		return cameraSupported;
	}

	public Display getDisplay() {
		return this.display;
	}

	public LogForm getLogForm() {
		if (logForm == null) {
			logForm = new LogForm("", "", 20000, TextField.ANY);
		}
		return logForm;
	}

	public void showPostList() {
		Vector posts = new Vector();
		try {
			posts = mgr.getPosts(null);
		} catch (Exception e) {
			e.printStackTrace();
		}
		getPostList().setPosts(posts);
		setCurrent(getPostList());
	}

	public void showSettingList() {
		try {
			settings = mgr.getSettings(null);
		} catch (Exception e) {
			e.printStackTrace();
		}

		/*
		 * Setting setting = new Setting(); setting.setName("asd");
		 * setting.setUrl("http://zhangv.com/wordpress/xmlrpc.php");
		 * setting.setUser("test"); setting.setPwd("asdf1234");
		 * setting.setDefault(true); settings.addElement(setting);
		 */
		getSettingList().setSettings(settings);
		setCurrent(getSettingList());
	}

	public void showPostForm() {
		setCurrent(postForm);
	}

	public void onEvent(MoFireEvent evt) {
		if (evt.getType() == MoFireEventType.SETTING_SAVE_SUCCESS) {
			// showconfirm dialog - whether validate the setting now
			final Setting setting = (Setting) evt.getObj1();
			Utils.showConfirmDialog(midlet,getSettingForm(),"INFO", "Validate it now?", "Yes", "No", new CommandListener(){
				public void commandAction(Command c, Displayable d) {
					if(c.getCommandType()==Command.OK){
						ValidateCall vc = (ValidateCall) CallFactory.getValidateCall(setting);
						vc.addListener(MoFire.this);
						vc.go();
					}
					getPostForm().setCurSetting(setting);
					showPostForm();
				}
			});
		}else if(evt.getType() == MoFireEventType.SETTING_VALIDATE_ERROR) {
			Utils.error(midlet, evt.getMessage());
		}else if(evt.getType()== MoFireEventType.SETTING_VALIDATE_SUCCESS){
			Setting setting = (Setting) evt.getObj1();
			try {
				MoFire.getMgr().saveSetting(setting);
			} catch (Exception e) {
				Utils.error(midlet, "MoFire.onEvent - e"+e.toString());
			}
			this.getPostForm().setCurSetting(setting);
			this.showPostForm();
			Utils.info(midlet, "Setting is OK!");
		}else if(evt.getType() == MoFireEventType.POST_SUCCESS){
			Utils.info(midlet,evt.getMessage(),3000);
			/*
			Post post = (Post) evt.getObj1();
			try {
				twitter.postTweet(post.getTitle());
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			*/
		} 
	}

	public void showSettingForm() {
		setCurrent(getSettingForm());
	}
	
	public static MIDlet getMidlet(){
		return midlet;
	}

	public String getSystemEncoding() {
		return systemEncoding;
	}

	public void setSystemEncoding(String systemEncoding) {
		this.systemEncoding = systemEncoding;
	}
}
