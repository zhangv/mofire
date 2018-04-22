package com.modofo.mofire;

import java.util.Vector;

import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Displayable;
import javax.microedition.media.MediaException;

import com.modofo.mofire.domain.Post;
import com.modofo.mofire.domain.Setting;

import jmunit.framework.cldc11.Test;
import jmunit.framework.cldc11.TestCase;

public class MoFireTest extends TestCase {
	MoFire mf = null;
	Setting setting = null;
	Post post = null;
	private Displayable current;

	/**
	 * The default constructor. It just transmits the necessary informations to
	 * the superclass.
	 * 
	 * @param totalOfTests
	 *            the total of test methods present in the class.
	 * @param name
	 *            this testcase's name.
	 */
	public MoFireTest() {
		super(5, "MoFireTest");

	}

	public void doStart() {
		//get the initial jmunit screen
		current = Display.getDisplay(this).getCurrent();
	}

	/**
	 * A empty method used by the framework to initialize the tests. If there's
	 * 5 test methods, the setUp is called 5 times, one for each method. The
	 * setUp occurs before the method's execution, so the developer can use it
	 * to any necessary initialization. It's necessary to override it, however.
	 * 
	 * @throws Throwable
	 *             anything that the initialization can throw.
	 */
	public void setUp() throws Throwable {
		mf = new MoFire(this);
		mf.initLang("EN");
		mf.initUI();
		setting = new Setting();
		setting.setUrl("http://127.0.0.1:9090");
		setting.setName("testsetting");
		setting.setUser("testuser");
		setting.setPwd("111");
		setting.setDefault(true);
		setting.setCategories("1,cat1;2,cat2");
		mf.getSettingForm().setSetting(setting);

		post = new Post();
		post.setTitle("testtitle");
		post.setContent("testcontent");
		post.setTags("test,mofire");

	}

	public void saveSetting() {
		// TODO save setting testcase
	}

	public void deleteSetting() {

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
	 * @param testNumber
	 *            the test to be executed.
	 * @throws Throwable
	 *             anything that the executed test can throw.
	 */
	public void test(int testNumber) throws Throwable {
		switch (testNumber) {
		case 0:
			newPost();
			break;
		case 1:
			validateSetting();
			break;
		case 2:
			newPostWithPhotoCapture(); //TODO chunked exception at server
			// side - see the tuning at the apache-xmlrpc
			break;
		case 3:
			savePost();
			break;
		case 4:
			System.out.println("###done testing###");
			Display.getDisplay(this).setCurrent(current);
			break;
		default:
			Display.getDisplay(this).setCurrent(current);
			break;
		}

	}

	private void newPost() {
		mf.getPostForm().setCurSetting(setting);
		mf.getPostForm().setPost(post);
		mf.getPostForm().setMIDlet(this);
		mf.getPostForm().doPostCmd();
	}

	private void validateSetting() {
		Vector v = new Vector();
		v.addElement(setting);
		mf.getSettingList().setSettings(v);
		mf.getSettingList().setMIDlet(this);
		mf.getSettingList().doValidate();
	}

	private void newPostWithPhotoCapture() {
		mf.getPostForm().setMIDlet(this);
		mf.getPostForm().showCamera();
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		try {
			mf.getPostForm().getCamera().doCapture();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		mf.getPostForm().setCurSetting(setting);
		mf.getPostForm().setPost(post);

		mf.getPostForm().doPostCmd();
	}

	private void savePost() {
		mf.getPostForm().setMIDlet(this);
		mf.getPostForm().setCurSetting(setting);
		mf.getPostForm().setPost(post);
		mf.getPostForm().savePost();
		// need verify - post saved, attachments saved
	}

	private void newPostWithPhotoLoaded() {
		// too difficult to test
	}

}
