package com.jrts.messages;

import jade.util.leap.Serializable;

public class GetPath implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 5802697270808291950L;
	private int srcRow, srcCol, dstRow, dstCol;
	
	public GetPath(int srcRow, int srcCol, int dstRow, int dstCol) {
		super();
		this.srcRow = srcRow;
		this.srcCol = srcCol;
		this.dstRow = dstRow;
		this.dstCol = dstCol;
	}

	public int getSrcRow() {
		return srcRow;
	}

	public void setSrcRow(int srcRow) {
		this.srcRow = srcRow;
	}

	public int getSrcCol() {
		return srcCol;
	}

	public void setSrcCol(int srcCol) {
		this.srcCol = srcCol;
	}

	public int getDstRow() {
		return dstRow;
	}

	public void setDstRow(int dstRow) {
		this.dstRow = dstRow;
	}

	public int getDstCol() {
		return dstCol;
	}

	public void setDstCol(int dstCol) {
		this.dstCol = dstCol;
	}
}
