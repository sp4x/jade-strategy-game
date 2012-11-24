package com.jrts.agents;

import java.util.logging.Level;

import jade.core.AID;
import jade.core.behaviours.Behaviour;

import com.jrts.O2Ainterfaces.IUnit;
import com.jrts.agents.MasterAI.Nature;
import com.jrts.behaviours.BehaviourWrapper;
import com.jrts.behaviours.FollowPathBehaviour;
import com.jrts.behaviours.UnitBehaviour;
import com.jrts.common.AgentStatus;
import com.jrts.common.GameConfig;
import com.jrts.environment.CellType;
import com.jrts.environment.Direction;
import com.jrts.environment.Perception;
import com.jrts.environment.Position;
import com.jrts.environment.World;
import com.jrts.environment.WorldMap;
import com.jrts.messages.EnemySighting;
import com.jrts.messages.MessageSubject;
import com.jrts.messages.Notification;
import com.jrts.messages.Order;

public abstract class Unit extends JrtsAgent implements IUnit {

	private static final long serialVersionUID = -1503577834258923742L;
	
	private Level logLevel = Level.FINE;
	
	private Position cityCenter;
	private Position position = null;
	private String status;
	
	//perception
	Perception perception;
	EnemySighting lastEnemySighting;
	
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
		behaviourWrapper = new BehaviourWrapper(this);
	}

	@Override
	protected void setup() {
		super.setup();
		Position meetingPoint = null;
		Object[] args = getArguments();
		if (args != null) {
			int i = 0;
			setCityCenter((Position) args[i++]);
			setPosition((Position) args[i++]);
			meetingPoint = (Position) args[i++];
			setTeamName((String) args[i++]);
			setNature((Nature) args[i++]);
		}
		id = getAID().getLocalName();
		getTeamDF().registerUnit(this);
		addBehaviour(behaviourWrapper);
		
		
		goThere(meetingPoint);
	}
	
	private void setCityCenter(Position position) {
		this.cityCenter = position;
	}


	@Override
	public void addBehaviour(Behaviour b) {
		if (b instanceof UnitBehaviour)
			behaviourWrapper.wrap((UnitBehaviour) b);
		else
			super.addBehaviour(b);
	}

	public void goThere(Position p) {
		logger.log(logLevel, getAID().getName() + ":Go there " + p);
		addBehaviour(new FollowPathBehaviour(this, p));
	}

	public AID getMasterAID() {
		return new AID(getTeamName(), AID.ISLOCALNAME);
	}

	public AID getResourceAID() {
		return new AID(getTeamName() + "-resourceAI", AID.ISLOCALNAME);
	}

	public AID getMilitaryAID() {
		return new AID(getTeamName() + "-militaryAI", AID.ISLOCALNAME);
	}
	
	@Override
	protected void updatePerception() {
		perception = World.getInstance().getPerception(position, sight);
		
		// send perception to MasterAi
		sendNotification(Notification.PERCEPTION, perception, getMasterAID());
		
		EnemySighting enemies = lookForEnemies(position, sight, perception);
		
		lastEnemySighting = enemies;
		
		if (!enemies.isEmpty()) {
			onEnemySighted(enemies);
		}
	}

	public boolean move(Direction dir) {
		if (World.getInstance().move(position, dir)) {
			position = position.step(dir);
			logger.log(logLevel, id + ":moved into " + position);
			return true;
		}
		return false;
	}

	@Override
	public Position getPosition() {
		return position;
	}

	protected void setPosition(Position position) {
		if (position != null)
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

	public void terminate() {
		World.getInstance().killUnit(this);
		removeAllBehaviours();
		doDelete();
	}
	
	protected abstract void die(); 

	@Override
	public void decreaseLife(int damage) {
		setLife(getLife() - damage);
		sendNotification(Notification.UNIT_UNDER_ATTACK, getPosition(), getMilitaryAID());
	}
	
	public abstract void onAttacNotification(String attacker);

	public Position getCityCenter() {
		return cityCenter;
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
			Thread.sleep(GameConfig.UNIT_WAIT_TIME / speed);
		} catch (InterruptedException e) {}
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
		getTeamDF().registerUnit(this, newStatus);
		status = newStatus;
	}

	public WorldMap requestMap() {
		return (WorldMap) sendRequest(MessageSubject.GET_WORLD_MAP, getMasterAID());
	}

	public Position requestCityCenterPosition() {
		return (Position) sendRequest(MessageSubject.GET_CITY_CENTER_POSITION, getMasterAID());
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

	@Override
	protected Object handleRequest(String requestSubject) {
		return null;
	}
	
	@Override
	protected void handleNotification(Notification n) {
		if (n.getSubject().equals(Notification.TEAM_DECEASED)) {
			removeAllBehaviours();
			World.getInstance().killUnit(this);
			this.doDelete();
		} else if (n.getSubject().equals(Notification.ATTACK)) {
			String attacker = (String) n.getContentObject();
			onAttacNotification(attacker);
		} else if (n.getSubject().equals(Notification.ORDER)) {
			Order order = (Order) n.getContentObject();
			takeOrder(order);
		}
	}

	public void takeOrder(Order order) {
		if (order.getNextStatus().equals(AgentStatus.FREE)) {
			goThere(cityCenter);
			switchStatus(AgentStatus.FREE);
		}
	}

	public abstract void onEnemySighted(EnemySighting list);

	public EnemySighting getLastEnemySighting() {
		return lastEnemySighting;
	}
	
}
