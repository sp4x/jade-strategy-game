package com.jrts.agents;

import java.util.Random;

import jade.core.behaviours.TickerBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;

import com.common.GameConfig;
import com.jrts.environment.Position;

@SuppressWarnings("serial")
public class Worker extends Unit {

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

			@Override
			protected void onTick() {
				System.out.println(getLocalName()+ ":Pos" + getPosition());
			}
		});
		
		Random r = new Random();
		int x, y;
		if(r.nextInt(2) == 0)
			x = GameConfig.WORLD_ROWS-1;
		else
			x = 0;
		if(r.nextInt(2) == 0)
			y = GameConfig.WORLD_ROWS-1;
		else
			y = 0;
		goThere(x,y);
	}
}
