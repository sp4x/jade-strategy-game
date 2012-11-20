package com.jrts.common;

import jade.core.AID;

import java.util.ArrayList;
import java.util.HashMap;


public class UnitTable extends HashMap<AID, String> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public AID getAFreeUnit() {
		return getAUnitWithStatus(AgentStatus.FREE);
	}
	
	public ArrayList<AID> getFreeUnits() {
		ArrayList<AID> freeUnits = new ArrayList<AID>();
		for (AID agent : keySet()) {
			if (get(agent).equals(AgentStatus.FREE))
				freeUnits.add(agent);
		}
		return freeUnits;
	}
	
	public ArrayList<AID> getBusyUnits() {
		ArrayList<AID> busyUnits = new ArrayList<AID>();
		for (AID agent : keySet()) {
			if (!get(agent).equals(AgentStatus.FREE))
				busyUnits.add(agent);
		}
		return busyUnits;
	}
	
	public AID getAUnitWithStatus(String status) {
		for (AID agent : keySet()) {
			if (get(agent).equals(status))
				return agent;
		}
		return null;
	}

	public ArrayList<AID> getAllUnits() {
		ArrayList<AID> units = new ArrayList<AID>();
		for (AID agent : keySet()) {
			units.add(agent);
		}
		return units;
	}

}
