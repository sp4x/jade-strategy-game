package com.jrts.main;

import gui.MainFrame;
import jade.Boot;

import com.jrts.environment.World;

public class JadeStrategyGame {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		World.create(50, 50, 0.1f);
		String agents = "master1:agents.MasterAI;master2:agents.MasterAI";
		String[] jadeArgs = {"-local-host", "127.0.0.1", agents};
		Boot.main(jadeArgs);
		new MainFrame(World.getInstance().getFloor());
	}

}
