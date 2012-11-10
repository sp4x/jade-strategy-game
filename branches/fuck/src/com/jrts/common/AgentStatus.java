package com.jrts.common;

/**
 * An anget status identifies what a unit is currently doing.
 * It is also used by high-level AI to give unit orders
 * @author spax
 *
 */
public class AgentStatus {
	
	public static final String FREE_WORKER = "free_worker";
	public static final String FREE_SOLDIER = "free_soldier";

	public static final String WOOD_CUTTING = "wood-cutting";
	public static final String FOOD_COLLECTING = "food-collecting";

	public static final String PATROLING = "patroling";
	public static final String EXPLORING = "exploring";

}
