package com.jrts.common;

public class GameStatistics implements Runnable {
		
	static private int counter = 0;
	static int frameRate = 0;
	
	public GameStatistics() {}

	@Override
	public void run() {
		while (true) {
//			System.err.println("Counter: " + counter);
			frameRate = counter;
			counter = 0;
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	public static void increaseCounter(){
		GameStatistics.counter++;
//		System.out.println("Increase counter " + counter);
	}
	
	public static int getFrameRate(){
		return GameStatistics.frameRate;
	}
}
