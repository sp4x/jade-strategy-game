
package com.jrts.common;

public class ResourcesContainer {

	private int wood;
	private int food;
	
	public ResourcesContainer(int wood, int food) {
		this.wood = wood;
		this.food = food;
	}
	
	public synchronized int getWood() {
		return wood;
	}
	
	public synchronized int getFood() {
		return food;
	}
	
	public synchronized void addWood(int qnt) {
		wood += qnt;
	}
	
	public synchronized void addFood(int qnt) {
		food += qnt;
	}
	
	public synchronized boolean removeWood(int qnt) {
		if (wood - qnt < 0)
			return false;
		wood -= qnt;
		return true;
	}
	
	public synchronized boolean removeFood(int qnt) {
		if (food - qnt < 0)
			return false;
		food -= qnt;
		return true;
	}
	
	public synchronized boolean isThereEnoughWood(int qnt) {
		if (wood - qnt < 0)
			return false;
		return true;
	}
	
	public synchronized boolean isThereEnoughFood(int qnt) {
		if (food - qnt < 0)
			return false;
		return true;
	}
}
