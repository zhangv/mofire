package com.modofo.fileexplorer;

import java.io.IOException;
import java.util.Enumeration;
import java.util.Vector;

import javax.microedition.io.Connector;
import javax.microedition.io.file.FileConnection;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.List;
import javax.microedition.midlet.MIDlet;

import com.modofo.jmeutil.Utils;

public class FileExplorer extends List implements CommandListener{
	private String currentFolder,selectedFile;
	private FileConnection currentFileConnection;
	private Vector listeners = new Vector();
	private Command selectCmd,backCmd;
	private Displayable from;
	final static String prefix = "";//"file://";
	private MIDlet midlet;
	
	public FileExplorer(String title,String defaultFolder,Displayable from,MIDlet midlet) {
		super(title, List.EXCLUSIVE);
		this.midlet = midlet;
		this.from = from;
		
		currentFolder = defaultFolder;

		selectCmd = new Command("Select",Command.OK,0);
		backCmd = new Command("Back",Command.BACK,1);
		
		addCommand(selectCmd);
		addCommand(backCmd);
		
		setCommandListener(this);
		
		displayFolder();
	}

	public void displayFolder() {
		try {
			this.deleteAll(); //delete all
			
			System.out.println("displaying:"+prefix+currentFolder);
			//append the path to parent folder
			this.append(".",null);
			currentFileConnection = (FileConnection) Connector.open(prefix+currentFolder, Connector.READ);
			Enumeration enu = currentFileConnection.list();
			while (enu.hasMoreElements()) {
				String name = (String) enu.nextElement();
				this.append(name, null);
			}
		} catch (IOException e) { 
			Utils.error(midlet,e.toString());
		}
	}

	public void onSelectFile() {
		FileExplorerEvent fee = new FileExplorerEvent();
		fee.setType(FileExplorerEvent.FILE_EXPLORER_EVENT_SELECT_FILE);
		fee.setFolder(false);
		fee.setPath(selectedFile);
		notifyEvent(fee);
	}

	public void onSelectFolder() {
		//change current folder
		displayFolder();
		
		FileExplorerEvent fee = new FileExplorerEvent();
		fee.setType(FileExplorerEvent.FILE_EXPLORER_EVENT_SELECT_FOLDER);
		fee.setFolder(true);
		fee.setPath(currentFolder);
		notifyEvent(fee);
	}
	
	private void notifyEvent(FileExplorerEvent fee){
		for(int i=0;i<listeners.size();i++){
			FileExplorerListener fel = (FileExplorerListener) listeners.elementAt(i);
			fel.onFileSelected(fee);
		}
	}
	public void addListener(FileExplorerListener l) {
		listeners.addElement(l);
	}
	
	public void commandAction(Command arg0, Displayable arg1) {
		if(arg0 == selectCmd) {
				new Thread(new Runnable(){
					public void run(){
						doSelectCmd();			
					}
				}).start();
		}else if(arg0 == backCmd){
			Display.getDisplay(midlet).setCurrent(from);
		}
	}
	
	private void doSelectCmd(){
		if(this.getSelectedIndex()==-1) return; //nothing selected
		if(this.getSelectedIndex()==0) { //parent folder selected
			//check whether parent folder exists
			if(!isRootFolder(currentFolder)){ // not root folder
				currentFolder = getParentFolder(currentFolder);
				selectedFile = currentFolder;
			}
		}else{
			selectedFile = currentFolder+this.getString(this.getSelectedIndex());
		}
		System.out.println("selected-"+prefix+selectedFile);
		try {
			currentFileConnection = (FileConnection) Connector.open(prefix+selectedFile, Connector.READ);
			if(currentFileConnection.exists()){
				if(currentFileConnection.isDirectory()){
					System.out.println(selectedFile+" is directory");
					if(!selectedFile.endsWith("/")){
						selectedFile+="/"; //append the '/'
					}
					currentFolder = selectedFile;
					onSelectFolder();
				}else{
					onSelectFile();
				}
			}
		} catch (IOException e) {
			//exception of null pointer will occur, if current folder is root
			e.printStackTrace();
		} catch(IllegalArgumentException e){ //"Root is not specified"
			e.printStackTrace();
		}
	}
	
	private boolean isRootFolder(String path){
		//"file:///c:/data/sounds/
		StringBuffer sb = new StringBuffer(path);
		char lastchar = sb.charAt(sb.length()-1);
		if('/' == lastchar){
			char ch = sb.charAt(sb.length()-2);
			if(ch == ':'){
				return true;// - c:/
			}else{
				return false;// - c:/abc/
			}
		}else if(':' == lastchar){ // - c:
			return true;
		}
		return false;
	}
	
	private String getParentFolder(String path){
		StringBuffer sb = new StringBuffer(path);
		char lastchar = sb.charAt(sb.length()-1);
		if(isRootFolder(path)){
			return path;
		}else{
			if('/'==lastchar){
				sb.deleteCharAt(sb.length()-1); //delete the last '/'
			}
			String s = sb.toString();
			s = s.substring(0,s.lastIndexOf('/')+1); //with the last '/'
			return s;
		}
	}
	public void mkdir(String dirname){
		try {
			System.out.println("making folder-"+prefix+currentFolder+dirname);
			FileConnection fc = (FileConnection) Connector.open(prefix+currentFolder+dirname, Connector.READ_WRITE);
			if(!fc.exists()){
				fc.mkdir();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		displayFolder();
	}
	
	public void newFile(String filename){
		try {
			FileConnection fc = (FileConnection) Connector.open(prefix+currentFolder+filename, Connector.READ_WRITE);
			if(!fc.exists()){
				fc.create();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		displayFolder();
	}
}
