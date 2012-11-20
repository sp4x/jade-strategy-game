package com.jrts.environment;

import java.util.Collection;

import com.jrts.common.ResourcesContainer;
import com.jrts.common.TeamDF;
import com.jrts.messages.EnemySighting;

public class MasterPerception {
	
	WorldMap worldMap;
	ResourcesContainer resourcesContainer;
	Position cityCenter;
	
	int numWorkers;
	int numSoldiers;
	TeamDF teamDF;
	
	Collection<EnemySighting> enemySightings;
	Collection<Position> threats;
	Collection<Position> deaths;
	
	
	
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
	public int getNumWorkers() {
		return numWorkers;
	}
	public void setNumWorkers(int numWorkers) {
		this.numWorkers = numWorkers;
	}
	public int getNumSoldiers() {
		return numSoldiers;
	}
	public void setNumSoldiers(int numSoldiers) {
		this.numSoldiers = numSoldiers;
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
	
	
	
	
}
