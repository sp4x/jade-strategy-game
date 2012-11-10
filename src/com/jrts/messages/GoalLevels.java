package com.jrts.messages;

import jade.util.leap.Serializable;

import com.jrts.common.GoalPriority;

public class GoalLevels implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	GoalPriority resources;

	public GoalLevels(GoalPriority resources) {
		super();
		this.resources = resources;
	}

	public GoalPriority getResources() {
		return resources;
	}

	public void setResources(GoalPriority resources) {
		this.resources = resources;
	}
	
	

}
