package com.jrts.behaviours;

import jade.core.behaviours.Behaviour;

import java.util.Calendar;

import com.jrts.agents.Unit;
import com.jrts.common.GameStatistics;

public abstract class JrtsBehaviour extends Behaviour{
		
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public JrtsBehaviour(Unit u) {
		super(u);
	}

	public JrtsBehaviour() {
		super();
	}

	@Override
	public void action(){
		GameStatistics.increaseCounter();
		long timeBefore, timeAfter;
		
		timeBefore = Calendar.getInstance().getTimeInMillis();
		unitAction();
		timeAfter = Calendar.getInstance().getTimeInMillis();
		
		GameStatistics.saveElapsedTime(this.getClass().getName(), timeAfter - timeBefore);
	}
	
	public abstract void unitAction();
}
