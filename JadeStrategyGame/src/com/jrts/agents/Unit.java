package com.jrts.agents;

import jade.core.AID;
import jade.core.Agent;
import jade.lang.acl.ACLMessage;

import java.io.IOException;
import java.util.List;

import behaviours.FollowPathBehaviour;

import com.jrts.environment.Direction;
import com.jrts.environment.Floor;
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
	int sight;
		
	Floor perception;
	
	public Unit() {}
	
	Unit(Position position) {
		super();
		this.position = position;
	}

	public void goThere(Position p) {
		goThere(p.getRow(), p.getCol());
	}

	public void goThere(int x, int y) {
		List<Direction> path = World.getInstance().getPath(this.position, x, y);
		addBehaviour(new FollowPathBehaviour(this, path, x, y));
	}
	
	protected void updatePerception() {
		perception = World.getInstance().getPerception(getPosition(), sight);
		//send perception to Master
		ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
		msg.addReceiver(new AID(team, AID.ISLOCALNAME));
		try {
			msg.setContentObject(perception);
			send(msg);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public boolean move(Direction dir){
		boolean success = World.getInstance().move(this.position, dir);
		if (success)
			updatePerception();
		return success;
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

	public int getSight() {
		return sight;
	}

	protected void setSight(int sight) {
		this.sight = sight;
	}

	public String getTeam() {
		return team;
	}

	protected void setTeam(String team) {
		this.team = team;
	}

	public Floor getPerception() {
		return perception;
	}

	public void spendTime() {
		try {
			Thread.sleep(5000/getSpeed());
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
