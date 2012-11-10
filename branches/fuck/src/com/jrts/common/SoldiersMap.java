package com.jrts.common;

import jade.core.AID;

import java.util.HashMap;


public class SoldiersMap extends HashMap<AID, String> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public AID getFreeSoldier() {
		for (AID agent : keySet()) {
			if (get(agent).equals(AgentStatus.FREE_SOLDIER))
				return agent;
		}
		
		return null;
	}

}
