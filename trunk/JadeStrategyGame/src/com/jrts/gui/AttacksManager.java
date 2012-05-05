package com.jrts.gui;

import java.util.ArrayList;

import com.jrts.environment.Cell;
import com.jrts.environment.Direction;
import com.jrts.environment.Hit;
import com.jrts.environment.Position;
import com.jrts.environment.World;

public class AttacksManager {

	private static ArrayList<Hit> hits;
	private static int counter;
	
	static {
		hits = new ArrayList<Hit>();
	}

	public synchronized static void addHit(Position pos, Direction dir, int damage){
		hits.add(new Hit(pos, dir, damage));
		System.out.println("Added new hit" + hits.size());
	}
	
	synchronized static void update() {
		if(counter++<50)
			return;
		counter = 0;
		
		//refresh hit's positions
		for (int i = 0; i < hits.size(); i++){
			Hit hit = (Hit) hits.get(i);
			hit.step();
			//disable hit which exceed floor's bound
			if(!hit.respectLimits(World.getInstance().getRows(), World.getInstance().getCols()))
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
			Position hp = hits.get(i).getPos();
			if(World.getInstance().getFloor().get(hp.getRow(), hp.getCol()) != Cell.FREE)
				hits.remove(i);
			//TODO notificare se viene colpita unita'
		}
	}
	

	public synchronized static boolean isThereAnHit(int row, int col) {
		for (int i = 0; i < hits.size(); i++)
			if(hits.get(i).getPos().equals(new Position(row, col)))
				return true;
		return false;
	}
}