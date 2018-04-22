package com.modofo.mofire.domain;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;

import com.modofo.mofire.Lang;

public class Setting implements Storable{
	private String name="";
	private String url="",user="",pwd="";
	private int id ;
	private boolean isDefault=true;
	private String lang = Lang.CN;
	//use a pattern to easily handle the storing
	//category will be stored as a string with pattern: id1,name1:id2,name2....idx,namex
	private String categories="";
	
	public boolean isDefault() {
		return isDefault;
	}
	public void setDefault(boolean isDefault) {
		this.isDefault = isDefault;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getUser() {
		return user;
	}
	public void setUser(String user) {
		this.user = user;
	}
	public String getPwd() {
		return pwd;
	}
	public void setPwd(String pwd) {
		this.pwd = pwd;
	}

	public byte[] toByteArray() {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream os = new DataOutputStream(baos);
        try {
        	os.writeInt(id);
            os.writeUTF(name);
            os.writeUTF(url);
            os.writeUTF(user);
            os.writeUTF(pwd);
            os.writeBoolean(isDefault);
            if(null!=categories){
            	os.writeUTF(categories);
            }
            os.flush();
            os.close();
            return baos.toByteArray();
        } catch (Exception e) {
            e.printStackTrace();
            return new byte[0];
        }
    }
	
	public boolean fromByteArray(byte[] data) {
        try {
            DataInputStream is = new DataInputStream(new ByteArrayInputStream(data));
            id = is.readInt();
            name = is.readUTF();
            url = is.readUTF();
            user = is.readUTF();
            pwd = is.readUTF();
            isDefault = is.readBoolean();
            categories = is.readUTF();
            return true;
        } catch (Exception e) {
        	e.printStackTrace();
        	return false;
        }
    }
	public String getCategories() {
		return categories;
	}
	public void setCategories(String categories) {
		this.categories = categories;
	}
	public String toString(){
		StringBuffer sb = new StringBuffer();
		sb.append(id).append(':')
			.append(name).append(':').append(url).append(':').append(isDefault);
		return sb.toString();
	}
}
