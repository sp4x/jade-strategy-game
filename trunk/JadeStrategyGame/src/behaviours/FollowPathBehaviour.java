package behaviours;

import jade.core.behaviours.Behaviour;

import java.util.ArrayList;

import com.common.GameConfig;
import com.common.Utils;
import com.jrts.agents.Unit;
import com.jrts.environment.Direction;
import com.jrts.environment.Position;

@SuppressWarnings("serial")
public class FollowPathBehaviour extends Behaviour {

	ArrayList<Direction> list;
	Unit unit;
	private int goalRow, goalCol;
	int remainingAttempts;
	
	public FollowPathBehaviour(Unit unit, int goalRow, int goalCol, int remainingAttempts) {
		this.unit = unit;
		this.goalRow = goalRow;
		this.goalCol = goalCol;
		this.remainingAttempts = remainingAttempts;
		this.list = new ArrayList<Direction>();
		
		if(remainingAttempts > 0){
			Position unitPos = unit.getPosition();
			this.list  = Utils.calculatePath(unit.getPerception(), unitPos.getRow(), unitPos.getCol(), goalRow, goalCol);
		}
	}

	@Override
	public void action() {
		unit.spendTime();
		
		//se non riesco a spostarmi ricalcolo il path
		if (!list.isEmpty() && !unit.move(list.remove(0))) {
			System.out.println(unit.getLocalName() + ":Need path recalculation");
			list.clear();
			unit.addBehaviour(new FollowPathBehaviour(unit, goalRow, goalCol, remainingAttempts-1));
		}
		else
			remainingAttempts = GameConfig.UNIT_MOVING_ATTEMPTS;
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
