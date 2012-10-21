package com.jrts.agents;

import java.io.IOException;

import jade.core.AID;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.WakerBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.lang.acl.UnreadableException;
import jade.wrapper.AgentController;
import jade.wrapper.ControllerException;
import jade.wrapper.PlatformController;

import com.jrts.O2Ainterfaces.Team;
import com.jrts.common.GameConfig;
import com.jrts.common.GoalPriority;
import com.jrts.common.ResourcesContainer;
import com.jrts.common.UnitFactory;
import com.jrts.environment.Perception;
import com.jrts.environment.Position;
import com.jrts.environment.World;
import com.jrts.environment.WorldMap;
import com.jrts.messages.GoalLevels;


@SuppressWarnings("serial")
public class MasterAI extends JrtsAgent implements Team {
	
	AID resourceAID, militaryAID;
	
	WorldMap worldMap;
	
	GoalPriority resources;
	ResourcesContainer resourcesContainer;
	UnitFactory unitFactory;
	
	public MasterAI() {
		registerO2AInterface(Team.class, this);
	}

	protected void setup(){
		super.setup();
		World world = World.getInstance();
		worldMap = new WorldMap(world.getRows(), world.getCols());
		setTeamName(getAID().getLocalName());
		world.addTeam(getTeamName());
		
		resourceAID = new AID(getTeamName() + "-resourceAI", AID.ISLOCALNAME);
		militaryAID = new AID(getTeamName() + "-militaryAI", AID.ISLOCALNAME);

		//create ResourceAI
		// get a container controller for creating new agents
		PlatformController container = getContainerController();
		AgentController militaryAI, resourceAI, df;
		try {
			df = container.createNewAgent(getTeamDF().getLocalName(), "jade.domain.df", null);
			df.start();
			
			unitFactory = new UnitFactory(getTeamName(), getContainerController());
			unitFactory.start();
			
			resourcesContainer = new ResourcesContainer(GameConfig.STARTUP_WOOD, GameConfig.STARTUP_FOOD);
			
			Object[] arg = new Object[3];
			arg[0] = getTeamName();
			arg[1] = unitFactory;
			arg[2] = resourcesContainer;
			
			resourceAI = container.createNewAgent(resourceAID.getLocalName(), ResourceAI.class.getName(), arg);
			resourceAI.start();
			militaryAI = container.createNewAgent(militaryAID.getLocalName(), MilitaryAI.class.getName(), arg);
			militaryAI.start();
		} catch (ControllerException e) {
			e.printStackTrace();
		}
		
		// listen for world map request and answer
		addBehaviour(new CyclicBehaviour() {
			@Override
			public void action() {
				MessageTemplate mt = MessageTemplate.MatchConversationId(WorldMap.class.getSimpleName());
				ACLMessage msg = receive(mt);
				if (msg != null) {
					ACLMessage reply = msg.createReply();
					try {
						reply.setContentObject(worldMap);
					} catch (IOException e) {
						e.printStackTrace();
					}
					send(reply);
				} else {
					block();
				}
			}
		});
		
		//updated world map according to unit messages
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
		
		
		addBehaviour(new WakerBehaviour(this, 2000) {
			@Override
			protected void handleElapsedTimeout() {
				setResourcesGoalPriority(GoalPriority.HIGH);
			}
		});
	}
	
	@Override
	protected void updatePerception() {
		World world = World.getInstance();
		//the area near the building is always visible
		Position citycenter = world.getCityCenter(getTeamName());
		Perception center = world.getPerception(citycenter, GameConfig.CITY_CENTER_SIGHT);
		//TODO maybe do something if an enemy is detected
		//TODO check if the main building has been destroyed
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

	public void setResourcesGoalPriority(GoalPriority resources) {
		this.resources = resources;
		notifyGoalChanges();
	}
	
	private void notifyGoalChanges() {
		ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
		msg.setConversationId(GoalLevels.class.getSimpleName());
		msg.addReceiver(militaryAID);
		msg.addReceiver(resourceAID);
		try {
			msg.setContentObject(new GoalLevels(resources));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		send(msg);
	}
	
	
}