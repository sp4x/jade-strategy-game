package com.jrts.environment;

import java.util.ArrayList;

public class Hit {

	Position pos;
	int speed;
	Direction dir;
	boolean enabled = true;
	
	public Hit(Position pos, int speed, Direction dir) {
		super();
		this.pos = pos;
		this.speed = speed;
		this.dir = dir;
	}
	
	public void step(){
		pos.setCol(pos.col + dir.colVar());
		pos.setRow(pos.row + dir.rowVar());
	}

	public Position getPos() {
		return pos;
	}

	public void setPos(Position pos) {
		this.pos = pos;
	}

	public int getSpeed() {
		return speed;
	}

	public void setSpeed(int speed) {
		this.speed = speed;
	}

	public Direction getDir() {
		return dir;
	}

	public void setDir(Direction dir) {
		this.dir = dir;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public boolean respectLimits(int rows, int cols) {
		return pos.row >= 0 && pos.row < rows && pos.col >= 0 && pos.col < cols;
	}

	public static boolean isThereAnHit(ArrayList<Hit> hits, int row, int col) {
		for (int i = 0; i < hits.size(); i++)
			if(hits.get(i).getPos().equals(new Position(row, col)))
				return true;
		System.out.println("No hit found");
		return false;
	}
}
