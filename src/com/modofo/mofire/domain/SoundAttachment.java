package com.modofo.mofire.domain;

import com.modofo.mofire.Constants;

public class SoundAttachment {
	private String filePath; //when the post stored in local
	private String type = Constants.ATTACHMENT_TYPE_SOUND;
	private byte[] byteData;
	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public byte[] getByteData() {
		return byteData;
	}

	public void setByteData(byte[] byteData) {
		this.byteData = byteData;
	}
	
}
