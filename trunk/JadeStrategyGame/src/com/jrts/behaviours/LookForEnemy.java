package com.jrts.behaviours;

import jade.core.Agent;
import jade.core.behaviours.TickerBehaviour;

import com.jrts.agents.Unit;
import com.jrts.environment.Cell;
import com.jrts.environment.CellType;
import com.jrts.environment.Position;
import com.jrts.messages.EnemySighting;
import com.jrts.messages.EnemySightingItem;
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
		
		if (unit.getPerception() == null) {
			System.out.println("NON HO PERCEZIONE:::::");
			return;
		}
		
		int row = unit.getPosition().getRow();
		int col = unit.getPerception().getCols();
		int sight = unit.getSight();
		EnemySighting list = new EnemySighting(new Position(row,col));
		for (int i = row - sight; i < row + sight; i++) {
			for (int j = col - sight; j < col + sight; j++) {
				Cell cell= unit.getPerception().get(i,j);
				CellType type = cell.getType();
				String enemyId = cell.getId();;
				if (type == CellType.SOLDIER || type == CellType.WORKER) {
					System.out.println("Sono " + unit.getId() + " e vicino a me c'e' " + enemyId);
					if (!unit.isFriend(enemyId)) {
						System.out.println("E noi ("+unit.getId()+" e " + enemyId + " NON siamo della stessa squadra!!");
						list.addEnemy(new EnemySightingItem(new Position(row, col), enemyId, type));
					} else {
						System.out.println("Ma noi ("+unit.getId()+" e " + enemyId + " siamo della stessa squadra..");
					}
				} 
			}
		}
		if (!list.isEmpty()) {
			unit.sendNotification(Notification.ENEMY_SIGHTED, list, unit.getMilitaryAID());
			unit.onEnemySighted(list);
		}
	}
}
