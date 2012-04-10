package agents;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;

import java.io.IOException;
import java.util.Random;

import messages.CreateWorker;

public class MasterAi extends Agent {
	
	@Override
	//TODO stub
	protected void setup() {
		super.setup();
		System.out.println("setting up master");
		this.addBehaviour(new OneShotBehaviour() {
			
			@Override
			public void action() {
				for (int i = 0; i < 20; i++)
					creaWorker();
			}
		});
	}

	protected void creaWorker() {
		ACLMessage msg = new ACLMessage(ACLMessage.REQUEST);
		Random r = new Random();
		try {
			msg.setContentObject(new CreateWorker(r.nextInt(50), r.nextInt(50)));
		} catch (IOException e) {
			e.printStackTrace();
		}
		msg.addReceiver(new AID("worker-factory", AID.ISLOCALNAME));
		send(msg);
	}

}
