package com.modofo.jmeutil;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.microedition.io.Connector;
import javax.microedition.io.file.FileConnection;
import javax.microedition.lcdui.Alert;
import javax.microedition.lcdui.AlertType;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Displayable;
import javax.microedition.midlet.MIDlet;

import com.modofo.mofire.MoFire;

public class Utils {
	public static void alert(MIDlet midlet,String msg, String title, int timeout) {
		Alert alert = new Alert(title);
		alert.setString(msg);
		alert.setType(AlertType.INFO);
		alert.setTimeout(timeout);
		Display.getDisplay(midlet).setCurrent(alert);
	}
	
	public static void info(MIDlet midlet,String msg){
		alert(midlet,msg,"Info",Alert.FOREVER);
	}
	public static void info(MIDlet midlet,String msg,int timeout){
		alert(midlet,msg,"Info",timeout);
	}
	public static void error(MIDlet midlet,String msg){
		alert(midlet,msg,"Error",3000);
	}
	
	public static void confirm(MIDlet m,String msg, String title, int timeout) {
		Alert alert = new Alert(title);
		alert.setString(msg);
		alert.setType(AlertType.CONFIRMATION);
		Display.getDisplay(m).setCurrent(alert);
	}

	/* Cannot compile in CLDC1.1 MIDP2.0, fileConnection not exists
	 * To enable this specify wtk.optionalpda.enabled property to true 
	 * to enable jsr75 in antenna
	 * */
	public static void saveFile(String path, String name, byte[] data) {
		try {
            String url = path + name;
            FileConnection fconn = (FileConnection)Connector.open(url, Connector.READ_WRITE);
            if (!fconn.exists()) {
                fconn.create();
            }
            OutputStream ops = fconn.openOutputStream();
            ops.write(data);
            ops.close();
            fconn.close();
        }
        catch (IOException ioe) {
//            Utils.error(ioe.toString());
        	ioe.printStackTrace();
        }
        catch (SecurityException se) {
            se.printStackTrace();
            //Utils.error(se.toString());
        }
	}

	
	 public static byte[] readFile(String path) {
		byte[] bt = null;
        try {
            FileConnection fc = (FileConnection)Connector.open(path, Connector.READ);
            if(!fc.exists()) {
                System.out.println("File doesn't exist!");
            }
            else {
                int size = (int)fc.fileSize();
                InputStream is = fc.openInputStream();
                bt = new byte[size];
                is.read(bt, 0, size);
            }            
            
        } catch (IOException ioe) {
           // Utils.alert("IOException: "+ioe.getMessage(),"ERROR",Alert.FOREVER);
        	ioe.printStackTrace();
        } catch (IllegalArgumentException iae) {
        	iae.printStackTrace();
            //Utils.alert("IllegalArgumentException: "+iae.getMessage(),"ERROR",Alert.FOREVER);
        }
        return bt;
   }

	public static boolean fileExists(String path, String name) throws IOException {
		FileConnection fc = (FileConnection)Connector.open(path+name, Connector.READ);
		return fc.exists();
	} 
	
	public static void showConfirmDialog(MIDlet midlet,Displayable parent,
			String title, String text,String oklabel,String cancellabel,CommandListener listener) {
        Alert alert = new Alert(title, text, null, AlertType.CONFIRMATION);
        alert.addCommand(new Command (oklabel, Command.OK, 1));
        alert.addCommand(new Command(cancellabel, Command.CANCEL, 1));
        alert.setCommandListener(listener);
        Display.getDisplay(midlet).setCurrent(alert, parent);
    }
}
