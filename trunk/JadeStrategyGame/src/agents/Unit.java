package agents;

import jade.core.AID;
import jade.core.Agent;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.lang.acl.UnreadableException;

import java.io.IOException;
import java.util.List;

import logic.Direction;
import logic.Floor;
import logic.PositionGoal;
import messages.GetPath;
import messages.MoveThere;
import behaviours.FollowPathBehaviour;

public abstract class Unit extends Agent{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -6755184651108394854L;

	int row;
	int col;
	int life;
	int speed;
	int forceOfAttack;
	
	int oldRow;
	int oldCol;
	
	int team;
	
	PositionGoal positionGoal;
	
	public void goThere(int x, int y){
		ACLMessage msg = new ACLMessage(ACLMessage.REQUEST);
		msg.setLanguage("info-language");
		msg.setSender(getAID());
		msg.addReceiver(new AID("worldManager", AID.ISLOCALNAME));
		try {
			msg.setContentObject(new GetPath(getRow(), getCol(), x, y));
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		send(msg);
		System.out.println(getLocalName() + ":Sent get path to WM");
		//wait the answer in blocking mode
		ACLMessage reply = blockingReceive(MessageTemplate.MatchLanguage("info-language"));
		System.out.println(getLocalName() + ":Received reply to get path request");
		if(reply.getPerformative() == ACLMessage.INFORM){
			System.out.println(getLocalName() + ":WM send me a path");
			try {
				if(reply.getContentObject() instanceof List<?>){
					List<Direction> path = (List<Direction>) reply.getContentObject();
					addBehaviour(new FollowPathBehaviour(this, path, x, y));
				}
			} catch (UnreadableException e) {
				e.printStackTrace();
			}
		}
	}

	public void move(Direction dir){
		ACLMessage msg = new ACLMessage(ACLMessage.PROPOSE);
		msg.setSender(getAID());
		msg.setLanguage("movement-language");
		msg.addReceiver(new AID("worldManager", AID.ISLOCALNAME));
		try {
			msg.setContentObject(new MoveThere(getRow(), getCol(), dir));
		} catch (IOException e) {
			System.out.println(getLocalName() + ":Error in MoveThere creation");
			e.printStackTrace();
		}
//		System.out.println("U:Propose movement");
		send(msg);
		
		//wait the answer in blocking mode
		ACLMessage reply = blockingReceive(MessageTemplate.MatchLanguage("movement-language"));
		if(reply.getPerformative() == ACLMessage.ACCEPT_PROPOSAL){
			System.out.println(getLocalName() + ":WM accepts my movement");
			setRow(row + dir.rowVar());
			setCol(col + dir.colVar());
		}
		else if(reply.getPerformative() == ACLMessage.REJECT_PROPOSAL){
			System.out.println(getLocalName() + ":WM rejects my movement");
		}
		else if(reply.getPerformative() == ACLMessage.REFUSE){
			System.out.println(getLocalName() + ":Received detected error message format");
		}
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
	
	public boolean isPositionChanged(){
		if(getOldRow() == getRow() && getOldCol() == getCol()){
			setOldRow(getRow());
			setOldCol(getCol());
			return false;
		}
		setOldRow(getRow());
		setOldCol(getCol());
		return true;
	}

	public int getRow() {
		return row;
	}

	protected void setRow(int row) {
		this.row = row;
	}

	public int getCol() {
		return col;
	}

	protected void setCol(int col) {
		this.col = col;
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

	public int getOldRow() {
		return oldRow;
	}

	public void setOldRow(int oldRow) {
		this.oldRow = oldRow;
	}

	public int getOldCol() {
		return oldCol;
	}

	public void setOldCol(int oldCol) {
		this.oldCol = oldCol;
	}

	public void spendTime() {
		try {
			Thread.sleep(5000/getSpeed());
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
