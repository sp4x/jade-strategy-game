package com.jrts.common;

import jade.core.AID;

import java.util.HashMap;


public class UnitTable extends HashMap<AID, String> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public AID getFreeUnits() {
		for (AID agent : keySet()) {
			if (get(agent).equals(AgentStatus.FREE))
				return agent;
		}
		return null;
	}

}
