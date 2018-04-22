package com.modofo.fileexplorer;

public class FileExplorerEvent {
	public static final short FILE_EXPLORER_EVENT_SELECT_FOLDER=1,FILE_EXPLORER_EVENT_SELECT_FILE=0;
	private short type;
	private String path;
	private boolean isFolder;
	
	public short getType() {
		return type;
	}
	public void setType(short type) {
		this.type = type;
	}
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
	public boolean isFolder() {
		return isFolder;
	}
	public void setFolder(boolean isFolder) {
		this.isFolder = isFolder;
	}
	
}
