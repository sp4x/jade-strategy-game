package logic;

import jade.core.AID;
import jade.core.behaviours.CyclicBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.wrapper.AgentController;
import jade.wrapper.PlatformController;

import java.util.ArrayList;

public class WorkerFactory extends UnitFactory {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7364332872627866474L;

	private static final String CREATE_WORKER = "CREATE WORKER";

	int workerCount = 0;

	ArrayList<AID> workersList = new ArrayList<AID>();

	protected void setup() {
		try {
			System.out.println( getLocalName() + " setting up");

			// create the agent description of itself
			DFAgentDescription dfd = new DFAgentDescription();
			dfd.setName( getAID() );
			DFService.register( this, dfd );

			// add a Behaviour to handle creations
			addBehaviour( new CyclicBehaviour( this ) {
				public void action() {
					MessageTemplate mt = MessageTemplate.MatchPerformative(ACLMessage.REQUEST);
					ACLMessage msg = myAgent.receive(mt);

					if (msg != null) {
						if (CREATE_WORKER.equalsIgnoreCase( msg.getContent() )) {
							createUnit(3, 4);//TODO
							System.out.println("Created worker");
						}
						else {
							ACLMessage reply = msg.createReply();
							reply.setPerformative(ACLMessage.REFUSE);
							reply.setContent("( UnexpectedContent ("+msg.getContent()+"))");
							send(reply);
						}
					}
					else {
						// if no message is arrived, block the behaviour
						block();
					}
				}
			} );
		}
		catch (Exception e) {
			System.out.println( "Saw exception in WorkerFactory: " + e );
			e.printStackTrace();
		}
	}

	@Override
	void createUnit(int x, int y) {
		PlatformController container = getContainerController(); // get a container controller for creating new agents

		try {
			Object[] pos = new Integer[2];
			pos[0] = x;
			pos[1] = y;
			AgentController worker = container.createNewAgent("worker"+workerCount, "logic.Worker", pos);
			worker.start();
			System.out.println("Worker started");

			// keep the worker's ID on a local list
			workersList.add( new AID("worker"+workerCount, AID.ISLOCALNAME) );
			workerCount++;
		}
		catch (Exception e) {
			System.err.println( "Exception while adding worker: " + e );
			e.printStackTrace();
		}
	}
}