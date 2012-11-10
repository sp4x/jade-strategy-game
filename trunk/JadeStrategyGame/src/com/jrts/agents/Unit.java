package com.jrts.agents;

import jade.core.AID;
import jade.core.behaviours.Behaviour;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.lang.acl.UnreadableException;

import java.io.IOException;

import com.jrts.O2Ainterfaces.IUnit;
import com.jrts.behaviours.BehaviourWrapper;
import com.jrts.behaviours.FollowPathBehaviour;
import com.jrts.behaviours.LookForEnemy;
import com.jrts.behaviours.ReceiveOrders;
import com.jrts.behaviours.UnitBehaviour;
import com.jrts.common.GameConfig;
import com.jrts.common.Utils;
import com.jrts.environment.CellType;
import com.jrts.environment.Direction;
import com.jrts.environment.Perception;
import com.jrts.environment.Position;
import com.jrts.environment.World;
import com.jrts.environment.WorldMap;

@SuppressWarnings("serial")
public abstract class Unit extends JrtsAgent implements IUnit {

	private Position position = null;
	private String status;
	private Perception perception;
	private DFAgentDescription agentDescription;
	private ServiceDescription basicService;
	
	private BehaviourWrapper behaviourWrapper;

	String id;
	int life;
	int speed = 4;
	int forceOfAttack;
	int sight;

	public Unit() {
		this(null, null);
	}

	public Unit(String id, Position position) {
		super();
		this.position = position;
		this.id = id;
		registerO2AInterface(IUnit.class, this);
		behaviourWrapper = new BehaviourWrapper();
	}

	@Override
	protected void setup() {
		super.setup();
		Object[] args = getArguments();
		if (args != null) {
			setPosition((Position) args[0]);
			setTeamName((String) args[1]);
		} else {

		}
		id = getAID().getLocalName();
		agentDescription = new DFAgentDescription();
		agentDescription.setName(getAID());
		basicService = new ServiceDescription();
		basicService.setName(getAID().getName());
		basicService.setType(getClass().getSimpleName());
		agentDescription.addServices(basicService);
		register(agentDescription, false);
		addBehaviour(new LookForEnemy(this, 2000));
		addBehaviour(new ReceiveOrders(this));
		addBehaviour(behaviourWrapper);
	}
	
	@Override
	public void addBehaviour(Behaviour b) {
		if(b instanceof UnitBehaviour)
			behaviourWrapper.wrap((UnitBehaviour) b);
		else
			super.addBehaviour(b);
	}
	
	public void goThere(Position p) {
		logger.info(getAID().getName() + ":Go there " + p);
		addBehaviour(new FollowPathBehaviour(this, p, GameConfig.UNIT_MOVING_ATTEMPTS));
	}

	public AID getMasterAID() {
		return new AID(getTeamName(), AID.ISLOCALNAME);
	}

	public AID getResourceAID() {
		return new AID(getTeamName() + "-resourceAI", AID.ISLOCALNAME);
	}

	@Override
	protected void updatePerception() {
		perception = World.getInstance().getPerception(getPosition(), sight);
		// send perception to Master
		ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
		msg.setConversationId(Perception.class.getSimpleName());
		msg.addReceiver(getMasterAID());
		try {
			msg.setContentObject(perception);
			send(msg);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public boolean move(Direction dir) {
		if (World.getInstance().move(position, dir)) {
			position = position.step(dir);
			logger.info(id + ":moved into " + position);
			return true;
		}
		return false;
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
		if (life > 0)
			this.life = life;
		else {
			this.life = 0;
			die();
		}
	}

	private void die() {
		World.getInstance().killUnit(this);
		doDelete();
	}

	@Override
	public void decreaseLife(int damage) {
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
			Thread.sleep(GameConfig.REFRESH_TIME / speed);
		} catch (InterruptedException e) {
		}
	}

	public void spendRandomTime() {
		try {
			Thread.sleep(GameConfig.REFRESH_TIME * (Utils.random.nextInt(5)+1));
		} catch (InterruptedException e) {
		}
	}

	@Override
	public String getStatus() {
		return status;
	}

	@Override
	public String getId() {
		return id;
	}

	public void switchStatus(String newStatus) {
		ServiceDescription sd = new ServiceDescription();
		sd.setName(getAID().getName());
		sd.setType(newStatus);
		agentDescription.clearAllServices();
		agentDescription.addServices(basicService);
		agentDescription.addServices(sd);
		// register the description with the DF
		register(agentDescription, true);
		status = newStatus;
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

	public WorldMap requestMap() {
		ACLMessage msg = new ACLMessage(ACLMessage.REQUEST);
		String id = WorldMap.class.getSimpleName();
		msg.setConversationId(id);
		msg.addReceiver(getMasterAID());
		send(msg);
		ACLMessage response = blockingReceive(MessageTemplate
				.MatchConversationId(id));
		try {
			return (WorldMap) response.getContentObject();
		} catch (UnreadableException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public Position findNearest(CellType type) {
		WorldMap map = requestMap();
		return map.findNearest(getPosition(), type);
	}

	public Perception getPerception() {
		return perception;
	}
	
	public int getKnapsack() {
		if (this instanceof Worker) 
			return ((Worker) this).getKnapsack();
		else
			return -1;
	}

}
