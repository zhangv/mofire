package com.modofo.camera;

public class VideoClip {
	private byte[] videoData;
	public VideoClip(byte[] bytes) {
		videoData = bytes;
	}
	
	public byte[] getVideoData(){
		return this.videoData;
	}

}
