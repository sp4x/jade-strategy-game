package agents;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;

public class MasterAi extends Agent {
	
	@Override
	//TODO stub
	protected void setup() {
		super.setup();
		System.out.println("setting up master");
		this.addBehaviour(new OneShotBehaviour() {
			
			@Override
			public void action() {
				ACLMessage msg = new ACLMessage(ACLMessage.REQUEST);
				msg.setContent(WorkerFactory.CREATE_WORKER);
				msg.addReceiver(new AID("worker-factory", AID.ISLOCALNAME));
				send(msg);
			}
		});
	}

}
