package com.modofo.mofire.call;

import java.util.Vector;

import org.kxmlrpc.XmlRpcClient;

import com.modofo.mofire.MoFireEvent;
import com.modofo.mofire.MoFireListener;

public abstract class AbstractCall implements Call {
	protected XmlRpcClient xmlRpc;
	protected String targetUrl,targetFunc;
	protected String username,password;
	private Object response;
	private Vector listeners = new Vector();

	public synchronized void go(){
		Thread t = new Thread(this);
		t.start();
	}
	public XmlRpcClient getXmlRpc() {
		return xmlRpc;
	}

	public void setXmlRpc(XmlRpcClient xmlRpc) {
		this.xmlRpc = xmlRpc;
	}

	public String getTargetUrl() {
		return targetUrl;
	}

	public void setTargetUrl(String targetUrl) {
		this.targetUrl = targetUrl;
	}

	public String getTargetFunc() {
		return targetFunc;
	}

	public void setTargetFunc(String targetFunc) {
		this.targetFunc = targetFunc;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Object getResponse() {
		return response;
	}

	public void setResponse(Object response) {
		this.response = response;
	}
	
	public void addListener(MoFireListener listener){
		listeners.addElement(listener);
	}
	public void fireEvent(MoFireEvent evt){
		int size = listeners.size();
		if(size<=0) return;
		MoFireListener l;
		for(int i=0;i<size;i++ ){
			l = (MoFireListener) listeners.elementAt(i);
			l.onEvent(evt);
		}
	}
}
