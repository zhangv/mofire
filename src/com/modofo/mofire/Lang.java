package com.modofo.mofire;

import java.io.IOException;
import java.io.InputStream;
import java.util.Hashtable;

public class Lang {
	public static String CN = "Chinese",EN = "English";
	private static Hashtable langs = null;
	public static String[] langSupported = {"Chinese","English"}; 
	
	public static Hashtable getLangs() throws IOException{
		if(langs == null){
			langs  = new Hashtable();
			InputStream in = Lang.class.getResourceAsStream("/langs.properties");
			byte c;
			StringBuffer line = new StringBuffer();
			while ((c = (byte) in.read()) != -1) {
				if ((c != '\n') && (c != '\r')) {
					line.append(new String(new byte[] { c }));
				}else if(c!='\r') {//if windows, ignore the \r after \n
					String aux = line.toString();
					String key = aux.substring(0, aux.indexOf("=")).trim();
					String message = aux.substring(aux.indexOf("=") + 1, aux
							.length());
					line = new StringBuffer();
					langs.put(key, message);
				}
			}
		}
		return langs;
	}
}
