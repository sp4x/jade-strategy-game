package com.jrts.environment;

import java.util.Collection;
import java.util.GregorianCalendar;
import java.util.LinkedList;

import com.jrts.common.ResourcesContainer;
import com.jrts.common.TeamDF;
import com.jrts.common.Utils;
import com.jrts.messages.EnemySighting;

public class MasterPerception {
	
	WorldMap worldMap;
	ResourcesContainer resourcesContainer;
	Position cityCenter;
	Position meetingPoint;
	
	TeamDF teamDF;
	
	boolean alertNoMoreResources = false;
	long lastCityCenterUnderAttack = GregorianCalendar.getInstance().getTimeInMillis();
	
	int numTeams;
	
	public int numDeadWorkers = 0;
	public int numDeadSoldiers = 0;
	
	Collection<EnemySighting> enemySightings = new LinkedList<EnemySighting>();
	Collection<Position> threats = new LinkedList<Position>();
	Collection<Position> deaths = new LinkedList<Position>();
	
	public WorldMap getWorldMap() {
		return worldMap;
	}
	public void setWorldMap(WorldMap worldMap) {
		this.worldMap = worldMap;
	}
	public ResourcesContainer getResourcesContainer() {
		return resourcesContainer;
	}
	public void setResourcesContainer(ResourcesContainer resourcesContainer) {
		this.resourcesContainer = resourcesContainer;
	}
	public Position getCityCenter() {
		return cityCenter;
	}
	public void setCityCenter(Position cityCenter) {
		this.cityCenter = cityCenter;
	}
	
	public Position getMeetingPoint() {
		return meetingPoint;
	}
	public void setMeetingPoint(Position meetingPoint) {
		this.meetingPoint = meetingPoint;
	}
	public Collection<EnemySighting> getEnemySightings() {
		return enemySightings;
	}
	public void setEnemySightings(Collection<EnemySighting> enemySightings) {
		this.enemySightings = enemySightings;
	}
	public Collection<Position> getThreats() {
		return threats;
	}
	public void setThreats(Collection<Position> threats) {
		this.threats = threats;
	}
	public Collection<Position> getDeaths() {
		return deaths;
	}
	public void setDeaths(Collection<Position> deaths) {
		this.deaths = deaths;
	}
	public TeamDF getTeamDF() {
		return teamDF;
	}
	public void setTeamDF(TeamDF teamDF) {
		this.teamDF = teamDF;
	}
	
	public boolean isAlertNoMoreResources() {
		return alertNoMoreResources;
	}
	public void setAlertNoMoreResources(boolean alertNoMoreResources) {
		this.alertNoMoreResources = alertNoMoreResources;
	}

	public long getLastCityCenterUnderAttack() {
		return lastCityCenterUnderAttack;
	}
	public void setLastCityCenterUnderAttack(long lastCityCenterUnderAttack) {
		this.lastCityCenterUnderAttack = lastCityCenterUnderAttack;
	}

	public void clean() {
		enemySightings.clear();
		threats.clear();
		deaths.clear();
	}
	
	public int getNumTeams() {
		return numTeams;
	}
	public void setNumTeams(int leftTeams) {
		this.numTeams = leftTeams;
	}
	
	
	
}
