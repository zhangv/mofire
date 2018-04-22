package com.modofo.jmeutil;

import com.modofo.mofire.MoFire;

public class Logger {
	public static void log(String log){
		System.out.println(log);
	}
	
	public static void debug(String log){
		//LATER MoFire.getInstance().getLogForm().log(log);
	}
}
