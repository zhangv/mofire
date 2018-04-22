package com.modofo.camera;

import javax.microedition.lcdui.Canvas;
import javax.microedition.lcdui.Graphics;
import javax.microedition.media.MediaException;
import javax.microedition.media.control.VideoControl;

public class VideoCanvas extends Canvas {
	
	public VideoCanvas(VideoControl videoControl) {
		init(videoControl);
	}
	
	public VideoCanvas() {
	}

	public void init(VideoControl videoControl) {
		int width = getWidth();
		int height = getHeight();
		
		videoControl.initDisplayMode(VideoControl.USE_DIRECT_VIDEO, this);
		try {
			videoControl.setDisplayLocation(2, 2);
			videoControl.setDisplaySize(width - 4, height - 4);
		} catch (MediaException me) {
		}
		videoControl.setVisible(true);
	}

	public void paint(Graphics g) {
		int width = getWidth();
		int height = getHeight();

		g.setColor(0x00ff00);
		g.drawRect(0, 0, width - 1, height - 1);
		g.drawRect(1, 1, width - 3, height - 3);
	}
}