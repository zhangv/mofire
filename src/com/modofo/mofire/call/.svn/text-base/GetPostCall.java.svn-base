package com.modofo.mofire.call;

import java.util.Vector;

import org.kxmlrpc.XmlRpcClient;

import com.modofo.mofire.XmlRpcClientFactory;
import com.modofo.mofire.domain.Setting;

public class GetPostCall extends AbstractCall {
	private String targetFunc = "metaWeblog.getPost";
	private Object response;
	private Setting setting;
	private String postId;
	GetPostCall(String postid, Setting setting) {
		this(null,postid,setting);
	}

	GetPostCall(XmlRpcClient xmlrpc,String postid, Setting setting) {
		this.postId = postid;
		this.setting = setting; 
		if (null == xmlrpc)
			this.xmlRpc = XmlRpcClientFactory.getXmlRpcClient(setting.getUrl());
		else
			this.xmlRpc = xmlrpc;
	}

	public void run() {
		Vector params = new Vector();
		params.addElement(postId);
		params.addElement(setting.getUser());
		params.addElement(setting.getPwd());
		try {
			response = xmlRpc.execute(targetFunc, params);
			System.out.println("response:" + response);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}