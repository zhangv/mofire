package com.modofo.mofire.call;

import java.util.Hashtable;
import java.util.Vector;

import javax.microedition.location.LocationException;

import org.kxmlrpc.XmlRpcClient;

import com.modofo.jmeutil.StringUtils;
import com.modofo.jmeutil.Utils;
import com.modofo.mofire.Constants;
import com.modofo.mofire.GoogleMaps;
import com.modofo.mofire.ImageUploadingException;
import com.modofo.mofire.MoFire;
import com.modofo.mofire.MoFireEvent;
import com.modofo.mofire.MoFireEventType;
import com.modofo.mofire.Res;
import com.modofo.mofire.XmlRpcClientFactory;
import com.modofo.mofire.domain.Post;
import com.modofo.mofire.domain.Setting;

public class NewPostCall extends AbstractCall {
	private String targetFunc = "metaWeblog.newPost";
	private Setting setting;
	private Object response;
	private Vector imageUrls;
	private boolean imagesUploadSuccess = false;
	private String soundUrl;
	private boolean soundUploadSuccess = false;
	private Post post;
	boolean uploadFail = false;
	
	NewPostCall(Setting setting, Post post){
		this(null,setting,post);
	}
	
	NewPostCall(XmlRpcClient xmlrpc, Setting setting, Post post) {
		this.setting = setting;
		this.post = post;
		imageUrls = new Vector();
		if(null == xmlrpc)
			this.xmlRpc = XmlRpcClientFactory.getXmlRpcClient(setting.getUrl());
		else this.xmlRpc = xmlrpc;
	}
	
	
	public void run() {
		try {
			uploadImage(xmlRpc);
		} catch (ImageUploadingException iue) { 
			Utils.error(MoFire.getMidlet(),Res.getString("post.imageupload.error")+"-"+iue.toString());
			
		}
		
		try {
			uploadSoundClip(xmlRpc);
		} catch (Exception e) {
			//Utils.error(ResourceBundle.getString("post.soundupload.error"));
			Utils.error(MoFire.getMidlet(),e.toString());
		}
		
		StringBuffer sb = new StringBuffer();
		sb.append(post.getContent());
		//append sound
		if(soundUploadSuccess){
			sb.append("<OBJECT id='rvocx' classid='clsid:CFCDAA03-8BE4-11cf-B84B-0020AFBBCCFA'")
				.append("width='320' height='30'>")
				.append("<param name='src' value='"+soundUrl+"'>")
				.append("<param name='autostart' value='false'>")
				.append("<param name='controls' value='ControlPanel'>")
				.append("<param name='console' value='audio'>")
				.append("<EMBED src='"+soundUrl+" width='320' height='30'")
				.append(" type='audio/x-pn-realaudio-plugin' controls='ControlPanel'")
				.append(" console='video' autostart='false'></EMBED>")
				.append("</OBJECT><br>");
		}
		//append images
		if(imagesUploadSuccess){
			for (int i = 0; i < imageUrls.size(); i++) {
				String imageurl = (String) imageUrls.elementAt(i);
				sb.append("<br><a href=\"");
				sb.append(imageurl);
				sb.append("\">");
				sb.append("<img src=\"");
				sb.append((String) imageUrls.elementAt(i));
				sb.append("\" width=\"");
				sb.append(Constants.IMAGE_WIDTH);
				sb.append("\"/></a>");
			}
		}
		
		//append location info
		String mapurl =null;
		try {
			mapurl = GoogleMaps.getCurrentLocationMapUrl();
			if(mapurl!=null){
			sb.append("<br><a href=\"")
			.append(mapurl).append("\">location</a>");
			}
		} catch (LocationException e1) {
			e1.printStackTrace();
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
		
		//sb.append(Constants.POWERED_BY);
		

		Hashtable content = new Hashtable();
		content.put("title", post.getTitle());
		content.put("description",sb.toString());
		content.put("categories", post.getCategory());
		String tagstr = post.getTags();
		//blank char as the tag separator
		tagstr = tagstr.replace(' ', ',');
		content.put("mt_keywords",tagstr);
		
		Vector params = new Vector();
		params.addElement(new Integer(1));
		params.addElement(setting.getUser());
		params.addElement(setting.getPwd());
		params.addElement(content);
		params.addElement(new Boolean(true));
		String status = "Posted!";
		try {
			response = xmlRpc.execute(targetFunc, params);
			System.out.println("new post ID : " + response);
			
			Vector params2 = new Vector();
			params2.addElement(response.toString());
			params2.addElement(setting.getUser());
			params2.addElement(setting.getPwd());
			Object response2 = xmlRpc.execute("metaWeblog.getPost", params2);
			System.out.println("response2:"+response2);
			Hashtable ht = (Hashtable) response2;
			String link = (String) ht.get("link");
			System.out.println(link);
			post.setLink(link);
		} catch (Exception e) {
			status = e.toString();
			e.printStackTrace();
			// Utils.alert("Exception while posting content", "Error",
			// 3000);
		}
		params = null;
		MoFireEvent evt = new MoFireEvent();
		evt.setType(MoFireEventType.POST_SUCCESS);
		evt.setMessage(status);
		evt.setObj1(post);
		
		fireEvent(evt);
	}

	private void uploadSoundClip(XmlRpcClient xmlRpc) throws Exception {
		if(null==post.getSound()) return; // no sound attachments
		Hashtable ht = new Hashtable();
		ht.put("name", post.getTitle() + System.currentTimeMillis()
				+ ".wav");
		ht.put("type", "sound");
		ht.put("bits", post.getSound());
		
		Vector params = new Vector();
		params.addElement(new Integer(1));
		params.addElement(setting.getUser());
		params.addElement(setting.getPwd());
		params.addElement(ht);

		Object response = xmlRpc.execute("metaWeblog.newMediaObject",
				params);
		Hashtable result = (Hashtable) response;
		soundUrl = (String) result.get("url");
		soundUploadSuccess=true;
	}

	private void uploadImage(XmlRpcClient xmlrpc)
			throws ImageUploadingException {
		if(null==post.getImages() || 0>=post.getImages().size()) return; //no image attachments
		int imagesize = post.getImages().size();
		Hashtable ht = new Hashtable();
		Vector params = null;
		imageUrls.removeAllElements();
		for (int i = 0; i < imagesize; i++) {
			String imgname = StringUtils.gb2pinyin(post.getTitle());
			ht.put("name",  imgname+"-"+ System.currentTimeMillis()
					+ ".jpg");
			ht.put("type", "image");
			ht.put("bits", (byte[]) post.getImages().elementAt(i));
			params = new Vector();
			params.addElement(new Integer(1));
			params.addElement(setting.getUser());
			params.addElement(setting.getPwd());
			params.addElement(ht);

			try {
				// wp.uploadFile = metaWeblog.newMediaObject
				Object response = xmlrpc.execute("metaWeblog.newMediaObject",
						params);
				Hashtable result = (Hashtable) response;
				imageUrls.addElement(result.get("url"));
				System.out.println("response=" + response);
			} catch (Exception e) {
				uploadFail = true;
				throw new ImageUploadingException(e.toString());
			}
			params.removeAllElements();
		}
		imagesUploadSuccess=true;
	}

	public Object getResponse() {
		return response;
	}
}
