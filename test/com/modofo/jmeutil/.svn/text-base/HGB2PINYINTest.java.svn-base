package com.modofo.jmeutil;

import jmunit.framework.cldc11.TestCase;

public class HGB2PINYINTest extends TestCase {

	/**
	 * The default constructor. It just transmits the necessary informations to
	 * the superclass.
	 * 
	 * @param totalOfTests the total of test methods present in the class.
	 * @param name this testcase's name.
	 */
	public HGB2PINYINTest() {
		super(4, "HGB2PINYINTest");
	}

	public void getAllPY1Test() {
		HGB2PINYIN h2p = new HGB2PINYIN();
		String r = h2p.getAllPY("中,文");
		System.out.println(r);
	}

	public void getAllPY2Test() {
		HGB2PINYIN h2p = new HGB2PINYIN();
		String r = h2p.getFirstPY("中,文");
		System.out.println(r);
	}

	public void getFirstPY1Test() {
	}

	public void getFirstPY2Test() {
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
			getAllPY1Test();
			break;
		case 1:
			getAllPY2Test();
			break;
		case 2:
			getFirstPY1Test();
			break;
		case 3:
			getFirstPY2Test();
			break;
		}
	}

}
