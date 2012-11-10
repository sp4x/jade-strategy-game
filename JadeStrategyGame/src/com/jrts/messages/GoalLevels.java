package com.jrts.messages;

import jade.util.leap.Serializable;

import com.jrts.common.GoalPriority;

public class GoalLevels implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	GoalPriority resources;
	GoalPriority attack;
	GoalPriority defence;
	GoalPriority exploration;

	public GoalLevels() {
		// TODO Auto-generated constructor stub
	}
	
	public GoalLevels(GoalPriority resources, GoalPriority attack,
			GoalPriority defence, GoalPriority exploration) {
		super();
		this.resources = resources;
		this.attack = attack;
		this.defence = defence;
		this.exploration = exploration;
	}



	public GoalPriority getResources() {
		return resources;
	}

	public void setResources(GoalPriority resources) {
		this.resources = resources;
	}

	public GoalPriority getAttack() {
		return attack;
	}

	public void setAttack(GoalPriority attack) {
		this.attack = attack;
	}

	public GoalPriority getDefence() {
		return defence;
	}

	public void setDefence(GoalPriority defence) {
		this.defence = defence;
	}

	public GoalPriority getExploration() {
		return exploration;
	}

	public void setExploration(GoalPriority exploration) {
		this.exploration = exploration;
	}
	
}
