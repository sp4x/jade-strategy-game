package com.jrts.environment;

public class Hit {

	Position pos;
	int damage;
	Direction dir;
	boolean enabled = true;
	
	public Hit(Position pos, Direction dir, int damage) {
		super();
		this.pos = pos;
		this.damage = damage;
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

	public int getDamage() {
		return damage;
	}

	public void setDamage(int damage) {
		this.damage = damage;
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
}
