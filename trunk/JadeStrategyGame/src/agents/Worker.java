package agents;

import java.util.Random;

import common.GameConfig;

import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.OneShotBehaviour;
import jade.core.behaviours.TickerBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import logic.Direction;

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
			setRow(args[0]);
			setCol(args[1]);
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
		
		addBehaviour(new TickerBehaviour(this, 1000) {

			@Override
			protected void onTick() {
				System.out.println("I'm a worker in pos(" + getRow() + "," + getCol() + ")");
				Random r = new Random();
				int newDir = r.nextInt(4);
				switch (newDir){
					case 0: move(Direction.RIGHT);break;
					case 1: move(Direction.LEFT);break;
					case 2: move(Direction.UP);break;
					case 3: move(Direction.DOWN);break;
				}
			}
		});
		
		goThere(9,7);
	}
}
