package behaviours;

import jade.core.behaviours.Behaviour;

import java.util.List;

import logic.Direction;
import agents.Unit;

public class FollowPathBehaviour extends Behaviour {

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
		if(list.isEmpty())
			return;
		if (!unit.move(list.remove(0))) {
			System.out.println("Need path recalculation");
			list.clear();
			unit.goThere(goalRow, goalCol);
		}
		unit.spendTime();
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
