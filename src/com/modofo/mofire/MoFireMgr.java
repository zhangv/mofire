package com.modofo.mofire;

import java.util.Vector;

import javax.microedition.rms.RecordComparator;
import javax.microedition.rms.RecordEnumeration;
import javax.microedition.rms.RecordFilter;
import javax.microedition.rms.RecordStore;
import javax.microedition.rms.RecordStoreException;
import javax.microedition.rms.RecordStoreFullException;
import javax.microedition.rms.RecordStoreNotFoundException;
import javax.microedition.rms.RecordStoreNotOpenException;

import com.modofo.jmeutil.DateUtils;
import com.modofo.jmeutil.Utils;
import com.modofo.mofire.domain.Config;
import com.modofo.mofire.domain.Post;
import com.modofo.mofire.domain.Setting;
import com.modofo.mofire.domain.Storable;

public class MoFireMgr implements RecordFilter, RecordComparator {
	private RecordStore store;
	private String filter;
	public static String STORE_POST = "moFire-post";
	public static String STORE_SETTING = "moFire-setting";
	public static String STORE_CONFIG = "moFire-config";
	
	public int savePost(Post post) throws RecordStoreNotOpenException, RecordStoreFullException, RecordStoreException {
		//first save all the attachment to the local file system
		// store images to photo dir
		final Vector images = post.getImages();
		final int size = images.size();
		System.out.println("images size:"+size);
		final String imagepath = System.getProperty(Constants.PHOTO_DIRECTORY); // file:///root1/photos/
		Vector imgspath = new Vector(size);
		final String[] imagenames = new String[size];
		for (int i = 0; i < size; i++) {
			//append name with date stamp
			StringBuffer sb = new StringBuffer(Constants.APP_NAME);
			sb.append(DateUtils.getToday());
			sb.append("-").append(post.getTitle()).append("-").append(i).append(".jpg");
			imagenames[i] = sb.toString();
			imgspath.addElement(imagepath + imagenames[i]);
		}
		//use ONE thread to save the image to local filesystem
		new Thread(){
			public void run(){
				for (int i = 0; i < size; i++) {
					System.out.println("saving image file:"+imagenames[i]+" to "+imagepath);
					byte[] data = (byte[]) images.elementAt(i);
					Utils.saveFile(imagepath, imagenames[i], data);
					System.out.println("saved."+imagepath+imagenames[i]);
				}
			}
		}.start();
		
		post.setImagePaths(imgspath);
		//store sound clip to sound dir
		final String soundpath = System.getProperty(Constants.SOUND_DIRECTORY);
		if(post.getSound()!=null){
			final byte[] data = post.getSound();
			StringBuffer sb = new StringBuffer(Constants.APP_NAME);
			sb.append(DateUtils.getToday());
			sb.append("-").append(post.getTitle()).append("-clip.wav");
			final String name = sb.toString();
			System.out.println("saving sound file:"+name+" to "+soundpath);
			new Thread(){
				public void run(){
					Utils.saveFile(soundpath, name, data);
				}
			}.start();
			System.out.println("saved."+soundpath+name);
			post.setSoundPath(soundpath+name);
		}
		return save(post,STORE_POST);
	}
	public void delPost(int id) throws RecordStoreFullException, RecordStoreNotFoundException, RecordStoreException {
		System.out.println("deleting post - id="+id);
		delete(id,STORE_POST);
		System.out.println("deleted post - id="+id);
	}
	public Post getPost(int id) throws RecordStoreFullException, RecordStoreNotFoundException, RecordStoreException {
		store = RecordStore.openRecordStore(STORE_POST, true);
		byte[] bt = store.getRecord(id);
		Post p = null;
		if(bt !=null) {
			p = new Post();
			p.fromByteArray(bt);
		}
		store.closeRecordStore();
		return p;
	}
	public Vector getPosts(final String filter) throws RecordStoreFullException, RecordStoreNotFoundException, RecordStoreException {
		store = RecordStore.openRecordStore(STORE_POST, true);
		RecordEnumeration enu = store.enumerateRecords( new RecordFilter(){
			public boolean matches(byte[] arg0) {
				if(filter == null) return true;//no filter, show all
				if(arg0 == null) return false;
				Post tmp = new Post();
				tmp.fromByteArray(arg0);
				if(tmp.getTitle().indexOf(filter)!=-1) return true; //contains keyword in title
				if(tmp.getContent().indexOf(filter)!=-1) return true;//contains keyword in content
				else return false;
			}
		},null, true);
		Post temp = null;
		Vector result = new Vector();
		while ( enu.hasNextElement()) {
			temp = new Post();
			temp.fromByteArray(enu.nextRecord());
			result.addElement(temp);
		}
		store.closeRecordStore();
		return result;
	}
	
	public int saveSetting(Setting p) throws RecordStoreFullException, RecordStoreNotFoundException, RecordStoreException, DuplicateSettingException {
		//check duplication
		Setting crit1 = new Setting();
		crit1.setName(p.getName());
		
		Vector result = getSettings(crit1);
//		System.out.println(result.size());
//		for(int i=0;i<result.size();i++)System.out.println(result.elementAt(i));
		if(result.size()==1){
			Setting s=(Setting) result.elementAt(0);
			if(p.getId()!=s.getId())
			//found duplicate name
			throw new DuplicateSettingException(Res.getInstance().getString("setting.duplicate"));
		}
		//convert previous default setting
		if(p.isDefault()){
			Setting crit = new Setting();
			crit.setDefault(true);
			result = getSettings(crit);
			if(null!=result){//found previous default settings
				//convert them
				Setting tmp=null;
				for(int i=0;i<result.size();i++){
					tmp = (Setting) result.elementAt(i);
					tmp.setDefault(false);
					save(tmp,STORE_SETTING);
				}
			}
		}
		return save(p,STORE_SETTING);
	}
	private Storable get(int id,String storename,Storable s) throws RecordStoreFullException, RecordStoreNotFoundException, RecordStoreException{
		if(s == null) return s;
		store = RecordStore.openRecordStore(storename, true);
		byte[] bt = store.getRecord(id);
		Storable ss = s;
		if(bt !=null) {
			ss.fromByteArray(bt);
		}
		store.closeRecordStore();
		return ss;
	}
	private void delete(int id,String storename) throws RecordStoreFullException, RecordStoreNotFoundException, RecordStoreException{
		store = RecordStore.openRecordStore(storename, true);
		store.deleteRecord(id);
		store.closeRecordStore();
	}
	private int save(Storable s,String storename) throws RecordStoreFullException, RecordStoreNotFoundException, RecordStoreException{
		store = RecordStore.openRecordStore(storename, true);
		int id = s.getId();
		if(id ==0){
			id = store.addRecord(s.toByteArray(), 0, s.toByteArray().length);  
			s.setId(id);
			//update id
			store.setRecord(id, s.toByteArray(), 0, s.toByteArray().length);
			System.out.println("ID:"+id+":CREATED:"+storename+":"+s.toString());
		}else{
			store.setRecord(s.getId(), s.toByteArray(), 0, s.toByteArray().length);
			System.out.println("ID:"+id+":UPDATED:"+storename+":"+s.toString());
		}
		System.out.println("TOTAL:"+store.getNumRecords());
		store.closeRecordStore();
		return id;
	}
	
	public void delSetting(int id) throws RecordStoreFullException, RecordStoreNotFoundException, RecordStoreException {
		delete(id,STORE_SETTING);
	}
	public Setting getSetting(int id) throws RecordStoreFullException, RecordStoreNotFoundException, RecordStoreException {
		return (Setting)get(id,STORE_SETTING,new Setting());
	}
	public Vector getSettings(final Setting filter) throws RecordStoreFullException, RecordStoreNotFoundException, RecordStoreException{
		store = RecordStore.openRecordStore(STORE_SETTING, true);
		RecordEnumeration enu = store.enumerateRecords( new RecordFilter(){
			public boolean matches(byte[] arg0) {
				if(filter == null) return true;//no filter, show all
				if(arg0 == null) return false;
				Setting tmp = new Setting();
				tmp.fromByteArray(arg0);
				boolean result = true;
				if(filter.getId()!=0 && filter.getId()!=tmp.getId()){
					result = false;
				}else if(filter.getName()!=null && !filter.getName().equals(tmp.getName())){
					result = false;
				}else if(filter.getUser()!=null && !filter.getUser().equals(tmp.getUser())){
					result = false;
				}else if(filter.getUrl()!=null && !filter.getUrl().equals(tmp.getUrl())){
					result = false;
				}else if(filter.isDefault() != tmp.isDefault()){
					result = false;
				}
				return result;
			}
		},null, true);
		Setting temp = null;
		Vector result = new Vector();
		while ( enu.hasNextElement()) {
			temp = new Setting();
			if(temp.fromByteArray(enu.nextRecord())){
				result.addElement(temp);
			}else{
				
			}
		}
		store.closeRecordStore();
		return result;
	}
	
	public boolean matches(byte[] arg0) {
		// TODO Auto-generated method stub
		return false;
	}

	public int compare(byte[] arg0, byte[] arg1) {
		// TODO Auto-generated method stub
		return 0;
	}
	
	public void saveConfig(Config config) throws RecordStoreFullException, RecordStoreNotFoundException, RecordStoreException{
		Config c = loadConfig();
		if(c != null){ //first time saving
			config.setId(1);
		}
		save(config,STORE_CONFIG);
	}
	
	public Config loadConfig() throws RecordStoreFullException,  RecordStoreException{
		store = RecordStore.openRecordStore(STORE_CONFIG, true);
		Config p = null;
		
		byte[] bt = null;
		try {
			bt = store.getRecord(1);
		} catch (Exception e) {
			//e.printStackTrace();
		}
		
		if(bt !=null) {
			p = new Config();
			p.fromByteArray(bt);
		}
		store.closeRecordStore();
		return p;
	}
	
}