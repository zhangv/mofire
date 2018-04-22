package com.modofo.mofire;

import java.io.IOException;
import java.io.InputStream;
import java.util.Hashtable;

public class Res {
	private static Hashtable map = new Hashtable();
	private static Res instance = null;

	private Res(final String fileName) throws IOException {
		StringBuffer completeFileName = new StringBuffer();
		completeFileName.append("/");
		completeFileName.append(fileName);
//		completeFileName.append("_");
//		completeFileName.append(System.getProperty("microedition.locale"));
		completeFileName.append(".properties");

		InputStream in = this.getClass().getResourceAsStream(
				completeFileName.toString());
		/*if (in == null) {
			String x = completeFileName.toString();
			completeFileName = new StringBuffer(x.substring(0, x.indexOf(System
					.getProperty("microedition.locale"))));
			completeFileName.append("en-US.properties");
			in = this.getClass().getResourceAsStream(
					completeFileName.toString());
		}*/

		//DataInputStream din = new DataInputStream(in);
		
		byte c;
		StringBuffer line = new StringBuffer();
		while ((c = (byte) in.read()) != -1) {
			if ((c != '\n') && (c != '\r')) {
				line.append(new String(new byte[] { c }));
			}else if(c!='\r') {//if windows, ignore the \r after \n
				String aux = line.toString().trim();
				//if blank row, just ignore it
				if(aux.length()==0 || aux.startsWith("#")){ // it's a comment liine
					
				}else{
					String key = aux.substring(0, aux.indexOf("=")).trim();
					String message = aux.substring(aux.indexOf("=") + 1, aux
							.length());
					line = new StringBuffer();
					this.map.put(key, message);
				}
			}
		}
	}

	public static final void init(final String fileName) throws IOException {
		if (instance == null) {
			instance = new Res(fileName);
		}
	}

	public static final Res getInstance() {
		return instance;
	}

	public static String getString(String key) {
		String val = (String) map.get(key);
		if(val==null) return key;
		return val;
	}

	public static String get(String key) {
		String s = getString(key);
		/*String r = "";
		try {
			System.out.println(System.getProperty("microedition.locale"));
			r += new String(s.getBytes("utf-8"),"iso-8859-1");
			r += new String(s.getBytes("iso-8859-1"),"iso-8859-1");
			r += new String(s.getBytes("gb2312"),"iso-8859-1");
			//r += new String(s.getBytes("utf-8"),"utf-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}*/
		return s;
	}
}