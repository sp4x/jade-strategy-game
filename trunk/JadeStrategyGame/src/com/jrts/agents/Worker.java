package com.jrts.agents;

import jade.core.behaviours.TickerBehaviour;

import java.util.Random;

import com.jrts.common.AgentStatus;
import com.jrts.common.GameConfig;
import com.jrts.environment.Direction;
import com.jrts.environment.Position;
import com.jrts.gui.AttacksManager;

@SuppressWarnings("serial")
public class Worker extends Unit {
		
	public Worker() {
		super();
	}

	public Worker(Position position) {
		super(position);
	}

	protected void setup(){
		super.setup();
		Object[] args = getArguments();
		if (args != null) {
			System.out.println("Set position");
			setPosition((Position) args[0]);
			setTeam((String) args[1]);
		}
		setLife(GameConfig.WORKER_LIFE);
		setSpeed(GameConfig.WORKER_SPEED);
		setForceOfAttack(GameConfig.WORKER_DAMAGES);
		setSight(GameConfig.WORKER_SIGHT);
		
		System.out.println(getLocalName() + ":Started");

		setStatus(AgentStatus.FREE);
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
		
		addBehaviour(new TickerBehaviour(this, 1000) {

			@Override
			protected void onTick() {
				sendHit(Direction.random());
			}
		});
	}
	
	private void sendHit(Direction direction) {
		AttacksManager.addHit(getPosition().clone(), direction, GameConfig.WORKER_DAMAGES);
	}
}
;