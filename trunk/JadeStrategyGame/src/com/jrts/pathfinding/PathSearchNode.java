package com.jrts.pathfinding;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import net.sourceforge.jsl.AbstractSearchNode;

import com.jrts.environment.Direction;
import com.jrts.environment.Floor;
import com.jrts.environment.Position;


public class PathSearchNode extends AbstractSearchNode {
	
	Position current;
	Position target;
	Floor floor;
	LinkedList<Direction> path;

	public PathSearchNode(Position start, Position target, Floor floor) {
		super();
		this.current = start;
		this.target = target;
		this.path = new LinkedList<Direction>();
		this.floor = floor;
	}

	private PathSearchNode(Position current, Direction action, List<Direction> path, Position target, Floor floor) {
		this(current, target, floor);
		this.path = new LinkedList<Direction>(path);
		this.path.add(action);
	}
	
	@Override
	public Object getDomainObject() {
		return current;
	}

	@Override
	public boolean isGoal() {
		if (floor.isWalkable(target))
			return current.equals(target);
		return current.isNextTo(target);
	}

	@Override
	public int getBreadth() {
		return expand().size();
	}

	@Override
	public int getDepth() {
		return path.size();
	}

	@Override
	public Collection<PathSearchNode> expand() {
		LinkedList<PathSearchNode> successors = new LinkedList<PathSearchNode>();
		for (Direction action : Direction.ALL) {
			Position domainObj = current.step(action);
			if (floor.isWalkable(domainObj) )
				successors.add(new PathSearchNode(domainObj, action, path,
						target, floor));
		}
		return successors;
	}

	@Override
	public List<Direction> getPath() {
		return path;
	}
	
	@Override
	public double getEstimatedRestCost() {
		return current.distance(target);
	}

}
