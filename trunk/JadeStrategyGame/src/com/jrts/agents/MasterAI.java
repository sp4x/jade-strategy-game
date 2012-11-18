package com.jrts.agents;

import jade.core.AID;
import jade.wrapper.AgentController;
import jade.wrapper.ControllerException;
import jade.wrapper.PlatformController;

import com.jrts.O2Ainterfaces.Team;
import com.jrts.common.GameConfig;
import com.jrts.common.GoalPriority;
import com.jrts.common.ResourcesContainer;
import com.jrts.common.UnitFactory;
import com.jrts.common.Utils;
import com.jrts.environment.CellType;
import com.jrts.environment.Perception;
import com.jrts.environment.Position;
import com.jrts.environment.World;
import com.jrts.environment.WorldMap;
import com.jrts.messages.EnemySighting;
import com.jrts.messages.GoalLevels;
import com.jrts.messages.MessageSubject;
import com.jrts.messages.Notification;

@SuppressWarnings("serial")
public class MasterAI extends JrtsAgent implements Team {

	public enum Nature {
		AGGRESSIVE, 
		AVERAGE, 
		DEFENSIVE
	}

	AID resourceAID, militaryAID;

	final Nature nature;
	GoalLevels goalLevels;
	ResourcesContainer resourcesContainer;
	UnitFactory unitFactory;
	WorldMap worldMap;

	Position cityCenter;

	public MasterAI() {
		registerO2AInterface(Team.class, this);
		int die = Utils.random.nextInt(3);
		nature = die == 0 ? Nature.AGGRESSIVE : die == 1 ? Nature.AVERAGE : Nature.DEFENSIVE;
	}

	protected void setup() {
		super.setup();
		World world = World.getInstance();
		worldMap = new WorldMap(world.getRows(), world.getCols());
		setTeamName(getAID().getLocalName());

		this.cityCenter = (Position) getArguments()[0];

		resourceAID = new AID(getTeamName() + "-resourceAI", AID.ISLOCALNAME);
		militaryAID = new AID(getTeamName() + "-militaryAI", AID.ISLOCALNAME);

		PlatformController container = getContainerController();
		AgentController militaryAI, resourceAI, df;
		try {
			df = container.createNewAgent(getTeamDF().getLocalName(), "jade.domain.df", null);
			df.start();

			unitFactory = new UnitFactory(getTeamName(), getContainerController(), cityCenter);
			unitFactory.start();

			resourcesContainer = new ResourcesContainer(GameConfig.STARTUP_WOOD, GameConfig.STARTUP_FOOD);

			Object[] arg = {getTeamName(), unitFactory, resourcesContainer, worldMap, cityCenter, nature};

			resourceAI = container.createNewAgent(resourceAID.getLocalName(), ResourceAI.class.getName(), arg);
			resourceAI.start();
			militaryAI = container.createNewAgent(militaryAID.getLocalName(), MilitaryAI.class.getName(), arg);
			militaryAI.start();
		} catch (ControllerException e) {
			e.printStackTrace();
		}

		setDefaultGoals();
	}

	public void setDefaultGoals() {
		switch (nature) {
		case AGGRESSIVE:
			goalLevels = new GoalLevels(GoalPriority.HIGH, GoalPriority.MEDIUM, GoalPriority.LOW, GoalPriority.HIGH);
			break;
		case AVERAGE:
			goalLevels = new GoalLevels(GoalPriority.HIGH, GoalPriority.LOW, GoalPriority.MEDIUM, GoalPriority.MEDIUM);
			break;
		case DEFENSIVE:
			goalLevels = new GoalLevels(GoalPriority.HIGH, GoalPriority.LOW, GoalPriority.HIGH, GoalPriority.LOW);
		default:
			break;
		}
		notifyGoalChanges();
	}
	
	@Override
	protected void updatePerception() {
		World world = World.getInstance();
		// the area near the building is always visible
		Perception center = world.getPerception(cityCenter, GameConfig.CITY_CENTER_SIGHT);
		// TODO maybe do something if an enemy is detected
		// TODO check if the main building has been destroyed
		worldMap.update(center);
	}

	/** O2A methods (for use in non-agent java objects) */

	@Override
	public int getFood() {
		return resourcesContainer.getFood();
	}

	@Override
	public int getWood() {
		return resourcesContainer.getWood();
	}

	@Override
	public String getTeamName() {
		return getLocalName();
	}

	private void notifyGoalChanges() {
		sendNotification(Notification.GOAL_LEVELS, goalLevels, militaryAID);
		sendNotification(Notification.GOAL_LEVELS, goalLevels, resourceAID);
	}

	@Override
	public WorldMap getWorldMap() {
		return this.worldMap;
	}

	@Override
	protected void handleNotification(Notification n) {
		if (n.getSubject().equals(Notification.PERCEPTION)) {
			Perception info = (Perception) n.getContentObject();
			worldMap.update(info);
			
		} else if (n.getSubject().equals(Notification.ENEMY_SIGHTED)) {
			EnemySighting e = (EnemySighting) n.getContentObject();
			// TODO save it
			
		} else if (n.getSubject().equals(Notification.NO_MORE_RESOURCE)) {
			CellType resourceType = (CellType) n.getContentObject();
			// TODO save it
		}
	}

	@Override
	protected Object handleRequest(String requestSubject) {
		if (requestSubject.equals(MessageSubject.GET_CITY_CENTER_POSITION))
			return cityCenter;
		else if (requestSubject.equals(MessageSubject.GET_WORLD_MAP))
			return worldMap;
		return null;
	}
}