package com.jrts.agents;

import jade.core.AID;
import jade.core.behaviours.TickerBehaviour;
import jade.wrapper.AgentController;
import jade.wrapper.ControllerException;
import jade.wrapper.PlatformController;

import java.util.logging.Level;

import com.jrts.O2Ainterfaces.Team;
import com.jrts.common.GameConfig;
import com.jrts.common.GoalPriority;
import com.jrts.common.ResourcesContainer;
import com.jrts.common.UnitFactory;
import com.jrts.common.Utils;
import com.jrts.environment.Cell;
import com.jrts.environment.MasterPerception;
import com.jrts.environment.Perception;
import com.jrts.environment.Position;
import com.jrts.environment.World;
import com.jrts.environment.WorldMap;
import com.jrts.gui.MainFrame;
import com.jrts.messages.EnemySighting;
import com.jrts.messages.GoalLevels;
import com.jrts.messages.MessageSubject;
import com.jrts.messages.Notification;
import com.jrts.scorer.AttackScorer;
import com.jrts.scorer.DefenceScorer;
import com.jrts.scorer.ExplorationScorer;
import com.jrts.scorer.ResourceScorer;

@SuppressWarnings("serial")
public class MasterAI extends JrtsAgent implements Team {

	private Level logLevel = Level.FINE;

	public enum Nature {
		AGGRESSIVE, AVERAGE, DEFENSIVE
	}

	AID resourceAID, militaryAID;

	GoalLevels goalLevels;

	ResourceScorer resourceScorer;
	AttackScorer attackScorer;
	DefenceScorer defenceScorer;
	ExplorationScorer explorationScorer;
	
	AgentController militaryAI, resourceAI, df;
	
	UnitFactory unitFactory;

	MasterPerception perception = new MasterPerception();
	
	int teamDeceasedNum = 0;

	public MasterAI() {
		registerO2AInterface(Team.class, this);
		int die = Utils.random.nextInt(3);
		nature = die == 0 ? Nature.AGGRESSIVE : die == 1 ? Nature.AVERAGE
				: Nature.DEFENSIVE;
	}

	protected void setup() {
		super.setup();
		World world = World.getInstance();
		perception.setWorldMap(new WorldMap(world.getRows(), world
				.getCols()));
		setTeamName(getAID().getLocalName());

		perception.setCityCenter((Position) getArguments()[0]);
		perception.setMeetingPoint((Position) getArguments()[1]);

		resourceAID = new AID(getTeamName() + "-resourceAI", AID.ISLOCALNAME);
		militaryAID = new AID(getTeamName() + "-militaryAI", AID.ISLOCALNAME);

		PlatformController container = getContainerController();
		try {
			df = container.createNewAgent(getTeamDF().getLocalName(),
					"jade.domain.df", null);
			df.start();

			perception.setTeamDF(getTeamDF());
			
			perception.setNumTeams(World.getInstance().getNumberOfTeams());

			unitFactory = new UnitFactory(getTeamName(),
					getContainerController(), perception.getCityCenter(),
					perception.getMeetingPoint(), nature);
			unitFactory.start();

			perception.setResourcesContainer(new ResourcesContainer(
					GameConfig.STARTUP_WOOD, GameConfig.STARTUP_FOOD));

			Object[] arg = { getTeamName(), unitFactory,
					perception.getResourcesContainer(),
					perception.getCityCenter(), nature };

			resourceAI = container.createNewAgent(resourceAID.getLocalName(),
					ResourceAI.class.getName(), arg);
			resourceAI.start();
			militaryAI = container.createNewAgent(militaryAID.getLocalName(),
					MilitaryAI.class.getName(), arg);
			militaryAI.start();
		} catch (ControllerException e) {
			e.printStackTrace();
		}

		setDefaultGoals();

		this.attackScorer = new AttackScorer(nature, perception);
		this.defenceScorer = new DefenceScorer(nature, perception);
		this.explorationScorer = new ExplorationScorer(nature, perception);
		this.resourceScorer = new ResourceScorer(nature, perception);

		addBehaviour(new TickerBehaviour(this, GameConfig.GOALS_UPDATE) {

			@Override
			protected void onTick() {
				goalLevels.setAttack(attackScorer.calculatePriority());
				goalLevels.setDefence(defenceScorer.calculatePriority());
				goalLevels.setExploration(explorationScorer.calculatePriority());
				goalLevels.setResources(resourceScorer.calculatePriority());
				notifyGoalChanges();
				perception.clean();
			}
		});
	}

	public void setDefaultGoals() {
		// just for initialization they will change soon
		goalLevels = new GoalLevels(GoalPriority.LOW, GoalPriority.LOW,
				GoalPriority.LOW, GoalPriority.LOW);
		notifyGoalChanges();
	}

	@Override
	protected void updatePerception() {
		World world = World.getInstance();
		perception.setNumTeams(world.getNumberOfTeams());
		// check if city center was destroyed
		Cell cityCenterCell = world.getCell(perception.getCityCenter());
		logger.log(logLevel,
				getTeamName() + " energy: " + cityCenterCell.getEnergy());
		if (cityCenterCell.getEnergy() <= 0) {
			logger.log(logLevel, "TEAM DELETED");
			decease();
		}
	}

	public synchronized void decease() {
		// TODO maybe send a notification to other teams (like age of empires)

		MainFrame.getInstance().removeTeam(getTeamName());
		
		unitFactory.setFinished(true);
		
		// send notification of decease to military an resource ai (they will
		// forward it to their units)
		sendNotification(Notification.TEAM_DECEASED, null, militaryAID);
		sendNotification(Notification.TEAM_DECEASED, null, resourceAID);

		// NOW IT WAITS FOR NOTIFICATION FROM MILITARY AND RESOURCE, THEN DIES (see handlenotification)
	}

	/** O2A methods (for use in non-agent java objects) */

	@Override
	public int getFood() {
		return perception.getResourcesContainer().getFood();
	}

	@Override
	public int getWood() {
		return perception.getResourcesContainer().getWood();
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
		return perception.getWorldMap();
	}

	@Override
	protected void handleNotification(Notification n) {
		if (n.getSubject().equals(Notification.PERCEPTION)) {
			Perception info = (Perception) n.getContentObject();
			perception.getWorldMap().update(info);

		} else if (n.getSubject().equals(Notification.ENEMY_SIGHTED)) {
			EnemySighting e = (EnemySighting) n.getContentObject();
			perception.getEnemySightings().add(e);

		} else if (n.getSubject().equals(Notification.UNAVAILABLE_RESOURCE)) {
			perception.setAlertNoMoreResources(true);

		} else if (n.getSubject().equals(Notification.RESOURCES_FOUND)) {
			perception.setAlertNoMoreResources(false);

		} else if (n.getSubject().equals(Notification.UNIT_DEATH)) {
			String className = (String) n.getContentObject();
			if (className.equals(Worker.class.getCanonicalName()))
				perception.numDeadWorkers++;
			else if (className.equals(Soldier.class.getCanonicalName())) {
				perception.numDeadSoldiers++;
			}

		} else if (n.getSubject().equals(Notification.UNIT_UNDER_ATTACK)) {
			Position where = (Position) n.getContentObject();
			perception.getThreats().add(where);

		} else if (n.getSubject().equals(Notification.CITYCENTER_UNDER_ATTACK)) {
			perception.setAlertCityCenterUnderAttack(true);
			
		} else if (n.getSubject().equals(Notification.TEAM_DECEASED)) {
			teamDeceasedNum++;
			if (teamDeceasedNum >= 2) {
				doDelete();
			}
		}
	}

	@Override
	protected Object handleRequest(String requestSubject) {
		if (requestSubject.equals(MessageSubject.GET_CITY_CENTER_POSITION))
			return perception.getCityCenter();
		else if (requestSubject.equals(MessageSubject.GET_WORLD_MAP))
			return perception.getWorldMap();
		return null;
	}

	public ResourcesContainer getResourcesContainer() {
		return perception.getResourcesContainer();
	}

	@Override
	public int getEnergy() {
		Position cityCenter = perception.getCityCenter();
		return World.getInstance().getCell(cityCenter).getEnergy();
	}

	@Override
	public int getQueueWorkerCount() {
		return unitFactory.getQueueWorkerCount();
	}

	@Override
	public int getQueueSoldierCount() {
		return unitFactory.getQueueSoldierCount();
	}

	@Override
	public GoalLevels getGoalLevels() {
		return this.goalLevels;
	}

	@Override
	public int getNumDeadWorkers() {
		return perception.numDeadWorkers;
	}

	@Override
	public int getNumDeadSoldiers() {
		return perception.numDeadSoldiers;
	}
	
	@Override
	protected void takeDown() {
		// clean the cell of citycenter in the floor
		World.getInstance().removeTeam(getTeamName());
		super.takeDown();
	}

	@Override
	public String getProgressTrainingWorker() {
		return "(" + unitFactory.getWorkerTrainingProgress() + "%)";
	}
	
	@Override
	public String getProgressTrainingSoldier() {
		return "(" + unitFactory.getSoldierTrainingProgress() + "%)";
	}
}