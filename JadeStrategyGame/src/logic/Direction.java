package logic;

public enum Direction {
	RIGHT(0,1),
	LEFT(0,-1),
	UP(-1,0),
	DOWN(1,0),
	RIGHT_UP(-1,1),
	RIGHT_DOWN(1,1),
	LEFT_UP(-1,-1),
	LEFT_DOWN(1,-1);
	
	private int rowVar;
	private int colVar;
	
	Direction(int rowVar, int colVar){
		this.rowVar = rowVar;
		this.colVar = colVar;
	}
	
	public int rowVar(){
		return rowVar;
	}
	
	public int colVar(){
		return colVar;
	}
}
