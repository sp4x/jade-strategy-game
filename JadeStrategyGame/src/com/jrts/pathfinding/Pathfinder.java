package com.jrts.pathfinding;

import java.util.List;

import com.jrts.environment.Direction;
import com.jrts.environment.Floor;
import com.jrts.environment.Position;

public interface Pathfinder {

	public List<Direction> calculatePath(Floor floor, Position startPosition,
			Position endPosition, int tolerance);

}
