package agents;

import jade.core.Agent;

import java.util.List;

import logic.PositionGoal;
import behaviours.FollowPathBehaviour;

import com.jrts.environment.Direction;
import com.jrts.environment.Position;
import com.jrts.environment.World;

public abstract class Unit extends Agent{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -6755184651108394854L;

	private Position position = null;
	private String team;
	int life;
	int speed;
	int forceOfAttack;
	
	
	PositionGoal positionGoal;
	
	public Unit() {}
	
	Unit(Position position) {
		super();
		this.position = position;
	}



	public void goThere(int x, int y) {
		List<Direction> path = World.getInstance().getPath(this.position, x, y);
		addBehaviour(new FollowPathBehaviour(this, path, x, y));
	}

	public boolean move(Direction dir){
		return World.getInstance().move(this.position, dir);
	}
	
	public Position getPosition() {
		return position;
	}

	protected void setPosition(Position position) {
		if (this.position == null)
			this.position = position;
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

	public String getTeam() {
		return team;
	}

	protected void setTeam(String team) {
		this.team = team;
	}

	public void spendTime() {
		try {
			Thread.sleep(5000/getSpeed());
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
