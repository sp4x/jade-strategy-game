package com.jrts.scorer;

import java.util.Collection;
import java.util.LinkedList;

import com.jrts.agents.MasterAI.Nature;
import com.jrts.common.GameConfig;
import com.jrts.environment.MasterPerception;
import com.jrts.environment.Position;
import com.jrts.messages.EnemySighting;

public class DefenceScorer extends GoalScorer {

	public DefenceScorer(final Nature nature, final MasterPerception perception) {
		super(nature, perception);

		addRule(new Rule() {

			@Override
			public double value() {
				int numEnemySighted = 0;
				int numThreats = 0;
						
				int bound = GameConfig.DEFENCE_NATURE_AVERAGE_BOUND;
				if(nature == Nature.DEFENSIVE)
					bound = GameConfig.DEFENCE_NATURE_DEFENSIVE_BOUND;
				else if(nature == Nature.AGGRESSIVE)
					bound = GameConfig.DEFENCE_NATURE_AGGRESSIVE_BOUND;
				
				for (EnemySighting es : perception.getEnemySightings())
					if(perception.getCityCenter().distance(es.getSightingPosition()) < bound)
							numEnemySighted += es.getSoldierNumber();
				
				for (Position pos : perception.getThreats())
					if(perception.getCityCenter().distance(pos) < bound)
							numThreats += 2;
				
				return (numEnemySighted + numThreats) * 10;
			}
		});

		addRule(new Rule() {

			@Override
			public double value() {

				if(nature == Nature.DEFENSIVE)
					return MAX_SCORE;
				if(nature == Nature.AVERAGE)
					return MAX_SCORE / 3 * 2;

				return MAX_SCORE /  2;
			}
		});
		
		//max if city center is under attack
		addRule(new Rule() {
			
			@Override
			public double value() {
				if (perception.isAlertCityCenterUnderAttack())
					return MAX_SCORE;
				return MIN_SCORE;
			}
		});
		
		addRule(new Rule() {
			
			@Override
			public double value() {
				if (perception.getEnemySightings().isEmpty())
					return MIN_SCORE;
				Position cityCenter = perception.getCityCenter();
				Collection<Position> enemyPositions = new LinkedList<Position>();
				for (EnemySighting e : perception.getEnemySightings()) {
					enemyPositions.add(e.getSightingPosition());
				}
				Position closest = cityCenter.nearest(enemyPositions);
				return MAX_SCORE - closest.distance(cityCenter)*4;
			}
		});
		
		
	}

}
