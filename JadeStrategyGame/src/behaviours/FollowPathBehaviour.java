package behaviours;

import jade.core.behaviours.Behaviour;

import java.util.List;


import com.jrts.agents.Unit;
import com.jrts.environment.Direction;

public class FollowPathBehaviour extends Behaviour {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	List<Direction> list;
	Unit unit;
	private int goalRow, goalCol;
	
	public FollowPathBehaviour(Unit unit, List<Direction> list, int goalRow, int goalCol) {
		this.unit = unit;
		this.list = list;
		this.goalRow = goalRow;
		this.goalCol = goalCol;
	}

	@Override
	public void action() {
		unit.spendTime();
		if(list.isEmpty())
			return;
		if (!unit.move(list.remove(0))) {
			System.out.println(unit.getLocalName() + ":Need path recalculation");
			list.clear();
			unit.goThere(goalRow, goalCol);
		}
	}

	@Override
	public boolean done() {
		return list.isEmpty();
	}

	public int getGoalRow() {
		return goalRow;
	}

	public void setGoalRow(int goalRow) {
		this.goalRow = goalRow;
	}

	public int getGoalCol() {
		return goalCol;
	}

	public void setGoalCol(int goalCol) {
		this.goalCol = goalCol;
	}
}
