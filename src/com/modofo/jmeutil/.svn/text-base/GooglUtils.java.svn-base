package com.modofo.jmeutil;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

import javax.microedition.io.Connector;
import javax.microedition.io.HttpConnection;

import org.json.me.JSONObject;

public class GooglUtils {
	private static String GOOGL_API_ENDPOINT="http://goo.gl/api/shorten?security_token=null&url=";
	public static String shorten(String url) throws Exception{
		HttpConnection con = null;
		InputStream in = null;
		OutputStream out = null;
		ByteArrayOutputStream bos = null;
		OutputStreamWriter osw = null;
		InputStreamReader isr = null;
		byte[] request;
		int messageLength;

		try {
			bos = new ByteArrayOutputStream();
			osw = new OutputStreamWriter(bos, "UTF-8");
			osw.flush();

			request = bos.toByteArray();
			messageLength = request.length;

			con = (HttpConnection) Connector.open(GOOGL_API_ENDPOINT+url, Connector.READ_WRITE);
			con.setRequestMethod(HttpConnection.POST);
			con.setRequestProperty("Content-Length", Integer
					.toString(messageLength));
			con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");
			out = con.openOutputStream();
			out.write(request);
			in = con.openInputStream();
			isr = new InputStreamReader(in,"UTF-8");
			
			char[] buf = new char[256];
			int rsize = 0;
			StringBuffer sb = new StringBuffer();
			while((rsize=isr.read(buf))>0){
				sb.append(buf,0,rsize);
			}
			System.out.println(sb.toString());
			
			String shorturl =null;
/*			JSONObject jo = new JSONObject(sb.toString());
			String shorturl = (String) jo.get("short_url");
			System.out.println(shorturl);*/
			return shorturl;
			
		} catch (Exception x) {
			throw x;
		} finally {
			try {
				if (con != null)
					con.close();
				if (in != null)
					in.close();
				if (out != null)
					out.close();
			} catch (IOException ioe) {
				ioe.printStackTrace();
			}
		}
	}
}
