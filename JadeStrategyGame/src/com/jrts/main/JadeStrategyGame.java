package com.jrts.main;

import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.core.Runtime;
import jade.wrapper.AgentContainer;
import jade.wrapper.AgentController;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;

import com.jrts.O2Ainterfaces.Team;
import com.jrts.agents.MasterAI;
import com.jrts.common.GameConfig;
import com.jrts.environment.World;
import com.jrts.gui.MainFrame;

public class JadeStrategyGame {

	public static void main(String[] args) throws Exception {
		String teamsString = "";
		do {
			teamsString = JOptionPane.showInputDialog("Number of teams (max 4)", "2");
			if (teamsString == null)
				System.exit(0);
		} while (!teamsString.matches("\\d+") || Integer.valueOf(teamsString) > 4);
		int teamNumber = Integer.valueOf(teamsString);

		/** start jade runtime */
		Runtime rt = Runtime.instance();
		ProfileImpl p = new ProfileImpl();
		p.setParameter(Profile.LOCAL_HOST, Profile.LOOPBACK_ADDRESS_CONSTANT);

		// Runtime.instance().setCloseVM(true);

		AgentContainer ac = rt.createMainContainer(p);

		List<Team> teams = new ArrayList<Team>();

		World.create(GameConfig.WORLD_ROWS, GameConfig.WORLD_COLS, 0.01f);

		/** create and start all the team's masterAi */
		for (int i = 1; i <= teamNumber; i++) {
			String teamName = "team" + i;
			AgentController controller = ac.createNewAgent(teamName, MasterAI.class.getName(), new Object[0]);
			controller.start();

			Team team = controller.getO2AInterface(Team.class);
			teams.add(team);
			World.getInstance().addTeam(teamName);
		}

		/** start graphics */
		MainFrame.start(World.getInstance().getSnapshot(), teams, ac);
	}

}
