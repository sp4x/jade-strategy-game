package com.jrts.agents;

import jade.core.AID;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.lang.acl.UnreadableException;
import jade.wrapper.AgentController;
import jade.wrapper.ControllerException;
import jade.wrapper.PlatformController;

import java.io.IOException;

import com.jrts.O2Ainterfaces.Team;
import com.jrts.common.GameConfig;
import com.jrts.common.GoalPriority;
import com.jrts.common.ResourcesContainer;
import com.jrts.common.UnitFactory;
import com.jrts.common.Utils;
import com.jrts.environment.Perception;
import com.jrts.environment.Position;
import com.jrts.environment.World;
import com.jrts.environment.WorldMap;
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

	protected void setup() {
		super.setup();
		World world = World.getInstance();
		worldMap = new WorldMap(world.getRows(), world.getCols());
		setTeamName(getAID().getLocalName());

		this.cityCenter = (Position) getArguments()[0];

		resourceAID = new AID(getTeamName() + "-resourceAI", AID.ISLOCALNAME);
		militaryAID = new AID(getTeamName() + "-militaryAI", AID.ISLOCALNAME);

		// create ResourceAI
		// get a container controller for creating new agents
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

		// updated world map according to unit messages
		addBehaviour(new CyclicBehaviour() {
			@Override
			public void action() {
				MessageTemplate mt = MessageTemplate.MatchConversationId(Perception.class.getSimpleName());
				ACLMessage msg = receive(mt);
				if (msg != null) {
					try {
						Perception info = (Perception) msg.getContentObject();
						worldMap.update(info);
					} catch (UnreadableException e) {
						e.printStackTrace();
					}
				} else {
					block();
				}

			}
		});

		setDefaultGoals();

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
	protected void handleNotification(Notification notification) {
		// TODO do something
	}

	@Override
	protected void handleRequest(ACLMessage msg) throws IOException {
		String messageSubject = msg.getConversationId();
		if (messageSubject.equals(MessageSubject.GET_CITY_CENTER_POSITION))
			msg.setContentObject(cityCenter);
		else if (messageSubject.equals(MessageSubject.GET_WORLD_MAP))
			msg.setContentObject(worldMap);
	}
}