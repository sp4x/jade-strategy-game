package com.jrts.environment;

import java.io.Serializable;

import com.jrts.O2Ainterfaces.IUnit;

public class Cell implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private CellType type;
	private String id;
	private IUnit unit;
	
	int energy = 0;
	
	public Cell(CellType t) {
		type = t;
		id = null;
		unit = null;
	}
	
	public Cell(CellType t, int energy) {
		this.type = t;
		this.id = null;
		this.unit = null;
		this.energy = energy;
	}
	
	public Cell(CellType type, String id) {
		super();
		this.type = type;
		this.id = id;
		this.unit = null;
	}

	public Cell(String id, IUnit unit, CellType type) {
		super();
		this.type = type;
		this.id = id;
		this.unit = unit;
	}
	
	public Cell(Cell cell) {
		this.type = cell.type;
		this.id = cell.id;
		this.unit = cell.unit;
		this.energy = cell.energy;
	}

	public String getId() {
		return id;
	}
	
	public CellType getType() {
		return type;
	}

	public IUnit getUnit() {
		return unit;
	}
	
	public int getEnergy() {
		return energy;
	}

	public void setType(CellType type) {
		this.type = type;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setUnit(IUnit unit) {
		this.unit = unit;
	}

	public void setEnergy(int resourceEnergy) {
		this.energy = resourceEnergy;
	}
	
	public boolean isFree() {
		return type == CellType.FREE;
	}
	
	public boolean isWalkable() {
		return type == CellType.FREE || type == CellType.UNKNOWN;
	}
	
	public boolean isCityCenter() {
		return type == CellType.CITY_CENTER;
	}
	
	public boolean isFood() {
		return type == CellType.FOOD;
	}
	
	public boolean isWood() {
		return type == CellType.WOOD;
	}
	
	public boolean isUnit() {
		return type == CellType.SOLDIER || type == CellType.WORKER;
	}
	
	public boolean isUnknown() {
		return type == CellType.UNKNOWN;
	}
	
	public boolean isResource() {
		return isFood() || isWood();
	}
}
