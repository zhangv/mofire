package com.modofo.jmeutil;

import java.io.UnsupportedEncodingException;
import java.util.Vector;

public class StringUtils {
	private static HGB2PINYIN hgb2pinyin;
	public static Vector split(String str,char del){
		Vector v = new Vector();
		String tmpStr = str;
		while(tmpStr.indexOf(del)!=-1){
			v.addElement(tmpStr.substring(0,tmpStr.indexOf(del)));
			tmpStr = tmpStr.substring(tmpStr.indexOf(del)+1);
		}
		if(tmpStr.length()>0) v.addElement(tmpStr);
		return v;
	}
	public static String convert(String s,String from,String to) throws UnsupportedEncodingException{
		if(from == null) return new String(s.getBytes(),to);
		else return new String(s.getBytes(from),to);
	}
	
	public static String gb2pinyin(String gb2312){
		return getGb2Pinyin().getAllPY(gb2312);
	}
	
	private static HGB2PINYIN getGb2Pinyin(){
		if(hgb2pinyin==null){
			hgb2pinyin = new  HGB2PINYIN();
		}
		return hgb2pinyin;
	}
}
