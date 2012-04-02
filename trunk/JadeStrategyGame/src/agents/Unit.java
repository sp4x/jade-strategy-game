package agents;

import jade.core.Agent;

public abstract class Unit extends Agent{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -6755184651108394854L;

	int x;
	int y;
	int life;
	int speed;
	int forceOfAttack;
	
	int team;
	
	public void goThere(int x, int y){
		//TODO
	}

	public int getX() {
		return x;
	}

	protected void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	protected void setY(int y) {
		this.y = y;
	}

	public int getLife() {
		return life;
	}

	protected void setLife(int life) {
		if(life>0)
			this.life = life;
		else
			this.life = 0;
	}

	public int getSpeed() {
		return speed;
	}

	protected void setSpeed(int speed) {
		this.speed = speed;
	}

	public int getForceOfAttack() {
		return forceOfAttack;
	}

	protected void setForceOfAttack(int forceOfAttack) {
		this.forceOfAttack = forceOfAttack;
	}

	public int getTeam() {
		return team;
	}

	private void setTeam(int team) {
		this.team = team;
	}
}
