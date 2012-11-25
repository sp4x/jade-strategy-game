package com.jrts.common;

import jade.core.AID;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;

import com.jrts.environment.Position;
import com.jrts.environment.WorldMap;

public class Battalion {

	Position center;
	
	int diameter;
	//List<AID> soldiers = new LinkedList<AID>();
	boolean[][] occupiedPositions;
	HashMap<Position, AID> soldiers;
	
	public Battalion(Position center, int diameter) {
		this.center = center;
		this.diameter = diameter;
		soldiers = new HashMap<Position, AID>();
		
		occupiedPositions = new boolean[diameter][];
		for (int i = 0; i < diameter; i++) {
			occupiedPositions[i] = new boolean[diameter];
			Arrays.fill(occupiedPositions[i], Boolean.FALSE);
		}
	}
	
	public boolean isFull(){
		return soldiers.size() == diameter*diameter;

//		if(soldiers.size() == diameter*diameter){
//			for (int i = 0; i < diameter; i++) {
//				for (int j = 0; j < diameter; j++) {
//					
//					int w = center.getRow() - 1 + i;
//					int h = center.getCol() -1 + j;			
//					Position pos = new Position(w, h);
//					
//					if(map.get(pos).isWalkable())
//						return false;
//				}
//			}
//			
//			return true;
//		}
//		
//		return false;
	}
	
	public int size(){
		return this.soldiers.size();
	}
	
	public Position addSoldier(AID soldier){
		for (int i = 0; i < diameter; i++) {
			for (int j = 0; j < diameter; j++) {
				
				if(!this.occupiedPositions[i][j]){
					this.occupiedPositions[i][j] = true;
										
					int w = center.getRow() - 1 + i;
					int h = center.getCol() -1 + j;
					
					Position pos = new Position(w, h);
					this.soldiers.put(pos, soldier);
					
					return pos;
				}
			}
		}
		
		return null;
	}

	public Collection<AID> getSoldiersList() {
		return soldiers.values();
	}
}
