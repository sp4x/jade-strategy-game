package com.jrts.gui;

import java.awt.GridLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;

import com.jrts.O2Ainterfaces.Team;

public class TeamInfoPanel extends JPanel {

	private static final long serialVersionUID = 1L;
	Team team;

	JLabel resources = new JLabel("");
	JLabel attack = new JLabel("");
	JLabel defence = new JLabel("");
	JLabel exploration = new JLabel("");
	JLabel numDeadWorkers = new JLabel("0");
	JLabel numDeadSoldiers = new JLabel("0");
	
	public TeamInfoPanel(Team team) {
		super(new GridLayout(6, 2));

		this.team = team;
		
		add(new JLabel("Risorse:"));
		add(resources);
		
		add(new JLabel("Attacco:"));
		add(attack);
		
		add(new JLabel("Difesa:"));
		add(defence);
		
		add(new JLabel("Esplorazione:"));
		add(exploration);
		
		add(new JLabel("Worker Persi:"));
		add(numDeadWorkers);
		
		add(new JLabel("Soldier Persi:"));
		add(numDeadSoldiers);
	}
	
	public void update () {
		if (team != null) {
			this.resources.setText(team.getGoalLevels().getResources() + ""); 
			this.attack.setText(team.getGoalLevels().getAttack() + "");
			this.defence.setText(team.getGoalLevels().getDefence() + "");
			this.exploration.setText(team.getGoalLevels().getExploration() + "");

			this.numDeadWorkers.setText(team.getNumDeadWorkers() + "");
			this.numDeadSoldiers.setText(team.getNumDeadSoldiers() + "");
		}
	}
}
