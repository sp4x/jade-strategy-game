package com.jrts.gui;

import com.jrts.environment.Position;
import com.jrts.environment.World;

public class DeleteExplosionThread extends Thread {

	Position p;
	public DeleteExplosionThread(Position p){
		this.p = p;
	}
	
	 @Override
	public void run() {
		super.run();
		
		try{Thread.sleep(2000); }
		catch(Exception e){}
		
		World.getInstance().takeEnergy(this.p, 1000);
	}
}
