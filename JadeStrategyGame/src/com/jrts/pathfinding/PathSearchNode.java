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
	int tolerance;
	LinkedList<Direction> path;

	public PathSearchNode(Position start, Position target, Floor floor, int tolerance) {
		super();
		this.current = start;
		this.target = target;
		this.path = new LinkedList<Direction>();
		this.floor = floor;
		this.tolerance = tolerance;
	}

	private PathSearchNode(Position current, Direction action, List<Direction> path, Position target, Floor floor, int tolerance) {
		this(current, target, floor, tolerance);
		this.path = new LinkedList<Direction>(path);
		this.path.add(action);
	}
	
	@Override
	public Object getDomainObject() {
		return current;
	}

	@Override
	public boolean isGoal() {
		if (current == null || target == null)
			return false;
//		if (floor.isWalkable(target))
//			return current.equals(target);
		return current.distance(target) < tolerance + 1;
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
		if (current == null)
			return successors; //empty
		for (Direction action : Direction.ALL) {
			Position domainObj = current.step(action);
			if (floor.isWalkable(domainObj) )
				successors.add(new PathSearchNode(domainObj, action, path,
						target, floor, tolerance));
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
