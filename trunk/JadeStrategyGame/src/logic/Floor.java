package logic;

import java.io.Serializable;

/**
 *  This class implement a Floor built of a Cell matrix where the agent acts.
 * 
 * @see Cell	 
 *
 */
public class Floor implements Serializable{

	public int rows;

	public int cols;

	private Cell [][] floor;
	
	/**
	 * 
	 * @param rows		length of the floor
	 * @param cols			width of the floor
	 */
	public Floor(int rows, int cols){
		this.rows = rows;
		this.cols = cols;
		this.floor = new Cell[rows][cols];
		for (int i = 0; i < rows; i++)
			for (int j = 0; j < cols; j++)
				this.floor[i][j] = Cell.FREE;
	}

	public void clear(){
		for (int i = 0; i < rows; i++)
			for (int j = 0; j < cols; j++)
				floor[i][j] = Cell.FREE;
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
			return Cell.UNKNOWN;
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
		if(i<0 || j<0 || i>=rows || j>=cols)
			return;
		this.floor[i][j] = st;
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

	public Floor getCopy() {
		Floor newFloor = new Floor(rows, cols);
		for (int i = 0; i < rows; i++)
			for (int j = 0; j < cols; j++)
				newFloor.set(i, j, floor[i][j]);
		return newFloor;
	}
}
