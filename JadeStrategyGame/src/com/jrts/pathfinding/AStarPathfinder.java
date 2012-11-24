package com.jrts.pathfinding;

import java.util.List;

import net.sourceforge.jsl.AStarSearch;
import net.sourceforge.jsl.SearchException;

import com.jrts.environment.Direction;
import com.jrts.environment.Floor;
import com.jrts.environment.Position;

public class AStarPathfinder implements Pathfinder {

	@Override
	public List<Direction> calculatePath(Floor floor, Position startPosition,
			Position endPosition, int tolerance) {
		if (startPosition == null)
			return null;
		for (int i = 0; i <= tolerance; i++) {

			AStarSearch search = new AStarSearch();
			search.setSeed(new PathSearchNode(startPosition, endPosition,
					floor, i));
			try {
				PathSearchNode result = (PathSearchNode) search.search();
				if (result != null)
					return result.getPath();
			} catch (SearchException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return null;
	}

}
