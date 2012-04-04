package agents;

import gui.MainFrame;
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
import logic.Cell;
import logic.Floor;
import messages.CreateWorker;

public class WorldManager extends Agent{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -6382920635049200303L;
	
	Floor floor;

	private MainFrame mainFrame;
	
	public WorldManager() {}

	protected void setup(){
		String[] args = (String[]) getArguments();
		if (args != null && args.length == 2) {
			System.out.println("Set size");
			floor = new Floor(Integer.parseInt(args[0]), Integer.parseInt(args[1]));
			
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
							CreateWorker cw = null;
							try {
								cw = (CreateWorker) msg.getContentObject();
							} catch (UnreadableException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							System.out.println("Creazione in " + cw.getX() + " " + cw.getY());
							floor.set(cw.getX(),cw.getY(), Cell.UNIT);
							mainFrame.update();
						}
					}
				}
			});
			
			mainFrame = new MainFrame(this);
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
