package com.modofo.mofire;

import java.util.Vector;

import javax.microedition.rms.RecordStore;
import javax.microedition.rms.RecordStoreException;
import javax.microedition.rms.RecordStoreFullException;
import javax.microedition.rms.RecordStoreNotFoundException;

import jmunit.framework.cldc11.TestCase;

import com.modofo.mofire.domain.Post;
import com.modofo.mofire.domain.Setting;

public class MoFireMgrTest extends TestCase {
	MoFireMgr mgr = null;
	/**
	 * The default constructor. It just transmits the necessary informations to
	 * the superclass.
	 * 
	 * @param totalOfTests the total of test methods present in the class.
	 * @param name this testcase's name.
	 */
	public MoFireMgrTest() {
		super(7, "MoFireMgrTest");
	}
 
	public void savePost1Test() {
		Post p = new Post();
		p.setTitle("test");
		p.setContent("test content");
		p.setCategory("category0");
		p.addImage(new byte[1]);
		p.setSound(new byte[1]);
		int id = -1;
		try {
			id = mgr.savePost(p);
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("saved id - "+id);
		assertNotEquals(id, -1);
	}

	public void delPost1Test() {
		MoFireMgr mgr = new MoFireMgr();
		Post p = new Post();
		p.setTitle("test");
		p.setContent("test content");
		p.setCategory("category0");
		
		int id = -1;
		try {
			id = mgr.savePost(p);
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("saved id - "+id);
		
		//delete
		try {
			Vector all = mgr.getPosts(null);
			int before = all.size();
			mgr.delPost(id);
			all = mgr.getPosts(null);
			int after = all.size();
			assertEquals(before,after+1);
		} catch (RecordStoreFullException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (RecordStoreNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (RecordStoreException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	public void getPost1Test() {
		Post p = new Post();
		p.setTitle("test");
		p.setContent("test content");
		p.setCategory("category0");
		p.addImage(new byte[1]);
		p.setSound(new byte[1]);
		int id = -1;
		try {
			id = mgr.savePost(p);
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("saved id - "+id);
		assertNotEquals(id, -1);
		
		//then get
		Post pp=null;
		try {
			pp = mgr.getPost(id);
		} catch (Exception e) {
			e.printStackTrace();
		}
		assertNotNull(pp);
		if(pp!=null){
			assertNotNull(pp.getSoundPath());
			assertEquals(pp.getImagePaths().size(),1);
		}
	}

	public void getPosts1Test() {
		Vector posts = null;
		try {
			Post p = new Post();
			p.setTitle("test");
			p.setContent("test content");
			p.setCategory("category0");
			mgr.savePost(p);
			posts = mgr.getPosts(null);
		} catch (Exception e) {
			e.printStackTrace();
		}
		assertNotNull(posts);
	}

	public void saveSetting1Test() {
		Setting se = new Setting();
		se.setName("test"+System.currentTimeMillis());
		se.setDefault(false);
		se.setPwd("");
		se.setUrl("http://dummy");
		se.setUser("user");
		try {
			mgr.saveSetting(se);
		} catch (RecordStoreFullException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (RecordStoreNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (RecordStoreException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (DuplicateSettingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void delSetting1Test() {
		System.out.println("***delSettingTest|start***");
		Setting se = new Setting();
		se.setName("deletetest");
		se.setCategories("");
		se.setDefault(true);
		se.setUser("user");
		se.setUrl("http://test");
		se.setPwd("pwd");
		try {
			int id = mgr.saveSetting(se);
			Vector v = mgr.getSettings(null);
			int before = v.size();
			Setting tmp = (Setting) v.elementAt(0);
			mgr.delSetting(tmp.getId());
			v = mgr.getSettings(null);
			int af = v.size();
			assertEquals(before-1,af);
		} catch (RecordStoreFullException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (RecordStoreNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (RecordStoreException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (DuplicateSettingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("***delSettingTest|end***");
	}

	public void getSetting1Test() {
		Setting s = new Setting();
		String name = "getsettingtest";
		s.setName(name);
		s.setCategories("");
		s.setDefault(false);
		s.setPwd("pwd");
		s.setUrl("");
		s.setUser("user");
		try {
			int id = mgr.saveSetting(s);
			Setting ss = mgr.getSetting(id);
			assertEquals(name,ss.getName());
		} catch (RecordStoreFullException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (RecordStoreNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (RecordStoreException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (DuplicateSettingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}

	/**
	 * A empty method used by the framework to initialize the tests. If there's
	 * 5 test methods, the setUp is called 5 times, one for each method. The
	 * setUp occurs before the method's execution, so the developer can use it
	 * to any necessary initialization. It's necessary to override it, however.
	 * 
	 * @throws Throwable anything that the initialization can throw.
	 */
	public void setUp() throws Throwable {
		mgr = new MoFireMgr();
//		RecordStore.deleteRecordStore(MoFireMgr.STORE_CONFIG);
		/*RecordStore store = null;
		store = RecordStore.openRecordStore(MoFireMgr.STORE_CONFIG,false);
		store.
		store.closeRecordStore();
		store = RecordStore.openRecordStore(MoFireMgr.STORE_SETTING,false);
		store.closeRecordStore();
		RecordStore.deleteRecordStore(MoFireMgr.STORE_POST);
		*/
	}

	/**
	 * A empty mehod used by the framework to release resources used by the
	 * tests. If there's 5 test methods, the tearDown is called 5 times, one for
	 * each method. The tearDown occurs after the method's execution, so the
	 * developer can use it to close something used in the test, like a
	 * nputStream or the RMS. It's necessary to override it, however.
	 */
	public void tearDown() {
	}

	/**
	 * This method stores all the test methods invocation. The developer must
	 * implement this method with a switch-case. The cases must start from 0 and
	 * increase in steps of one until the number declared as the total of tests
	 * in the constructor, exclusive. For example, if the total is 3, the cases
	 * must be 0, 1 and 2. In each case, there must be a test method invocation.
	 * 
	 * @param testNumber the test to be executed.
	 * @throws Throwable anything that the executed test can throw.
	 */
	public void test(int testNumber) throws Throwable {
		switch (testNumber) {
		case 0:
			savePost1Test();
			break;
		case 1:
			delPost1Test();
			break;
		case 2:
			getPost1Test();
			break;
		case 3:
			getPosts1Test();
			break;
		case 4:
			saveSetting1Test();
			break;
		case 5:
			delSetting1Test();
			break;
		case 6:
			getSetting1Test();
			break;
		}
	}

}
