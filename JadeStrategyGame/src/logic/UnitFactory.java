package logic;

import jade.core.Agent;

public abstract class UnitFactory extends Agent{
	/**
	 * 
	 */
	private static final long serialVersionUID = 8239388979713620206L;

	abstract void createUnit(int x, int y);
}
