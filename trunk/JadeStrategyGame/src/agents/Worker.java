package agents;

import jade.core.behaviours.TickerBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;

import com.jrts.environment.Position;
import common.GameConfig;

public class Worker extends Unit {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public Worker(){}
		
	public Worker(Position position) {
		super(position);
	}

	protected void setup(){
		Object[] args = getArguments();
		if (args != null) {
			System.out.println("Set position");
			setPosition((Position) args[0]);
			setTeam((String) args[1]);
		}
		setLife(GameConfig.WORKER_LIFE);
		setSpeed(GameConfig.WORKER_SPEED);
		setForceOfAttack(GameConfig.WORKER_FORCE_OF_ATTACK);
		setSight(GameConfig.WORKER_SIGHT);
		
		System.out.println(getLocalName()+":Started");
		
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
			System.out.println(getLocalName()+":Registered with the df");
		} catch (FIPAException e) {
			e.printStackTrace();
		}
		
		addBehaviour(new TickerBehaviour(this, 5000) {

			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			protected void onTick() {
				System.out.println(getLocalName()+ ":Pos" + getPosition());
			}
		});
		
//		goThere(49,49);
	}
}
