package agents;

import java.util.List;

import org.jgrapht.GraphPath;
import org.jgrapht.graph.DefaultWeightedEdge;

import common.Utils;

import jade.core.AID;
import jade.core.Agent;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.lang.acl.UnreadableException;
import logic.Floor;
import logic.PositionGoal;

public abstract class Unit extends Agent{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -6755184651108394854L;

	int x;
	int y;
	int life;
	int speed;
	int forceOfAttack;
	
	int team;
	
	PositionGoal positionGoal;
	
	public void goThere(int x, int y){
		System.out.println("I go to " + x + "," + y);
		positionGoal = new PositionGoal(x, y);
		Floor floor = getWorldInfo();
		System.out.println("MY FLOOR IS");
		List<DefaultWeightedEdge> path = Utils.calculatePath(floor, this.x, this.y, x, y);
		System.out.println("MY PATH: " + path);
		
	}

	private Floor getWorldInfo() {
		ACLMessage msg = new ACLMessage(ACLMessage.REQUEST);
		msg.setSender(getAID());
		msg.addReceiver(new AID("worldManager", AID.ISLOCALNAME));
		msg.setContent(WorldManager.COMPLETE_WORLD_VIEW);
		send(msg);
		
		//wait the answer in blocking mode
		ACLMessage reply = blockingReceive(MessageTemplate.MatchPerformative(ACLMessage.INFORM));
		Floor floor = null;
		try {
			floor = (Floor) reply.getContentObject();
		} catch (UnreadableException e) {
			System.out.println("Error: answer doesn't contain a floor");
			return null;
		}
		return floor;
		
	}

	public int getX() {
		return x;
	}

	protected void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	protected void setY(int y) {
		this.y = y;
	}

	public int getLife() {
		return life;
	}

	protected void setLife(int life) {
		if(life>0)
			this.life = life;
		else
			this.life = 0;
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

	public int getTeam() {
		return team;
	}

	private void setTeam(int team) {
		this.team = team;
	}
}
