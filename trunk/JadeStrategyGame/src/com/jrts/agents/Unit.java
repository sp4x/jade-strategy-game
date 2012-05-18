package com.jrts.agents;

import jade.core.AID;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;

import java.util.Random;

import com.jrts.api.IUnit;
import com.jrts.behaviours.CheckReceivedAttacks;
import com.jrts.behaviours.FollowPathBehaviour;
import com.jrts.behaviours.LookForEnemy;
import com.jrts.behaviours.ReceiveOrders;
import com.jrts.common.GameConfig;
import com.jrts.environment.CellType;
import com.jrts.environment.Direction;
import com.jrts.environment.Floor;
import com.jrts.environment.Position;
import com.jrts.environment.World;

@SuppressWarnings("serial")
public abstract class Unit extends JrtsAgent implements IUnit {

	private Position position = null;
	private String status;
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
		super.setup();
		Object[] args = getArguments();
		if (args != null) {
			setPosition((Position) args[0]);
			setTeam((String) args[1]);
		}
		World.getInstance().setReference(this);
		basicService = new ServiceDescription();
		basicService.setName(getAID().getName());
		basicService.setType(getClass().getName());
		updatePerception();
		addBehaviour(new CheckReceivedAttacks(this));
		addBehaviour(new LookForEnemy(this, 2000));
		addBehaviour(new ReceiveOrders(this));
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
		updateLocalPerception(newPerception);
		//send perception to Master
		sendPerception(newPerception, masterAID);
	}

	public boolean move(Direction dir){
		return World.getInstance().move(this.position, dir);
	}

	@Override
	public Position getPosition() {
		return position;
	}

	protected void setPosition(Position position) {
		if (this.position == null)
			this.position = position;
	}

	@Override
	public int getLife() {
		return life;
	}

	protected void setLife(int life) {
		if(life>0)
			this.life = life;
		else{
			this.life = 0;
			die();
		}
	}
	
	private void die() {
		World.getInstance().agentDies(getPosition());
		doDelete();
	}

	public void decreaseLife(int damage){
		setLife(getLife() - damage);
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
		status = newStatus;
	}
	
	protected void setStatus(String newStatus) {
		setStatus(newStatus, false);
	}
	
	@Override
	public String getStatus() {
		return status;
	}
	
	@Override
	public String getId() {
		return getAID().getLocalName();
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
	
	public Position findNearest(CellType type) {
		double distance = Double.MAX_VALUE;
		Position nearestPosition = null;
		for (int i = 0; i < perception.getRows(); i++) {
			for (int j = 0; j < perception.getCols(); j++) {
				Position p = new Position(i, j);
				if (perception.get(p).getType() == type) {
					double currentDistance = position.distance(p);
					if (currentDistance < distance) {
						nearestPosition = p;
						distance = currentDistance;
					}
				}
			}
		}
		return nearestPosition;
	}
	
}
