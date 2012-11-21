package com.jrts.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;

import com.jrts.O2Ainterfaces.Team;

public class TeamResourcePanel extends JPanel{
	private static final long serialVersionUID = 1L;
	
	JLabel food;
	JLabel wood;
	JLabel workers;
	JLabel soldiers;
	
	Team team;
	
	public TeamResourcePanel(Team team) {
		super(new FlowLayout());
		
		this.team = team;
		
		TitledBorder border = BorderFactory.createTitledBorder(team.getTeamName() + " (" + team.getTeamNature().toString().toLowerCase() + ")");

		this.food = new JLabel(ImageLoader.foodIcon);
		this.wood = new JLabel(ImageLoader.woodIcon);
		
		if(team.getTeamName().equals("team1"))
		{
			border.setTitleColor(Color.RED);
			this.workers = new JLabel(ImageLoader.workerIcon1);
			this.soldiers = new JLabel(ImageLoader.soldierIcon1);
		} else if(team.getTeamName().equals("team2")) {
			this.workers = new JLabel(ImageLoader.workerIcon2);
			this.soldiers = new JLabel(ImageLoader.soldierIcon2);			
			border.setTitleColor(Color.BLACK);
		} else if(team.getTeamName().equals("team3")){
			this.workers = new JLabel(ImageLoader.workerIcon3);
			this.soldiers = new JLabel(ImageLoader.soldierIcon3);
			border.setTitleColor(Color.ORANGE);
		} else if(team.getTeamName().equals("team4")){
			this.workers = new JLabel(ImageLoader.workerIcon4);
			this.soldiers = new JLabel(ImageLoader.soldierIcon4);
			border.setTitleColor(Color.BLUE);
		}
		
		super.setBorder(border);
		super.setPreferredSize(new Dimension(215, 50));

		super.add(this.food);
		super.add(this.wood);
		super.add(this.workers);
		super.add(this.soldiers);
	}
	
	public void update () {
		if (team != null) {
			this.food.setText(team.getFood() + "   "); 
			this.wood.setText(team.getWood() + "   ");
			this.workers.setText(team.getQueueWorkerCount() + "   ");
			this.soldiers.setText("" + team.getQueueSoldierCount());
		}
	}
}
