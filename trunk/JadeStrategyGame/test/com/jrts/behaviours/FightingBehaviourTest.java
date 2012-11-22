package com.jrts.behaviours;

import static org.junit.Assert.*;

import org.junit.Test;

import com.jrts.agents.Soldier;
import com.jrts.environment.Cell;
import com.jrts.environment.CellType;
import com.jrts.environment.Direction;
import com.jrts.environment.Floor;
import com.jrts.environment.Position;
import com.jrts.environment.WorldMap;
import com.jrts.messages.EnemySighting;
import com.jrts.messages.EnemySightingItem;

public class FightingBehaviourTest {
	
	boolean success = false;

	@Test
	//+----x
	public void hitBuilding() {
		final Floor f = new Floor(1, 6);
		final Position target = new Position(0, 5);
		f.set(target, new Cell(CellType.CITY_CENTER));
		Soldier s = new Soldier("s", new Position(0, 0)) {
			@Override
			public WorldMap requestMap() {
				return new WorldMap(f);
			}
			
			@Override
			public boolean move(Direction dir) {
				Position p = getPosition().step(dir);
				if (f.get(p).isWalkable()) {
					setPosition(p);
					return true;
				}
				return false;
			}
			
			@Override
			public void sendHit(Direction direction) {
				if (getPosition().step(direction).equals(target))
					f.set(target, new Cell(CellType.FREE));
			}
		};
		FightingBehaviour behaviour = new FightingBehaviour(s, target);
		while (!behaviour.done())
			behaviour.myAction();
		assertTrue(f.get(target).isFree());
	}
	
	@Test
	//+--+-*
	public void hitEnemy() {
		success = false;
		final Floor f = new Floor(1, 6);
		final Soldier enemy = new Soldier("enemy", new Position(0, 3)) {
			@Override
			public boolean move(Direction dir) {
				setPosition(getPosition().step(dir));
				return true;
			}
		};
		final EnemySighting enemySighting = new EnemySighting(null) {
			public EnemySightingItem getById(String id) {
				if (success)
					return null;
				return new EnemySightingItem(enemy.getPosition(), enemy.getId(), enemy.getType());
			}
		};
		final Soldier s = new Soldier("s", new Position(0, 0)) {
			@Override
			public boolean move(Direction dir) {
				setPosition(getPosition().step(dir));
				return true;
			}
			
			@Override
			public void sendHit(Direction direction) {
				success = true;
			}
			
			@Override
			public EnemySighting getLastEnemySighting() {
				return enemySighting;
			}
			
			@Override
			public WorldMap requestMap() {
				return new WorldMap(f);
			}
		};
		UnitBehaviour[] list = {
				new FightingBehaviour(s, enemy.getId()),
				new FollowPathBehaviour(enemy, new Position(0, 5), 1, f)
		};
		simulate(list);
		
		assertTrue(success);
		assertEquals(new Position(0, 5), enemy.getPosition());
		assertTrue(s.getPosition().isNextTo(enemy.getPosition()));
	}
	
	public void simulate(UnitBehaviour[] list) {
		boolean allDone = false;
		while (!allDone) {
			allDone = true;
			for (UnitBehaviour b : list) {
				if (!b.done()) {
					allDone = false;
					b.myAction();
				}
			}
		}
	}

}
