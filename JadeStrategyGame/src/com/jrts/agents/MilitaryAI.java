package com.jrts.agents;

import jade.core.AID;
import jade.core.behaviours.WakerBehaviour;
import jade.domain.FIPAAgentManagement.DFAgentDescription;

import java.util.ArrayList;

import com.jrts.behaviours.PatrolBehaviour;
import com.jrts.behaviours.UpdateUnitTable;
import com.jrts.common.AgentStatus;
import com.jrts.common.GameConfig;
import com.jrts.common.GoalPriority;
import com.jrts.common.Utils;
import com.jrts.environment.Direction;
import com.jrts.environment.Perception;
import com.jrts.environment.World;
import com.jrts.messages.EnemySighting;
import com.jrts.messages.Notification;
import com.jrts.messages.Order;


public class MilitaryAI extends GoalBasedAI {
	private static final long serialVersionUID = 9114684864072759345L;

	int soldierCounter = 0;
	
	int lastCityCenterLife = GameConfig.BUILDING_ENERGY;
	
	@Override
	protected void setup() {
		super.setup();
		
		addBehaviour(new UpdateUnitTable(this, Soldier.class));

		addBehaviour(new WakerBehaviour(this, 15000) {
			private static final long serialVersionUID = 1746608629262055814L;
			@Override
			protected void handleElapsedTimeout() {
				requestSoldierCreation();
			}
		});
		
//		addBehaviour(new WakerBehaviour(this, 5000) {
//			private static final long serialVersionUID = 1746608629262055814L;
//			@Override
//			protected void handleElapsedTimeout() {
//				requestSoldierCreation();
//			}
//		});
//		
//		addBehaviour(new WakerBehaviour(this, 7000) {
//			private static final long serialVersionUID = 1746608629262055814L;
//			@Override
//			protected void handleElapsedTimeout() {
//				requestSoldierCreation();
//			}
//		});
		
		addBehaviour(new WakerBehaviour(this, 10000) {
			private static final long serialVersionUID = 1746608629262055814L;

			@Override
			protected void handleElapsedTimeout() {
				addExplorer();
			}
		});
//		
//		addBehaviour(new WakerBehaviour(this, 15000) {
//			private static final long serialVersionUID = 1746608629262055814L;
//
//			@Override
//			protected void handleElapsedTimeout() {
//				addPatroler();
//			}
//		});
		
	}

	public void requestSoldierCreation()
	{
		if (resourcesContainer.isThereEnoughFood(GameConfig.SOLDIER_FOOD_COST) && 
				resourcesContainer.isThereEnoughWood(GameConfig.SOLDIER_WOOD_COST)
				/*&& getTeamDF().countUnits() < GameConfig.POPULATION_LIMIT*/) {
						
			DFAgentDescription[] workers = this.getTeamDF().searchByUnitStatus(Worker.class, AgentStatus.FREE);
			if(workers.length > 0)
			{
				logger.log(logLevel, "ABBASTANZA LAVORATORI");
				
				DFAgentDescription worker = workers[0];	
				giveOrder(worker.getName(), new Order(AgentStatus.GO_UPGRADING));
				
				resourcesContainer.removeFood(GameConfig.SOLDIER_FOOD_COST);
				resourcesContainer.removeWood(GameConfig.SOLDIER_WOOD_COST);
			}
		}
	}

	public void trainSoldier()
	{		
		unitFactory.trainUnit(Soldier.class);
		soldierCounter++;
	}
	
	public void addExplorer()
	{
		AID soldier = this.getUnitTable().getAFreeUnit();
		if(soldier != null)
			giveOrder(soldier, new Order(AgentStatus.EXPLORING));
	}
	
	public void addPatroler()
	{
		AID soldier = this.getUnitTable().getAFreeUnit();
		if(soldier != null){
			
			Direction angle = Utils.getMapAnglePosition(cityCenter);
			
			Order order = new Order(AgentStatus.PATROLING);
			if(angle.equals(Direction.LEFT_UP))
			{
				if(Utils.random.nextBoolean())
					order.setPatrolDirection(Direction.RIGHT);
				else
					order.setPatrolDirection(Direction.DOWN);
			} else if(angle.equals(Direction.LEFT_DOWN))
			{
				if(Utils.random.nextBoolean())
					order.setPatrolDirection(Direction.RIGHT);
				else
					order.setPatrolDirection(Direction.UP);
			} else if(angle.equals(Direction.RIGHT_UP)){
				if(Utils.random.nextBoolean())
					order.setPatrolDirection(Direction.LEFT);
				else
					order.setPatrolDirection(Direction.DOWN);
			} else if(angle.equals(Direction.RIGHT_DOWN)){
				if(Utils.random.nextBoolean())
					order.setPatrolDirection(Direction.LEFT);
				else
					order.setPatrolDirection(Direction.UP);
			}
			
			switch (Utils.random.nextInt(3)) {
			case 0:
				order.setPatrolDistance(PatrolBehaviour.DISTANCE_LITTLE);
				break;
			case 1:
				order.setPatrolDistance(PatrolBehaviour.DISTANCE_MEDIUM);
				break;
			case 2:
				order.setPatrolDistance(PatrolBehaviour.DISTANCE_BIG);
				break;
			}
			
			giveOrder(soldier, order);
		}
	}
	
	@Override
	protected void updatePerception() {
		Perception cityCenterPerception = World.getInstance().getPerception(cityCenter, GameConfig.CITY_CENTER_SIGHT);
		sendNotification(Notification.PERCEPTION, cityCenterPerception, getMasterAID());
		
		EnemySighting enemies = lookForEnemies(cityCenter, GameConfig.CITY_CENTER_SIGHT, cityCenterPerception);
		sendNotification(Notification.ENEMY_SIGHTED, enemies, getMasterAID());
		if (!enemies.isEmpty()) {
			onEnemySighting(enemies);
		}
		
		int cityCenterLife = cityCenterPerception.get(cityCenter).getResourceEnergy();
		if (cityCenterLife < lastCityCenterLife) {
			lastCityCenterLife = cityCenterLife;
			underAttack();
		}
	}

	private void underAttack() {
		sendNotification(Notification.CITYCENTER_UNDER_ATTACK, cityCenter, getMasterAID());
		logger.log(logLevel, getTeamName() + " city center under attack!");
		// TODO handle it
	}

	@Override
	public void onGoalsChanged() {
		// TODO Auto-generated method stub
	}

	public void onEnemySighting(EnemySighting e) {
		int numSightedSoldiers = e.getSoldierNumber();
		int distance = (int)e.getSightingPosition().distance(cityCenter);
		ArrayList<AID> freeSoldiers = unitTable.getFreeUnits();
		int numMyFreeSoldier = freeSoldiers.size();
		
		logger.log(logLevel, 
				"\n----------------- Start OnEnemySighting ----------------\n" +
				"EnemySighting received: \n" + 
					e +
				"numSightedSoldiers = "+numSightedSoldiers+"\n"+
				"distance = " + distance +"\n"+
				"numMyFreeSoldier = "+numMyFreeSoldier+"\n");
		
		int x=1, y=1, z=1;
		switch (nature) {
		case AGGRESSIVE:
			x = 3;
			y = 3;
			z = 3;
			break;
		case AVERAGE:
			x = 2;
			y = 2;
			z = 1;
			break;
		case DEFENSIVE:
			x = 1;
			y = 1;
			z = 1;
			break;
		default:
			break;
		}
		
		// numSightedSoldiers * x - distance * goalDifesa / y + numMyFreeSoldier * z
		//          2/3				  10/20		                      5/6
		
		//TODO: Un numero che che rappresneti il livelo dovrebbe essere messo altrove, visto che sarà utilizzato spesso
		int defence = 1;
		if(goalLevels.getDefence().equals(GoalPriority.LOW))
			defence = 1;
		else if(goalLevels.getDefence().equals(GoalPriority.MEDIUM))
			defence = 2;
		else if(goalLevels.getDefence().equals(GoalPriority.HIGH))
			defence = 3;
		
		int heuristic = numSightedSoldiers*x - distance*defence/y + numMyFreeSoldier*z;
		logger.log(logLevel, "CALCOLO EURISTICA, VALORE: " + heuristic + "\n" +
				"----------------- End OnEnemySighting ----------------\n");
	}
	
	@Override
	protected void handleNotification(Notification n) {
		super.handleNotification(n);
		if (n.getSubject().equals(Notification.ENEMY_SIGHTED)) {
			EnemySighting e = (EnemySighting) n.getContentObject();
			// forward notification to masterAi
			sendNotification(n.getSubject(), n.getContentObject(), getMasterAID());
			// decide what to do
			onEnemySighting(e);
			
		} else if (n.getSubject().equals(Notification.UNIT_DEATH)) {
			soldierCounter--;
			sendNotification(Notification.UNIT_DEATH, n.getContentObject(), getMasterAID());
			
		} else if (n.getSubject().equals(Notification.READY_TO_BE_UPGRADED)) {
			this.trainSoldier();
			
		} else if (n.getSubject().equals(Notification.UNIT_UNDER_ATTACK)) {
			sendNotification(Notification.UNIT_UNDER_ATTACK, n.getContentObject(), getMasterAID());
		}
	}
	
	@Override
	protected Object handleRequest(String requestSubject) {
		// TODO Auto-generated method stub
		return null;
	}

	

}
