package com.jrts.logic;

import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.jrts.common.GameConfig;
import com.jrts.environment.Cell;
import com.jrts.environment.Direction;
import com.jrts.environment.Hit;
import com.jrts.environment.Position;
import com.jrts.environment.World;

public class AttacksManager {

	static Logger logger = Logger.getLogger(AttacksManager.class.getName());
	static Level logLevel = Level.FINE;
	
	private static ArrayList<Hit> hits;
	private static int counter;
	
	static {
		hits = new ArrayList<Hit>();
	}

	public synchronized static void addHit(Position pos, Direction dir, int damage, int hitRange){
		Hit hit = new Hit(pos, dir, damage, hitRange);
		//Eseguo uno spostamento per evitare che il colpo danneggi l'unita' sorgente stessa
//		hit.step(); EDIT: viene fatto dopo
		hits.add(hit);
	}
	
	public synchronized static void update() {
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
		
		//check if there is some collision
		for (int i = 0; i < hits.size(); i++){
			Position hp = hits.get(i).getPos();
			Cell cell = World.getInstance().getCell(hp);
			if(!cell.isFree()){
				logger.log(logLevel, "Detected collision");
				Hit hit = hits.remove(i);
				Position pos = hit.getPos();
				int damage = hit.getDamage();
				if(cell.isCityCenter() || cell.isUnit())
					World.getInstance().takeEnergy(pos, damage);
			}
			else if(hits.get(i).getRange() == 0){
				//exceeded range limit
				hits.get(i).setEnabled(false);
			}
		}
		
		//remove disabled hits
		for (int i = 0; i < hits.size(); i++)
			if(!hits.get(i).isEnabled())
				hits.remove(i);
	}

	public synchronized static boolean isThereAnHit(int row, int col) {
		for (int i = 0; i < hits.size(); i++)
			if(hits.get(i).getPos().equals(new Position(row, col)))
				return true;
		return false;
	}
}