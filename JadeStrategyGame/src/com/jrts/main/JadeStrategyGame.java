package com.jrts.main;

import jade.Boot;

import com.common.GameConfig;
import com.jrts.environment.World;
import com.jrts.gui.MainFrame;

public class JadeStrategyGame {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		World.create(GameConfig.WORLD_ROWS, GameConfig.WORLD_COLS, 0.1f);
		String agents = "master1:com.jrts.agents.MasterAI;master2:com.jrts.agents.MasterAI";
		String[] jadeArgs = {"-local-host", "127.0.0.1", agents};
		Boot.main(jadeArgs);
		new MainFrame(World.getInstance().getFloor());
	}

}
