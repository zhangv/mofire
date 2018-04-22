package com.modofo.fileexplorer;

import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.List;
import javax.microedition.midlet.MIDlet;
import javax.microedition.midlet.MIDletStateChangeException;

import com.modofo.mofire.Constants;

public class FileExplorerTest extends MIDlet {

	public FileExplorerTest() {
		// TODO Auto-generated constructor stub
	}

	protected void destroyApp(boolean unconditional)
			throws MIDletStateChangeException {
		// TODO Auto-generated method stub

	}

	protected void pauseApp() {
		// TODO Auto-generated method stub

	}

	protected void startApp() throws MIDletStateChangeException {
		String initfolder = System.getProperty(Constants.PHOTO_DIRECTORY);
		FileExplorer fe = new FileExplorer("test",initfolder,null,this );
		fe.mkdir("test");
		fe.newFile("testfile.txt");
		Display.getDisplay(this).setCurrent(fe);
	}

}
