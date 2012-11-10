package com.jrts.common;

import jade.core.AID;

import java.util.HashMap;


public class WorkersMap extends HashMap<AID, String> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public AID getFreeWorker() {
		for (AID agent : keySet()) {
			if (get(agent).equals(AgentStatus.FREE_WORKER))
				return agent;
		}
		return null;
	}

}
