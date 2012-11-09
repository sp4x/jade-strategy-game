package com.jrts.behaviours;

import java.util.GregorianCalendar;
import java.util.LinkedList;
import java.util.Random;

import com.jrts.agents.Soldier;
import com.jrts.common.Utils;
import com.jrts.environment.Direction;
import com.jrts.environment.Position;
import com.jrts.environment.World;

public class ExploreBehaviour extends UnitBehaviour {

	Random r = new Random(GregorianCalendar.getInstance().getTimeInMillis());
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	Soldier soldier;
	Position cityCenter;
	
	/**
	 * 
	 * @param soldier
	 */
	public ExploreBehaviour(Soldier soldier) {
		super(false);
		
		this.soldier = soldier;
		this.cityCenter = World.getInstance().getCityCenter(soldier.getTeamName());
	}

	@Override
	public void action() {
		
		this.soldier.logger.warning("BASE ACTION");
		
		LinkedList<Direction> dir = new LinkedList<Direction>();
		dir.add(Direction.LEFT_UP);
		dir.add(Direction.LEFT_DOWN);
		dir.add(Direction.RIGHT_UP);
		dir.add(Direction.RIGHT_DOWN);
		
		dir.remove(Utils.getMapAnglePosition(this.soldier.getPosition()));
		this.soldier.logger.warning("SOLDATO IN " + Utils.getMapAnglePosition(this.soldier.getPosition()));
		
		boolean going = false;
		for (int i = 0; i < 10 && !going ; i++)
		{
			Direction dirToGo = dir.get(r.nextInt(3));
			
			this.soldier.logger.warning("SOLDATO SI MUOVE IN " + dirToGo);
			
			Position posToGo = Utils.getRandomUnknownCellPosition(this.soldier.requestMap(), dirToGo);
			
			if(posToGo != null) 
			{
				this.soldier.logger.warning("SOLDATO VA " + posToGo);

				this.soldier.goThere(posToGo);
				going = true;
			} else 
				this.soldier.logger.warning("POSTOGO NULLL");
		}
		this.soldier.logger.warning("ESCO DAL FOR");
	}

	@Override
	public boolean done() {
		return false;
	}
}
