package com.jrts.environment;

import java.io.Serializable;
import java.util.Collections;
import java.util.LinkedList;

import com.jrts.common.Utils;

/**
 *  This class implement a Floor built of a Cell matrix where the agent acts.
 * 
 * @see Cell	 
 *
 */
public class Floor implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public int rows;

	public int cols;

	private Cell [][] floor;
	
	public Floor(int rows, int cols, CellType defaultCell){
		this.rows = rows;
		this.cols = cols;
		this.floor = new Cell[rows][cols];
		setAll(new Cell(defaultCell));
	}
	
	/**
	 * 
	 * @param rows		length of the floor
	 * @param cols			width of the floor
	 */
	public Floor(int rows, int cols) {
		this(rows, cols, CellType.FREE);
	}


	public Floor(Floor floor) {
		this.rows = floor.getRows();
		this.cols = floor.getCols();
		this.floor = new Cell[rows][cols];
		for (int i = 0; i < rows; i++)
			for (int j = 0; j < cols; j++)
				this.floor[i][j] = new Cell(floor.get(i, j));
	}

	public void setAll(Cell objectsType) {
		for (int i = 0; i < rows; i++)
			for (int j = 0; j < cols; j++)
				this.floor[i][j] = objectsType;
	}
	
	/**
	 * Generates randomly a floor scenario
	 * 
	 * @param numDirtySquares	quantity of dirty squares
	 * @param numObstacles		quantity of obstacles
	 */
	public void generateObject(int numObjects, Cell objectsType){

		class Square {
			int x, y;
			public Square(int x, int y){
				this.x = x;
				this.y = y;
			}
		}

		LinkedList<Square> cells = new LinkedList<Square>();

		for (int i = 0; i < getRows(); i++)
			for (int j = 0; j < getCols(); j++)
				if(floor[i][j].getType() == CellType.FREE)
					cells.add(new Square(i, j));

		Collections.shuffle(cells);
		int size = cells.size();

		if (numObjects > size)
			numObjects = size;
		
		if (numObjects < 0)
			numObjects = 0;
		

		for (int i = 0; i < numObjects; i++) {
			int random = Math.abs(Utils.random.nextInt()) % cells.size();
			Square myCell = cells.remove(random);
			floor[myCell.x][myCell.y] = objectsType;
		}
	}

	public void load(Floor floor){
		this.floor = floor.floor;
		this.cols = floor.cols;
		this.rows = floor.rows;
	}

	/**
	 * 
	 * @param i the row position of the Cell
	 * @param j the column position of the Cell
	 * @return the type of the Cell
	 */
	public Cell get(int i, int j){
		if(i<0 || j<0 || i>=rows || j>=cols)
			return new Cell(CellType.UNKNOWN);
		return this.floor[i][j];
	}

	/**
	 * Set the state of a Cell in the position (i,j).
	 * 
	 * @param i		the Cell's row
	 * @param j 	the Cell's column
	 * @param st	the Cell's state
	 */
	public void set(int i, int j, Cell st){
		if(i>=0 && j>=0 && i<=rows && j<=cols)
			this.floor[i][j] = st;
	}
	
	public void set(Position p, Cell st) {
		if (isValid(p))
			this.floor[p.row][p.col] = st;
	}

	public int getRows() {
		return rows;
	}

	public void setRows(int rows) {
		this.rows = rows;
	}

	public int getCols() {
		return cols;
	}

	public void setCols(int cols) {
		this.cols = cols;
	}

//	public Floor getCopy() {
//		Floor newFloor = new Floor(rows, cols);
//		for (int i = 0; i < rows; i++)
//			for (int j = 0; j < cols; j++)
//				newFloor.set(i, j, floor[i][j]);
//		return newFloor;
//	}
	
	public void mergeWith(Floor info) {
		if (rows == info.rows && cols == info.cols) {
			for (int i = 0; i < rows; i++) {
				for (int j = 0; j < cols; j++) {
					Cell current = info.get(i, j);
					if (current.getType() != CellType.UNKNOWN)
						set(i, j, current);
				}
			}
		}
	}
	
	@Override
	public String toString(){
		String out = "";
		for (int i = 0; i < rows; i++){
			for (int j = 0; j < cols; j++)
				out += get(i,j) + "\t";
			out += "\n";
		}
		return out;
	}

	public Cell get(Position p) {
		return get(p.row, p.col);
	}
	
	
	/**
	 * return the position of a cell of the specified type next to the specified position
	 * @param p the source position
	 * @param type the cell type to look for
	 * @param maxDistance the maximum distance admissible from the source position
	 * @return the position of the closest matching cell, if any. otherwise null
	 */
	public Position nextTo(Position p, CellType type, int maxDistance) {
		return nextTo(null, p, type, maxDistance);
	}
	
	public Position nextTo(Position source, Position target, CellType type, int maxDistance) {
		if (maxDistance == 0)
			return target;
		for (int minDistance = 1; minDistance <= maxDistance; minDistance++) {
			LinkedList<Position> candidates = new LinkedList<Position>();
			for (Direction d : Direction.ALL) {
				Position candidate = nextTo(source, target.step(d), type, minDistance-1);
				if (isValid(candidate) && get(candidate).type == type)
					candidates.add(candidate);
			}
			if (!candidates.isEmpty()) {
				if (source == null)
					return candidates.element();
				return source.nearest(candidates);
			}
		}
		return null;
	}

	public Cell getCopy(Position p) {
		return new Cell(get(p));
	}
	
	public boolean isValid(Position p) {
		return p != null && p.row >= 0 && p.col >= 0 && p.row <= rows && p.col <= cols; 
	}
	
}
