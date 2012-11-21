package com.jrts.messages;

import java.io.Serializable;

import com.jrts.environment.CellType;
import com.jrts.environment.Position;

public class EnemySightingItem implements Serializable {
	private static final long serialVersionUID = 1L;
	
	Position position;
	String id;
	CellType type;
	
	public EnemySightingItem(Position position, String id, CellType type) {
		super();
		this.position = position;
		this.id = id;
		this.type = type;
	}
	public Position getPosition() {
		return position;
	}
	public void setPosition(Position position) {
		this.position = position;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public CellType getType() {
		return type;
	}
	public void setType(CellType type) {
		this.type = type;
	}
	
	public boolean isWorker() {
		return type == CellType.WORKER;
	}
	
	public boolean isSoldier() {
		return type == CellType.SOLDIER;
	}
}