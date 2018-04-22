package com.modofo.jmeutil;

import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;

public class ImageUtils {
	public static Image rescaleImage(Image image) {
		int sourceWidth = image.getWidth();
		int sourceHeight = image.getHeight();
		double factor = 1d;
		int maxEdge = 90; //90*90 square
		int longerEdge = 1;
		if(sourceWidth<=maxEdge && sourceHeight<=maxEdge){
			factor = 1d;
		}else{
			if(sourceWidth>sourceHeight) longerEdge = sourceWidth;
			else longerEdge = sourceHeight;
			factor = (double)((double)longerEdge / (double)maxEdge);
		}
		int width = (int) (sourceWidth / factor);
		int height = (int) (sourceHeight / factor);
		//System.out.println("width:"+width+",height:"+height+",factor:"+factor);
		Image thumb = Image.createImage(width, height);
		Graphics g = thumb.getGraphics();
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				g.setClip(x, y, 1, 1);
				int dx = x * sourceWidth / width;
				int dy = y * sourceHeight / height;
				g.drawImage(image, x - dx, y - dy, Graphics.LEFT
								| Graphics.TOP);
			}
		}
		image = null;
		Image immutableThumb = Image.createImage(thumb);
		return immutableThumb;
	}
}
