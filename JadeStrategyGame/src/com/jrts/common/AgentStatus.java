package com.jrts.common;

/**
 * An anget status identifies what a unit is currently doing.
 * It is also used by high-level AI to give unit orders
 * @author spax
 *
 */
public class AgentStatus {
	
	public static final String FREE = "free";

	public static final String WOOD_CUTTING = "Wood Cutting";
	public static final String FOOD_COLLECTING = "Food Collecting";

	public static final String PATROLING = "Patroling";
	public static final String EXPLORING = "Exploring";

	public static final String GO_UPGRADING = "Go  Upgrading";
	public static final String GO_FIGHTING = "Go Fighting";
	public static final String FIGHTING = "Fighting";
	public static final String DYING = "Dying";

}
