package com.modofo.mofire.domain;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class Config implements Storable{
	private int id = 0;
	private String lang = "EN"; //default english

	public boolean fromByteArray(byte[] ba) {
		DataInputStream is = new DataInputStream(new ByteArrayInputStream(ba));
        try {
        	lang = is.readUTF();
            return true;
        } catch (Exception e) {
        	e.printStackTrace();
        	return false;
        }finally{
        	if(is!=null)
				try {
					is.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
        }
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
	}

	public byte[] toByteArray() {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream os = new DataOutputStream(baos);
        byte[] reval=null;
        try {
            os.writeUTF(lang);
            reval = baos.toByteArray();
        } catch (Exception e) {
            e.printStackTrace();
        } finally{
        	if(os!=null)
				try {
					os.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
        	if(baos!=null)
				try {
					baos.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
        }
        return reval;
	}

	public String getLang() {
		return lang;
	}

	public void setLang(String lang) {
		this.lang = lang;
	}
	
}
