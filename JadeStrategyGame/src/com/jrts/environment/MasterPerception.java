package com.jrts.environment;

import java.util.Collection;
import java.util.LinkedList;

import com.jrts.common.ResourcesContainer;
import com.jrts.common.TeamDF;
import com.jrts.messages.EnemySighting;

public class MasterPerception {
	
	WorldMap worldMap;
	ResourcesContainer resourcesContainer;
	Position cityCenter;
	
	TeamDF teamDF;
	
	boolean alertNoMoreResources = false;
	boolean alertCityCenterUnderAttack = false;
	
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
	
	public boolean isAlertCityCenterUnderAttack() {
		return alertCityCenterUnderAttack;
	}
	public void setAlertCityCenterUnderAttack(boolean alertCityCenterUnderAttack) {
		this.alertCityCenterUnderAttack = alertCityCenterUnderAttack;
	}
	
	public void clean() {
		enemySightings.clear();
		threats.clear();
		deaths.clear();
		alertCityCenterUnderAttack = false;
	}
	
	
	
}
