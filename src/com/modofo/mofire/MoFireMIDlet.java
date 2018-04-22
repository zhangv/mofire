package com.modofo.mofire;

import javax.microedition.midlet.MIDlet;
import javax.microedition.midlet.MIDletStateChangeException;

public class MoFireMIDlet extends MIDlet {
	private MoFire mofire;
	
	public MoFireMIDlet() {
		mofire = new MoFire(this);
	}

	protected void destroyApp(boolean unconditional)
			throws MIDletStateChangeException {
	}

	protected void pauseApp() {
	}

	protected void startApp() throws MIDletStateChangeException {
		mofire.startApp();
	}
	
	public void exit(){
		try {
			destroyApp(true);
		} catch (MIDletStateChangeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		notifyDestroyed();
	}
}
