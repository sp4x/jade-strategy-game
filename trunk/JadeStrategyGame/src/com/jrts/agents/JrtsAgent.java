package com.jrts.agents;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.TickerBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.lang.acl.UnreadableException;

import java.io.IOException;
import java.io.Serializable;
import java.util.logging.Logger;

import com.jrts.common.GameConfig;
import com.jrts.messages.Notification;

@SuppressWarnings("serial")
public abstract class JrtsAgent extends Agent {

	public final Logger logger = Logger.getLogger(Agent.class.getName());
	
	private String team;

	public JrtsAgent() {
		//this.logger.setLevel(Level.WARNING);
	}
	
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
				ACLMessage msg = receive(MessageTemplate.MatchConversationId(Notification.class.getSimpleName()));
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
						ACLMessage response = msg.createReply();
						handleRequest(response);
						send(response);
					} catch (Exception e) {
						logger.severe("Can't read message");
					}
				} else {
					// if no message is arrived, block the behaviour
					block();
				}
			}
		});
	}

	protected abstract void handleRequest(ACLMessage msg) throws IOException;

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
	protected AID getTeamDF() {
		return new AID(getTeamName() + "-df", AID.ISLOCALNAME);
	}

	/**
	 * register this agent description to the team directory facilitator
	 * 
	 * @param desc
	 *            the agent description
	 * @param deletePrevious
	 *            deregister any previous description associated to the agent
	 */
	protected void register(DFAgentDescription desc, boolean deletePrevious) {
		try {
			if (deletePrevious)
				DFService.deregister(this, getTeamDF());
			DFService.register(this, getTeamDF(), desc);
		} catch (FIPAException e) {
			e.printStackTrace();
		}
	}

	public DFAgentDescription[] search(DFAgentDescription desc) {
		try {
			return DFService.search(this, getTeamDF(), desc);
		} catch (FIPAException e) {
			e.printStackTrace();
		}
		return new DFAgentDescription[0];
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
			msg.setConversationId(Notification.class.getSimpleName());
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
	public ACLMessage sendRequest(String requestSubject, AID receiver) {
		try {
			ACLMessage msg = new ACLMessage(ACLMessage.QUERY_REF);
			msg.addReceiver(receiver);
			msg.setConversationId(requestSubject);
			send(msg);
			ACLMessage response = blockingReceive(MessageTemplate.MatchConversationId(requestSubject));
			return response;
			
		} catch (Exception e) {
			logger.severe("Can't send the " + requestSubject + " request to " + receiver);
			return null;
		}
	}

}
