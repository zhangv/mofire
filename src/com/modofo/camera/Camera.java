package com.modofo.camera;

import java.io.IOException;
import java.util.Vector;

import javax.microedition.lcdui.Canvas;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Form;
import javax.microedition.lcdui.Item;
import javax.microedition.media.Manager;
import javax.microedition.media.MediaException;
import javax.microedition.media.Player;
import javax.microedition.media.control.VideoControl;
import javax.microedition.midlet.MIDlet;

/**
 * Camera wrapper
 * 
 * @author zhangv
 * 
 */
public class Camera implements CommandListener {
	private MIDlet midlet;
	private Displayable parent;
	
	private Player videoPlayer;
	private VideoControl videoControl;
	
	// redesign
	private String name; // the name in the sys props
	private String mode; // video/photo mode
	private VideoClip videoClip; // video clip captured
	private long maxLength; // the max length of video clip(in seconds)
	private long maxSize; // the max file size of the video or photo
	private String encoding =  "encoding=jpeg"; //&width=800&height=600";
	private int width = 800, height = 600; // the view size
	private Photo photo; // photo captured
	
	//auto back to the parent displayable after a successful capture
	private boolean autoBack = true;
	private Vector cameraListeners = new Vector();
	
	public Camera(Displayable parent){
		this(parent,"","",2048);
	}
	public Camera(Displayable parent,String name, String mode, long maxLen) {
		this.parent = parent;
		this.name = name;
		this.mode = mode;
		this.maxLength = maxLen;
	}
	
	

	public void addListener(CameraListener listener){
		cameraListeners.addElement(listener);
	}
	
	public void removeListener(CameraListener listener){
		cameraListeners.removeElement(listener);
	}
	
	public void captureVideo() throws MediaException {
		videoControl.setVisible(false);
		videoClip = new VideoClip(videoControl.getSnapshot(encoding));
		videoPlayer.close();
		videoPlayer = null;
		videoControl = null;
	}

	public void capturePhoto() throws MediaException {
		videoControl.setVisible(false);
		photo = new Photo(videoControl.getSnapshot(encoding));
		
		destroy();
		
		//notify lisetners
		CameraEvent ce = new CameraEvent();
		ce.setType(CameraEvent.CAMERA_EVENT_TYPE_PHOTO);
		ce.setBytes(photo.getPhoto());
		
		if(autoBack){
			Display.getDisplay(midlet).setCurrent(parent);
		}
		//TODO consider notify in a thread?
		notifyListeners(ce);
	}
	
	private void notifyListeners(CameraEvent ce){
		for(int i=0;i<cameraListeners.size();i++){
			CameraListener l = (CameraListener) cameraListeners.elementAt(i);
			l.photoCaptured(ce);
		}
	}

	private Command backCmd, captureCmd;
	private Canvas videoCanvas;

	public void start(MIDlet midlet) throws MediaException, IOException {
		this.midlet = midlet;
		backCmd = new Command("Back", Command.BACK, 0);
		captureCmd = new Command("Capture", Command.OK, 0);
		//must re-init for the second capture, or the command cannot append to the form
		videoCanvas = new VideoCanvas();
		videoCanvas.addCommand(backCmd);
		videoCanvas.addCommand(captureCmd);
		videoCanvas.setCommandListener(this);
			
		//"devcam0","devcam1", "video", "image" --all not work!!
		videoPlayer = Manager.createPlayer("capture://video");
		videoPlayer.realize();
		videoControl = (VideoControl) videoPlayer.getControl("VideoControl");
		((VideoCanvas) videoCanvas).init(videoControl);
		Display.getDisplay(midlet).setCurrent(videoCanvas);
		videoPlayer.start();
	}
	
	public void start(Form form) throws MediaException, IOException {
		videoPlayer = Manager.createPlayer("capture://video");
		videoPlayer.realize();
		videoControl = (VideoControl) videoPlayer.getControl("VideoControl");
		if (videoControl != null) {
			form.append((Item)videoControl.initDisplayMode(VideoControl.USE_GUI_PRIMITIVE, null));
		}
		videoPlayer.start();
	}

	public void commandAction(javax.microedition.lcdui.Command c, Displayable d) {
		if (c == captureCmd) {
			doCapture();
		}
		if(c == backCmd){
			destroy();
			Display.getDisplay(midlet).setCurrent(parent);
		}
	}
	
	public void doCapture() {
		new Thread() {
			public void run() {
				try {
					capturePhoto();
				} catch (MediaException e) {
					e.printStackTrace();
				}
			}
		}.start();	
	}
	void destroy(){
		videoPlayer.deallocate();
		videoPlayer.close();
		videoPlayer = null;
		videoControl = null;
	}

}
