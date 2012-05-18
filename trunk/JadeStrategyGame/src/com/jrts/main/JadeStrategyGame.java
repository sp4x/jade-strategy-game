package com.jrts.main;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;

import jade.Boot;
import jade.core.ProfileImpl;
import jade.core.Runtime;
import jade.util.leap.Properties;
import jade.wrapper.AgentContainer;
import jade.wrapper.AgentController;
import com.jrts.O2Ainterfaces.Team;
import com.jrts.agents.MasterAI;
import com.jrts.common.GameConfig;
import com.jrts.environment.World;
import com.jrts.gui.MainFrame;


public class JadeStrategyGame {

	public static void main(String[] args) throws Exception {
		World.create(GameConfig.WORLD_ROWS, GameConfig.WORLD_COLS, 0.1f);
		
		String teamsString = "";
		do {
			teamsString = JOptionPane.showInputDialog("Number of teams", "2");
		}
		while (!teamsString.matches("\\d+"));
		int teamNumber = Integer.valueOf(teamsString);
		
		/** start jade runtime */
		Runtime rt = Runtime.instance();
		String[] jadeArgs = {"-local-host", "127.0.0.1"};
		Properties pp = Boot.parseCmdLineArgs(jadeArgs);
		ProfileImpl p = new ProfileImpl(pp);
		
		Runtime.instance().setCloseVM(true);
		
		AgentContainer ac = rt.createMainContainer(p);

		List<Team> teams = new ArrayList<Team>();
		
		/** create and start all the team's masterAi */
		for (int i = 1; i <= teamNumber; i++) {
			AgentController controller = ac.createNewAgent("team" + i,
					MasterAI.class.getName(), new Object[0]);
			controller.start();
			
			Team team = controller.getO2AInterface(Team.class);
			teams.add(team);
		}
		
		/** start graphics */
		MainFrame.start(World.getInstance().getFloor(), teams);
	}

}
