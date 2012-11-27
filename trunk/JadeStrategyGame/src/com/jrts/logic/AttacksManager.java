package com.jrts.logic;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.jrts.common.GameConfig;
import com.jrts.environment.Cell;
import com.jrts.environment.Direction;
import com.jrts.environment.Hit;
import com.jrts.environment.Position;
import com.jrts.environment.World;

public class AttacksManager {

	static Logger logger = Logger.getLogger(AttacksManager.class.getName());
	static Level logLevel = Level.FINE;

	private static ArrayList<Hit> hits;

	static {
		hits = new ArrayList<Hit>();
	}

	public static void start() {
		Thread t = new Thread(new Runnable() {
			@Override
			public void run() {
				while (true) {
					try {
						Thread.sleep(GameConfig.ATTACKS_REFRESH);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					update();
				}
			}
		});
		t.start();
	}

	public synchronized static void addHit(Position pos, Direction dir, int damage, int hitRange) {
		Hit hit = new Hit(pos, dir, damage, hitRange);
		// Eseguo uno spostamento per evitare che il colpo danneggi l'unita'
		// sorgente stessa
		// hit.step(); EDIT: viene fatto dopo
		hits.add(hit);
	}

	public synchronized static void update() {
		LinkedList<Hit> toRemove = new LinkedList<Hit>();
		for (Hit hit : hits) {
			hit.step();
			// remove hit which exceed floor's bound
			if (!hit.respectLimits(World.getInstance().getRows(), World.getInstance().getCols())) {
				toRemove.add(hit);
			} else {
				// check for collisions
				Position hitpos = hit.getPos();
				int damage = hit.getDamage();
				Cell cell = World.getInstance().getCell(hitpos);
				if (!cell.isFree()) {
					logger.log(logLevel, "Detected collision");
					toRemove.add(hit);
					if (cell.isCityCenter() || cell.isUnit())
						World.getInstance().takeEnergy(hitpos, damage);
				} else if (hit.getRange() == 0) {
					// exceeded range limit
					toRemove.add(hit);
				}
			}
		}
		for (Hit hit : toRemove) {
			hits.remove(hit);
		}
		toRemove.clear();
	}

	public synchronized static boolean isThereAnHit(int row, int col) {
		for (int i = 0; i < hits.size(); i++)
			if (hits.get(i).getPos().equals(new Position(row, col)))
				return true;
		return false;
	}
}