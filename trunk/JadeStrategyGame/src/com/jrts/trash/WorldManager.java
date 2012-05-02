package com.jrts.trash;

import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.OneShotBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.UnreadableException;
import jade.wrapper.AgentController;
import jade.wrapper.PlatformController;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;

import com.jrts.common.Utils;
import com.jrts.environment.Cell;
import com.jrts.environment.Direction;
import com.jrts.environment.Floor;
import com.jrts.messages.CreateWorker;
import com.jrts.messages.GetPath;
import com.jrts.messages.MoveThere;

public class WorldManager extends Agent{

	/** 
	 * 
	 */
	private static final long serialVersionUID = -6382920635049200303L;

	public static final String COMPLETE_WORLD_VIEW = "COMPLETE WORLD VIEW";

	Floor floor;

	public WorldManager() {}

	protected void setup(){
		String[] args = (String[]) getArguments();
		if (args != null && args.length == 2) {
			System.out.println("Set size");
			floor = new Floor(Integer.parseInt(args[0]), Integer.parseInt(args[1]));
			floor.generateObject(1000, Cell.WOOD);

			//test behaviour
			addBehaviour(new OneShotBehaviour() {

				@Override
				public void action() {
					PlatformController container = getContainerController(); // get a container controller for creating new agents
					try {
						AgentController factory = container.createNewAgent("worker-factory", "agents.WorkerFactory", null);
						factory.start();
						AgentController master = container.createNewAgent("master", "agents.MasterAi", null);
						master.start();
					} catch (Exception e) {
						e.printStackTrace();
						System.out.println("error creating agents");
						System.exit(1);
					}
				}
			});

			addBehaviour(new CyclicBehaviour(this) {

				@Override
				public void action() {
					ACLMessage  msg = myAgent.receive();
					if(msg != null){
						if(msg.getPerformative()== ACLMessage.INFORM){
							System.out.println("WM:Received MSG");
							try {
								if(msg.getContentObject() instanceof CreateWorker){
									CreateWorker cw = (CreateWorker) msg.getContentObject();
									System.out.println("Creazione in " + cw.getX() + " " + cw.getY());
									floor.set(cw.getX(),cw.getY(), Cell.UNIT);
								}
							} catch (UnreadableException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
						if(msg.getPerformative()== ACLMessage.REQUEST){
							try {
								if(msg.getContentObject() instanceof GetPath){
									GetPath getPath = (GetPath) msg.getContentObject();
									System.out.print("Richiesta path da (" + getPath.getSrcRow() + "," + getPath.getSrcCol() + ") ");
									System.out.println("a (" + getPath.getDstRow() + "," + getPath.getDstCol() + ")");
									
									List<Direction> list = Utils.calculatePath(floor, getPath.getSrcRow(), getPath.getSrcCol(), getPath.getDstRow(), getPath.getDstCol());
									System.out.println(list);
									ACLMessage reply = msg.createReply();
									reply.setLanguage("info-language");
									reply.setPerformative(ACLMessage.INFORM);
									try {
										reply.setContentObject((Serializable) list);
									} catch (IOException e) {
										e.printStackTrace();
									}
									send(reply);
								}
							} catch (UnreadableException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
						}
						if(msg.getPerformative()== ACLMessage.PROPOSE){
							MoveThere cm = null;
							try {
								cm = (MoveThere) msg.getContentObject();
							} catch (UnreadableException e) {
								System.out.println("Error: movement propose not valid");
								return;
							}
							if(floor.get(cm.getSourceRow(), cm.getSourceCol()) != Cell.UNKNOWN){
								//TODO check the movement
								System.out.println("WM:Check movement");
								int dstRow = cm.getSourceRow() + cm.getDir().rowVar();
								int dstCol = cm.getSourceCol() + cm.getDir().colVar();
								if(floor.get(dstRow, dstCol) == Cell.FREE){
									//Correct Movement
									System.out.println("WM:Accept Movement");
									ACLMessage reply = msg.createReply();
									reply.setPerformative(ACLMessage.ACCEPT_PROPOSAL);
									reply.setLanguage("movement-language");
									send(reply);
									//Update world info
									floor.set(cm.getSourceRow(), cm.getSourceCol(),Cell.FREE);
									floor.set(dstRow, dstCol,Cell.UNIT);
								}
								else{
									//Destination cell isn't free
									System.out.println("WM:Reject Movement");
									ACLMessage reply = msg.createReply();
									reply.setLanguage("movement-language");
									reply.setPerformative(ACLMessage.REJECT_PROPOSAL);
									send(reply);
								}
							}
							else{
								//bad indexes
								ACLMessage reply = msg.createReply();
								reply.setPerformative(ACLMessage.REFUSE);
								reply.setContent("Error bad indexes");
								send(reply);
							}
						}
					}
				}
			});

			//new MainFrame(this);
		}
		else{
			try {
				throw new Exception("Error in world creation");
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				System.exit(1);
			}
		}

		//registration
		try {
			// create the agent description of itself
			DFAgentDescription dfd = new DFAgentDescription();
			dfd.setName(getAID());
			ServiceDescription sd = new ServiceDescription();
			sd.setName("worldManager");
			sd.setType("world-manager");
			dfd.addServices(sd);
			// register the description with the DF
			DFService.register(this, dfd);
			System.out.println(getLocalName()+" REGISTERED WITH THE DF");
		} catch (FIPAException e) {
			e.printStackTrace();
		}
	}

	public Floor getFloor() {
		return floor;
	}

	public void setFloor(Floor floor) {
		this.floor = floor;
	}
}
