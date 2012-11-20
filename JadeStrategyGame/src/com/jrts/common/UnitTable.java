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
		for (AID agent : keySet()) {
			if (get(agent).equals(AgentStatus.FREE))
				return agent;
		}
		return null;
	}
	
	public ArrayList<AID> getFreeUnits() {
		ArrayList<AID> freeUnits = new ArrayList<AID>();
		for (AID agent : keySet()) {
			if (get(agent).equals(AgentStatus.FREE))
				freeUnits.add(agent);
		}
		return freeUnits;
	}

	public ArrayList<AID> getAllUnits() {
		ArrayList<AID> units = new ArrayList<AID>();
		for (AID agent : keySet()) {
			units.add(agent);
		}
		return units;
	}

}
