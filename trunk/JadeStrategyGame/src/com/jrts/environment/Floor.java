package com.jrts.environment;

import java.io.Serializable;
import java.util.ArrayList;
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
	
	ArrayList<Position> busyCells = new ArrayList<Position>();
	
	public Floor(int rows, int cols, CellType defaultCell){
		this.rows = rows;
		this.cols = cols;
		floor = new Cell[rows][cols];
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
		rows = floor.getRows();
		cols = floor.getCols();
		this.floor = new Cell[rows][cols];
		for (int i = 0; i < rows; i++)
			for (int j = 0; j < cols; j++)
				set(i, j, new Cell(floor.get(i, j)));
	}

	public void setAll(Cell objectsType) {
		for (int i = 0; i < rows; i++)
			for (int j = 0; j < cols; j++)
				set(i, j, objectsType);
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
			set(myCell.x, myCell.y, objectsType);
		}
	}

	public void load(Floor floor){
		this.floor = floor.floor;
		cols = floor.cols;
		rows = floor.rows;
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
		return floor[i][j];
	}

	/**
	 * Set the state of a Cell in the position (i,j).
	 * 
	 * @param i		the Cell's row
	 * @param j 	the Cell's column
	 * @param st	the Cell's state
	 */
	public void set(int i, int j, Cell newCell){
		Position position = new Position(i, j);
		CellType newType = newCell.getType();
		CellType currType = CellType.FREE;
		if(floor[i][j] != null)
			currType = floor[i][j].getType();
		
		if(i>=0 && j>=0 && i<=rows && j<=cols){
			//se sto per settare una cella occupata
			if(!isTypeWalkable(newType) && !busyCells.contains(position))
				busyCells.add(position);
			//se sto per liberare una cella occupata
			if(isTypeWalkable(currType) && !isTypeWalkable(newType) && busyCells.contains(position))
				busyCells.remove(position);
			floor[i][j] = newCell;
		}
	}
	
	public void set(Position p, Cell st) {
		set(p.getRow(), p.getCol(), st);
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
	
	@Override
	public Floor clone() {
		Floor newFloor = new Floor(rows, cols);
		for (int i = 0; i < rows; i++)
			for (int j = 0; j < cols; j++)
				newFloor.set(i, j, floor[i][j]);
		return newFloor;
	}
	
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
	
	public Position nextTo(Position source, Position target, CellType type,
			int maxDistance) {
		if (!isValid(target))
			return null;
		if (maxDistance == 0 && get(target).type == type)
			return target;
		for (int d = 1; d <= maxDistance; d++) {
			LinkedList<Position> frame = frameWithType(target, type, d);
			if (!frame.isEmpty()) {
				if (source == null)
					return frame.element();
				return source.nearest(frame);
			}
		}
		return null;
	}
	
	private LinkedList<Position> frameWithType(Position center, CellType type, int d) {
		LinkedList<Position> frame = new LinkedList<Position>();
		int i, j;
		j = center.col - d;
		for (i = center.row - d; i <= center.row + d; i++) {
			Position candidate = new Position(i, j);
			if (isValid(candidate) && get(candidate).type == type)
				frame.add(candidate);
		}
		j = center.col + d;
		for (i = center.row - d; i <= center.row + d; i++) {
			Position candidate = new Position(i, j);
			if (isValid(candidate) && get(candidate).type == type)
				frame.add(candidate);
		}
		i = center.row - d + 1;
		for (j = center.col - d; j <= center.col + d; j++) {
			Position candidate = new Position(i, j);
			if (isValid(candidate) && get(candidate).type == type)
				frame.add(candidate);
		}
		i = center.row + d - 1;
		for (j = center.col - d; j <= center.col + d; j++) {
			Position candidate = new Position(i, j);
			if (isValid(candidate) && get(candidate).type == type)
				frame.add(candidate);
		}
		return frame;
	}
	
	public Cell getCopy(Position p) {
		return new Cell(get(p));
	}
	
	public boolean isValid(Position p) {
		return p != null && p.row >= 0 && p.col >= 0 && p.row < rows && p.col < cols; 
	}
	
	public boolean isWalkable(Position p) {
		return isValid(p) && isTypeWalkable(get(p).type);
	}
	
	public boolean isTypeWalkable(CellType type) {
		return type == CellType.FREE || type == CellType.UNKNOWN;
	}

	public ArrayList<Position> getBusyCells() {
		return busyCells;
	}
}
