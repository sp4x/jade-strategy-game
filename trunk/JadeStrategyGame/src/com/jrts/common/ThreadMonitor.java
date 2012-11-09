package com.jrts.common;

public class ThreadMonitor {
	
	private static final ThreadMonitor instance = new ThreadMonitor();
	
	public static ThreadMonitor getInstance() {
		return instance;
	}
	
	public static void disable() {
		instance.enabled = false;
	}
	
	private boolean enabled = true;
	
	public synchronized void doWait() {
		if (enabled)
			try {
				wait();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	}
	
	public synchronized void sendNotifyAll() {
		notifyAll();
	}

}
