package agents;

import jade.core.AID;
import jade.core.behaviours.CyclicBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.SearchConstraints;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.util.leap.Iterator;
import jade.wrapper.AgentController;
import jade.wrapper.PlatformController;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

import messages.CreateWorker;

public class WorkerFactory extends UnitFactory {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7364332872627866474L;

	public static final String CREATE_WORKER = "CREATE WORKER";

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
							Random r = new Random();
							createUnit(1,1);//TODO
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

		//create unit
		try {
			Object[] pos = new Integer[2];
			pos[0] = x;
			pos[1] = y;
			AgentController worker = container.createNewAgent("worker"+workersList.size(), "agents.Worker", pos);
			worker.start();
			System.out.println("Worker started");

			// keep the worker's ID on a local list
			workersList.add( new AID("worker"+workersList.size(), AID.ISLOCALNAME) );
		}
		catch (Exception e) {
			System.err.println( "Exception while adding worker: " + e );
			e.printStackTrace();
		}

		//communicate unit position to world manager
		// Search for services of type "world-manager"
		System.out.println("Agent "+getLocalName()+" searching for services of type \"world-manager\"");
		try {
			// Build the description used as template for the search
			DFAgentDescription template = new DFAgentDescription();
			ServiceDescription templateSd = new ServiceDescription();
			templateSd.setType("world-manager");
			template.addServices(templateSd);

			SearchConstraints sc = new SearchConstraints();
			// We want to receive 1 results at most
			sc.setMaxResults(new Long(1));

			DFAgentDescription[] results = DFService.search(this, template, sc);
			if (results.length > 0) {
				System.out.println("Agent "+getLocalName()+" found the following world-manager services:");
				for (int i = 0; i < results.length; ++i) {
					DFAgentDescription dfd = results[i];
					AID provider = dfd.getName();
					// The same agent may provide several services; we are only interested
					// in the world-manager one
					Iterator it = dfd.getAllServices();
					while (it.hasNext()) {
						ServiceDescription sd = (ServiceDescription) it.next();
						if (sd.getType().equals("world-manager")) {
							System.out.println("- Service \""+sd.getName()+"\" provided by agent "+provider.getName());
							AID wm = dfd.getName();
							ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
							msg.setSender(getAID());
							msg.addReceiver(wm);
							try {
								msg.setContentObject(new CreateWorker(x, y));
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							send(msg);
						}
					}
				}
			}	
			else {
				System.out.println("Agent "+getLocalName()+" did not find any world-manager service");
			}
		}
		catch (FIPAException fe) {
			fe.printStackTrace();
		}
	}
}