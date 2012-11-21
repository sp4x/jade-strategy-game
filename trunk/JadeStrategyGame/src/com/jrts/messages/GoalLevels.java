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

	public int extimateResourceUnits() {
		
		if(getResources() == GoalPriority.LOW) return 2;
		else if(getResources() == GoalPriority.MEDIUM) return 4;
		else if(getResources() == GoalPriority.HIGH) return 6;
		
		return 0;
	}
	
	public int extimateFightingUnits() {

		if(getAttack() == GoalPriority.LOW) return 0;
		else if(getAttack() == GoalPriority.MEDIUM) return 10;
		else if(getAttack() == GoalPriority.HIGH) return 15;
		
		return 0;
	}
	
	public int extimatePatrolingUnits()
	{
		if(getDefence() == GoalPriority.LOW) return 1;
		else if(getDefence() == GoalPriority.MEDIUM) return  2;
		else if(getDefence() == GoalPriority.HIGH) return 4;
		
		return 0;
	}

	public int extimateExplorationUnits()
	{	
		if(getExploration() == GoalPriority.LOW) return 1;
		else if(getExploration() == GoalPriority.MEDIUM) return  2;
		else if(getExploration() == GoalPriority.HIGH) return 3;
		
		return 0;
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
	
	public int extimateNeededUnits() {
		return extimateExplorationUnits() + extimateFightingUnits()
				+ extimatePatrolingUnits() + extimateResourceUnits();
	}

}
