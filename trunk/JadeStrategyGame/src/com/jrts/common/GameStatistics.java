package com.jrts.common;


import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

public class GameStatistics implements Runnable {
	
	static Logger logger = Logger.getLogger(GameStatistics.class.getSimpleName());
	static Level logLevel = Level.FINE;
		
	static private int counter = 0;
	static int frameRate = 0;
	
	static HashMap<String, Integer> actionCounter = new HashMap<String, Integer>();
	static HashMap<String, Long> mediumTime = new HashMap<String, Long>();
	
	public GameStatistics() {}

	@Override
	public void run() {
		while (true) {
			showActionRateStatistics();
			
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
	
	private void showActionRateStatistics() {
		if(GameConfig.STATISTICS && !mediumTime.isEmpty()){
			logger.log(logLevel, "------------------------------------------------------");
			for (String className : mediumTime.keySet()) {
				logger.log(logLevel, "Class: " + className + " " + mediumTime.get(className));
			}
		}
	}

	public static void increaseCounter(){
		GameStatistics.counter++;
//		logger.log(logLevel, "Increase counter " + counter);
	}
	
	public static int getFrameRate(){
		return GameStatistics.frameRate;
	}

	public static void saveElapsedTime(String className, long timeElapsed) {
		if(GameConfig.STATISTICS){
//			logger.log(logLevel, "Behaviour: " + className + " " + timeElapsed);
			if(actionCounter.containsKey(className)){
				actionCounter.put(className, actionCounter.get(className)+1);
				Long currMediumTime = mediumTime.get(className);
				Integer oldActionSize = actionCounter.get(className);
				Long newMediumTime = (currMediumTime*oldActionSize + timeElapsed)/(oldActionSize+1) ;
				mediumTime.put(className, newMediumTime);
			}
			else{
				actionCounter.put(className,1);
				mediumTime.put(className, timeElapsed);
			}
		}
	}
}
