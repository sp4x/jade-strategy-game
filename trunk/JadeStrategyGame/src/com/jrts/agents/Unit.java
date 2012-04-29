package com.jrts.agents;

import jade.core.AID;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;

import java.io.IOException;
import java.util.Random;


import com.jrts.behaviours.FollowPathBehaviour;
import com.jrts.behaviours.LookForEnemy;
import com.jrts.common.GameConfig;
import com.jrts.environment.Direction;
import com.jrts.environment.Floor;
import com.jrts.environment.Position;
import com.jrts.environment.World;

@SuppressWarnings("serial")
public abstract class Unit extends JrtsAgent {

	private Position position = null;
	int life;
	int speed;
	int forceOfAttack;
	int sight;
	
	private ServiceDescription basicService;

	public Unit() {
	}

	Unit(Position position) {
		super();
		this.position = position;
	}

	@Override
	protected void setup() {
		basicService = new ServiceDescription();
		basicService.setName(getAID().getName());
		basicService.setType(getClass().getName());
		addBehaviour(new LookForEnemy(this, 500));
	}
	
	public void goThere(Position p) {
		goThere(p.getRow(), p.getCol());
	}

	public void goThere(int x, int y) {
		updatePerception();
		addBehaviour(new FollowPathBehaviour(this, x, y, GameConfig.UNIT_MOVING_ATTEMPTS));
	}

	@Override
	protected void updatePerception() {
		Floor newPerception = World.getInstance().getPerception(getPosition(), sight);
		life -= newPerception.get(getPosition()).getDamage(); //update life
		updateLocalPerception(newPerception);
		//send perception to Master
		ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
		msg.addReceiver(new AID(team, AID.ISLOCALNAME));
		try {
			msg.setContentObject(newPerception);
			send(msg);
		} catch (IOException e) {
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

	public void spendTime() {
		try {
			Thread.sleep(GameConfig.DEFAULT_REFRESH_TIME/getSpeed());
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public void spendRandomTime() {
		Random r = new Random();
		try {
			Thread.sleep(GameConfig.DEFAULT_REFRESH_TIME*r.nextInt(5));
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	private void setStatus(String newStatus, boolean deletePrevious) {
		DFAgentDescription dfd = new DFAgentDescription();
		dfd.setName(getAID());
		ServiceDescription sd = new ServiceDescription();
		sd.setName(getAID().getName());
		sd.setType(newStatus);
		dfd.addServices(basicService);
		dfd.addServices(sd);
		// register the description with the DF
		register(dfd, deletePrevious);
	}
	
	protected void setStatus(String newStatus) {
		setStatus(newStatus, false);
	}
	
	public void switchStatus(String newStatus) {
		setStatus(newStatus, true);
	}
	
	public boolean isFriend(String aid) {
		DFAgentDescription desc = new DFAgentDescription();
		desc.setName(new AID(aid, AID.ISLOCALNAME));
		try {
			return DFService.search(this, getTeamDF(), desc).length == 0;
		} catch (FIPAException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
	}
	
}
