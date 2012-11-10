package com.jrts.messages;

import jade.util.leap.Serializable;

public class AggiornaRisorse implements Serializable {

	private static final long serialVersionUID = 4980040343591505103L;

	private int wood = 0;
	private int food = 0;

	public AggiornaRisorse() {
	}

	public AggiornaRisorse(int wood, int food) {
		this.wood = wood;
		this.food = food;
	}

	public int getWood() {
		return wood;
	}

	public void setWood(int wood) {
		this.wood = wood;
	}

	public int getFood() {
		return food;
	}

	public void setFood(int food) {
		this.food = food;
	}
}
