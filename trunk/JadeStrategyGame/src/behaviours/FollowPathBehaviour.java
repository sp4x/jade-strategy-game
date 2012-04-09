package behaviours;

import java.util.List;

import agents.Unit;

import jade.core.behaviours.Behaviour;
import logic.Direction;

public class FollowPathBehaviour extends Behaviour {

	List<Direction> list;
	Unit unit;
	
	public FollowPathBehaviour(Unit unit, List<Direction> list) {
		this.unit = unit;
		this.list = list;
	}

	@Override
	public void action() {
		unit.move(list.remove(0));
		unit.spendTime();
	}

	@Override
	public boolean done() {
		return list.isEmpty();
	}

}
