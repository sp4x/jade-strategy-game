package com.jrts.gui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import com.jrts.common.GameConfig;
import com.jrts.environment.Cell;
import com.jrts.environment.CellType;
import com.jrts.environment.Direction;
import com.jrts.environment.Floor;
import com.jrts.environment.Hit;
import com.jrts.environment.Position;
import com.jrts.environment.World;

public class AttacksManager {

	private static ArrayList<Hit> hits;
	private static int counter;
	private static HashMap<String, Integer> damagesList;
	
	static {
		hits = new ArrayList<Hit>();
		damagesList = new HashMap<String, Integer>();
	}

	public synchronized static void addHit(Position pos, Direction dir, int damage){
		Hit hit = new Hit(pos, dir, damage);
		//Eseguo uno spostamento per evitare che il colpo danneggi l'unità sorgente stessa
		hit.step();
		hits.add(hit);
//		System.out.println("Added new hit" + hits.size());
	}
	
	synchronized static void update() {
		if(counter++ < GameConfig.ATTACKS_REFRESH)
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
//				System.out.println("Removed hit");
			}
		
		//check if there is some collision
		for (int i = 0; i < hits.size(); i++){
			Position hp = hits.get(i).getPos();
			Floor floor = World.getInstance().getFloor();
			if(floor.get(hp).getType() != CellType.FREE){
				System.out.println("Detected collision");
				notifyDamage(hits.remove(i));
			}
		}
	}

	private static void notifyDamage(Hit hit) {
		Floor floor = World.getInstance().getFloor();
		
		Position pos = hit.getPos();
		String id = floor.get(pos).getId();
		int damage = hit.getDamage();
		
		damagesList.put(id, damage);
	}

	public synchronized static boolean isThereAnHit(int row, int col) {
		for (int i = 0; i < hits.size(); i++)
			if(hits.get(i).getPos().equals(new Position(row, col)))
				return true;
		return false;
	}

	public synchronized static int getDamagesFor(String id) {
		Integer damage = damagesList.get(id);
		if(damage == null)
			return 0;
		System.out.println("Damage is " + damage);
		return damage;
	}
}