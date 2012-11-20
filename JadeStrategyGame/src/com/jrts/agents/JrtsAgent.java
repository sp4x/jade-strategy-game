package com.jrts.agents;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.TickerBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.lang.acl.UnreadableException;

import java.io.IOException;
import java.io.Serializable;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.jrts.common.GameConfig;
import com.jrts.common.TeamDF;
import com.jrts.environment.Cell;
import com.jrts.environment.Perception;
import com.jrts.environment.Position;
import com.jrts.messages.EnemySighting;
import com.jrts.messages.EnemySightingItem;
import com.jrts.messages.Notification;

@SuppressWarnings("serial")
public abstract class JrtsAgent extends Agent {

	public final Logger logger = Logger.getLogger(Agent.class.getName());
	public Level logLevel = Level.FINE;
	
	private String team;

	public JrtsAgent() {
		this.logger.setLevel(Level.WARNING);
	}
	
	LinkedList<Behaviour> behaviours = new LinkedList<Behaviour>();
	
	@Override
	protected void setup() {
		super.setup(); 
		addBehaviour(new TickerBehaviour(this, GameConfig.PERCEPTION_REFRESH) {
			@Override
			protected void onTick() {
				updatePerception();
			}
		});
		addBehaviour(new CyclicBehaviour(this) {
			@Override
			public void action() {
				ACLMessage msg = receive(MessageTemplate.MatchPerformative(ACLMessage.INFORM));
				if (msg != null) {
					try {
						Notification notification = (Notification) msg.getContentObject();
						handleNotification(notification);
					} catch (UnreadableException e) {
						logger.severe("Can't read message " + e);
					}
				} else {
					// if no message is arrived, block the behaviour
					block();
				}
			}
		});
		
		addBehaviour(new CyclicBehaviour(this) {
			
			@Override
			public void action() {
				ACLMessage msg = receive(MessageTemplate.MatchPerformative(ACLMessage.QUERY_REF));
				if (msg != null) {
					try {
						Serializable content = (Serializable) handleRequest(msg.getConversationId());
						ACLMessage response = msg.createReply();
						response.setContentObject(content);
						send(response);
					} catch (IOException e) {
						logger.severe("Can't send message");
					}
				} else {
					// if no message is arrived, block the behaviour
					block();
				}
			}
		});
	}

	@Override
	public void addBehaviour(Behaviour b) {
		behaviours.add(b);
		super.addBehaviour(b);
	}
	
	public LinkedList<Behaviour> getBehaviours() {
		return behaviours;
	}
	
	public void removeAllBehaviours() {
		for (Behaviour b : behaviours) {
			b.block();
			removeBehaviour(b);
		}
	}
	
	protected abstract Object handleRequest(String requestSubject);

	/**
	 * updates the agent perception
	 * 
	 */
	protected abstract void updatePerception();
	
	/**
	 * receive a notification 
	 * 
	 * @param notificationObject 
	 */
	protected abstract void handleNotification(Notification notificationObject);


	public String getTeamName() {
		return team;
	}

	/**
	 * gets the AID of the team directory facilitator
	 */
	public TeamDF getTeamDF() {
		return new TeamDF(this, getTeamName());
	}

	
	public boolean isFriend(String aid) {
		return aid.contains(getTeamName());
	}
	

	protected void setTeamName(String team) {
		this.team = team;
	}
	
	/**
	 * Send a notification<br/><br/>
	 * e.g.<br/>
	 * 		when a soldier see an enemy, he sends a notification to the masterAi with the following parameters:<br/>
	 * 			- messageSubject = MessageSubject.ENEMY_SIGHTED;<br/>
	 * 			- contentObject = enemy_position;<br/>
	 * 			- receiver = masterAID<br/>
	 * 
	 * @param messageSubject type of notification (e.g. Notification.ENEMY_SIGHTED when sighting an enemy)
	 * @param contentObject an object with more information about the notification (e.g. the position of the enemy)
	 * @param receiver the AID of the receiver 
	 */
	public void sendNotification(String messageSubject, Serializable contentObject, AID receiver) {
		try {
			ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
			msg.addReceiver(receiver);
			msg.setContentObject(new Notification(messageSubject, contentObject));
			send(msg);
			
		} catch (IOException e) {
			logger.severe("Can't send the " + messageSubject + " notification to " + receiver);
		}
	}
	
	/**
	 * Send a request
	 * 
	 * @param requestSubject type of request (e.g. MessageSubject.GET_CITY_CENTER_POSITION)
	 * @param receiver the AID of the receiver 
	 */
	public Object sendRequest(String requestSubject, AID receiver) {
		try {
			ACLMessage msg = new ACLMessage(ACLMessage.QUERY_REF);
			msg.addReceiver(receiver);
			msg.setConversationId(requestSubject);
			send(msg);
			ACLMessage response = blockingReceive(MessageTemplate.MatchConversationId(requestSubject));
			return response.getContentObject();
		} catch (UnreadableException e1) {
			logger.severe("Can't read message ");
			return null;
		}
		
	}

	protected EnemySighting lookForEnemies(Position position, int sight, Perception perception) {
		int row = position.getRow();
		int col = position.getCol();
		EnemySighting enemies = new EnemySighting(position);
		for (int i = row - sight; i <= row + sight; i++) {
			for (int j = col - sight; j <= col + sight; j++) {
				Cell cell = perception.get(i,j);
				String enemyId = cell.getId();
				if (cell.isUnit()) {
					if (!isFriend(enemyId)) {
						enemies.addEnemy(new EnemySightingItem(new Position(i, j), enemyId, cell.getType()));
					}
				} 
			}
		}
		return enemies;
	}
}
