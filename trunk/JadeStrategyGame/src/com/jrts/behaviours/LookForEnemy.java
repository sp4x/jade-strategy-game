package com.jrts.behaviours;

import jade.core.Agent;
import jade.core.behaviours.TickerBehaviour;

import com.jrts.agents.Unit;
import com.jrts.environment.Cell;
import com.jrts.environment.CellType;
import com.jrts.environment.Position;
import com.jrts.messages.EnemySighting;
import com.jrts.messages.Notification;

public class LookForEnemy extends TickerBehaviour {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public LookForEnemy(Agent a, long period) {
		super(a, period);
	}

	@Override
	protected void onTick() {
		Unit unit = (Unit) myAgent;
		int row = unit.getPosition().getRow();
		int col = unit.getPerception().getCols();
		int sight = unit.getSight();
		EnemySighting list = new EnemySighting(new Position(row,col));
		for (int i = row - sight; i < row + sight; i++) {
			for (int j = col - sight; j < col + sight; j++) {
				Cell cell = unit.getPerception().get(i,j);
				if ((cell.getType() == CellType.SOLDIER || cell.getType() == CellType.WORKER) && !unit.isFriend(cell.getId())) {
					list.addEnemy(cell.getUnit());
				} 
			}
		}
		if (!list.isEmpty()) {
			unit.sendNotification(Notification.ENEMY_SIGHTED, list, unit.getMilitaryAID());
			unit.onEnemySighted(list);
		}
	}
}
