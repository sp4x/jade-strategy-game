package com.jrts.gui;

import java.util.ArrayList;

import com.jrts.environment.Direction;
import com.jrts.environment.Hit;
import com.jrts.environment.Position;
import com.jrts.environment.World;

public class AttacksManager {

	private static ArrayList<Hit> hits;
	
	static {
		hits = new ArrayList<Hit>();
	}

	private int hitsCounter;
	
	public synchronized static void addHit(Position pos, Direction dir, int damage){
		hits.add(new Hit(pos, dir, damage));
		System.out.println("Added new hit" + hits.size());
	}
	
	private synchronized void updateHits() {
		hitsCounter ++;
		if(hitsCounter < 50)
			return;
		hitsCounter = 0;
		
		//refresh hit's positions
		for (int i = 0; i < hits.size(); i++){
			Hit hit = (Hit) hits.get(i);
			hit.step();
			//disable hit which exceed floor's bound
			if(hit.respectLimits(World.getInstance().getRows(), World.getInstance().getCols()))
				hit.setEnabled(false);
		}
		
		//remove disabled hits
		for (int i = 0; i < hits.size(); i++)
			if(!hits.get(i).isEnabled()){
				hits.remove(i);
				System.out.println("Removed hit");
			}
		
		//check if there is some collision
		for (int i = 0; i < hits.size(); i++){
			Position hitPos = hits.get(i).getPos();
//			if(floor.get(hitPos.row, hitPos.col) != Cell.FREE)
//				hits.remove(i);
		}
	}

	public static boolean isThereAnHit(int row, int col) {
		for (int i = 0; i < hits.size(); i++)
			if(hits.get(i).getPos().equals(new Position(row, col)))
				return true;
		return false;
	}
}