package com.jrts.behaviours;

import java.util.Calendar;

import jade.core.behaviours.Behaviour;

import com.jrts.agents.Unit;
import com.jrts.common.GameStatistics;

public abstract class JrtsBehaviour extends Behaviour{
		
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
