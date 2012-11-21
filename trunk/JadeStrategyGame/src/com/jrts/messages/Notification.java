package com.jrts.messages;

import java.io.Serializable;

/**
 * Used to send notification like "there is an enemy here: 10,30" or
 * "there is no more food in our known world"
 * 
 */
public class Notification implements Serializable {
	private static final long serialVersionUID = -4103939700986977091L;
	
	public static final String ENEMY_SIGHTED = "enemy_sighted";
	public static final String NO_MORE_RESOURCE = "no_more_resource";
	public static final String RESOURCES_UPDATE = "resources_update";
	public static final String GOAL_LEVELS = "goal_levels";
	public static final String PERCEPTION = "perception";
	public static final String ORDER = "order";
	public static final String TEAM_DECEASED = "team_deceased";
	public static final String UNIT_DEATH = "unit_death";
	public static final String UNIT_UNDER_ATTACK = "under_attack";
	public static final String NEW_WORKER = "new_worker";
	public static final String NEW_SOLDIER = "new_soldier";
	public static final String READY_TO_BE_UPGRADED = "ready_to_be_upgraded";
	public static final String RESOURCES_FOUND = "resources_found";
	public static final String CITYCENTER_UNDER_ATTACK = "citycenter_under_attack";

	public static final String ATTACK = "attack";

	String subject;
	Serializable contentObject;

	public Notification(String messageSubject, Serializable contentObject) {
		this.subject = messageSubject;
		this.contentObject = contentObject;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public Serializable getContentObject() {
		return contentObject;
	}

	public void setContentObject(Serializable contentObject) {
		this.contentObject = contentObject;
	}
}
