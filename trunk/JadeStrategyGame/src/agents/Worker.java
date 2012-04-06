package agents;

import common.GameConfig;

import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.OneShotBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;

public class Worker extends Unit {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 7202575787943066263L;

	public Worker(){}
		
	protected void setup(){
		Integer[] args = (Integer[]) getArguments();
		if (args != null && args.length == 2) {
			System.out.println("Set position");
			setX(args[0]);
			setY(args[1]);
		}
		setLife(GameConfig.WORKER_LIFE);
		setSpeed(GameConfig.WORKER_SPEED);
		setForceOfAttack(GameConfig.WORKER_FORCE_OF_ATTACK);
		
		System.out.println(getLocalName()+" STARTED");
		
		try {
			// create the agent description of itself
			DFAgentDescription dfd = new DFAgentDescription();
			dfd.setName(getAID());
			// register the description with the DF
			DFService.register(this, dfd);
			System.out.println(getLocalName()+" REGISTERED WITH THE DF");
		} catch (FIPAException e) {
			e.printStackTrace();
		}
		
		addBehaviour(new OneShotBehaviour() {
			
			@Override
			public void action() {
				System.out.println("I'm a worker in pos(" + getX() + "," + getY() + ")");
			}
		});
		
		goThere(9,7);
	}
}
