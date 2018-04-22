package com.modofo.mofire.call;

import java.util.Hashtable;
import java.util.Vector;

import org.kxmlrpc.XmlRpcClient;

import com.modofo.mofire.MoFireEvent;
import com.modofo.mofire.MoFireEventType;
import com.modofo.mofire.XmlRpcClientFactory;
import com.modofo.mofire.domain.Setting;

public class ValidateCall extends AbstractCall{
	private String targetFunc = "wp.getCategories";
	private Setting setting;
	private Vector response;
	private String[] retryGuess = {"/wordpress/xmlrpc.php","/xmlrpc.php"};
	
	ValidateCall(XmlRpcClient xmlRpc,Setting setting) {
		this.setting = setting;
	}

	public void run(){
		if(xmlRpc == null){//on demand
			xmlRpc = XmlRpcClientFactory.getXmlRpcClient(setting.getUrl());
		}
		Vector params = new Vector();
		params.addElement(new Integer(1));
		params.addElement(setting.getUser());
		params.addElement(setting.getPwd());
		
		boolean valid = false;
		boolean guessed = false;
		try {
			Object obj = xmlRpc.execute(targetFunc, params);
			System.out.println(obj);
			response = (Vector)obj;
			System.out.println("validate response:"+response);
			valid = true;
		} catch (Throwable e) {
			//if fails, will null pointer
			try{
				guessed = tryGuess();
			}catch(Exception ee){
				valid = false;
			}
		}
		
		MoFireEvent evt = new MoFireEvent();
		if(!valid && !guessed){
			evt.setType(MoFireEventType.SETTING_VALIDATE_ERROR);
			evt.setMessage("Setting is not validate!");
		}else if(valid || guessed){
			evt.setType(MoFireEventType.SETTING_VALIDATE_SUCCESS);
			evt.setMessage("Setting validated!");
			StringBuffer catestr = new StringBuffer(); 
			for(int i = 0;i<response.size();i++){
				Hashtable ht = (Hashtable) response.elementAt(i);
				catestr.append((String) ht.get("categoryId"));
				catestr.append(",");
				String name = (String) ht.get("categoryName");
				
				System.out.println("category name:"+name);
				catestr.append(name);
				catestr.append(";");
			}

			if(catestr.length()>0){
				catestr.deleteCharAt(catestr.length()-1);
			}
			setting.setCategories(catestr.toString());
			evt.setObj1(setting);
		}
		fireEvent(evt);
	}
	
	private boolean tryGuess() {
		boolean guessed = false;
		for(int i=0;i<retryGuess.length;i++){
			guessed = tryGuess(retryGuess[i]);
			if(guessed) {
				setting.setUrl(setting.getUrl()+retryGuess[i]);
				break;
			}
		}
		return guessed;
	}

	private boolean tryGuess(String guess){
		xmlRpc = XmlRpcClientFactory.getXmlRpcClient(setting.getUrl()+guess);
		Vector params = new Vector();
		params.addElement(new Integer(1));
		params.addElement(setting.getUser());
		params.addElement(setting.getPwd());
		
		boolean guessed = false;
		Object result = null;
		try {
			result = xmlRpc.execute(targetFunc, params);
		} catch (Exception e) {
			guessed = false;
		}
		if(result!=null){
			response = (Vector)result;
			guessed = true;
		}
		return guessed;
	}
}
