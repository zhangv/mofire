package com.modofo.recordr;

import java.io.IOException;

import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Form;
import javax.microedition.lcdui.Image;
import javax.microedition.lcdui.ImageItem;
import javax.microedition.lcdui.Item;
import javax.microedition.lcdui.StringItem;
import javax.microedition.midlet.MIDlet;

import com.modofo.jmeutil.Timer;
import com.modofo.jmeutil.TimerListener;
import com.modofo.mofire.MoFire;
import com.modofo.mofire.MoFireEvent;
import com.modofo.mofire.MoFireEventType;
import com.modofo.mofire.ui.BaseForm;

/**
 *  
 * @author zhangv
 * 
 */
public class RecorderForm extends BaseForm implements CommandListener,
		TimerListener {
	private Command cancelCmd, stopCmd, startCmd;
	// ,playCmd, pauseCmd;
	private Recorder recorder;
	private byte[] audioData;
	private Displayable backForm;
	private StringItem clock;
	private Timer timer;
	private MIDlet midlet;
	public void setBackForm(Displayable back){
		backForm = back;
	}
	public RecorderForm(MIDlet main,Recorder recorder) {
		super(main, "Recorder");
		this.recorder = recorder;
		ImageItem imi = new ImageItem("speak to the phone", null,
				ImageItem.LAYOUT_CENTER, "");
		try {
			imi.setImage(Image.createImage("/mic.jpg"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		append(imi);
		
		//add a new line to put the clock to the nextline - cannot figure out the better way
		StringItem tempItem = new StringItem("","");
		tempItem.setLayout(Item.LAYOUT_NEWLINE_BEFORE);
		append(tempItem);
		
		clock = new StringItem("", "0");
		clock.setLayout(Item.LAYOUT_CENTER);
		append(clock);

		startCmd = new Command("Start", Command.OK, 0);
		stopCmd = new Command("Stop", Command.OK, 0);
		cancelCmd = new Command("Cancel", Command.BACK, 0);
		// pauseCmd = new Command("Pause", Command.OK, 0);

		timer = new Timer();
		timer.setListener(this);
		
		addCommand(startCmd);
		addCommand(cancelCmd);
		setCommandListener(this);
	}

	public void resetClock() {
		updateClock("0");
	}

	public synchronized void updateClock(String s) {
		clock.setText(s);
	}

	public void commandAction(Command arg0, Displayable arg1) {
		if (arg0 == stopCmd) {
			timer.killThread();
			recorder.stopAudioCapture();
			//Display.getDisplay(midlet).setCurrent(backForm);
		} else if (arg0 == cancelCmd) {
			recorder.cancelAudioCapture();
			//Display.getDisplay(midlet).setCurrent(backForm);
		} else if (arg0 == startCmd) {
			recorder.startCapture();
			timer.start();
			removeCommand(startCmd);
			addCommand(stopCmd);
		}
	}

	public void onUpdate(long elapse) {
		updateClock(elapse+"");
	}

}
