package agents;

import com.jrts.environment.Position;

import jade.core.behaviours.TickerBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;

import common.GameConfig;

public class Worker extends Unit {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 7202575787943066263L;

	public Worker(){}
		
	public Worker(Position position) {
		super(position);
	}

	protected void setup(){
		Object[] args = getArguments();
		if (args != null && args[0] instanceof Position) {
			System.out.println("Set position");
			setPosition((Position) args[0]);
		}
		setLife(GameConfig.WORKER_LIFE);
		setSpeed(GameConfig.WORKER_SPEED);
		setForceOfAttack(GameConfig.WORKER_FORCE_OF_ATTACK);
		
		System.out.println(getLocalName()+" STARTED");
		
		try {
			// create the agent description of itself
			DFAgentDescription dfd = new DFAgentDescription();
			dfd.setName(getAID());
			ServiceDescription sd = new ServiceDescription();
	  		sd.setName(getAID().getName());
	  		sd.setType("worker");
	  		dfd.addServices(sd);
			// register the description with the DF
			DFService.register(this, dfd);
			System.out.println(getLocalName()+" REGISTERED WITH THE DF");
		} catch (FIPAException e) {
			e.printStackTrace();
		}
		
		addBehaviour(new TickerBehaviour(this, 1000) {

			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			protected void onTick() {
//				System.out.println("Worker Pos(" + getRow() + "," + getCol() + ") Changed: " + isPositionChanged());
			}
		});
		
		goThere(49,49);
	}
}
