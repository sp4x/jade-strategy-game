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
	Team team;
	
	public TeamResourcePanel(Team team) {
		super(new FlowLayout());
		
		this.team = team;
		
		TitledBorder border = BorderFactory.createTitledBorder(team.getTeamName());
		
		if(team.getTeamName().equals("team1")) border.setTitleColor(Color.RED);
		if(team.getTeamName().equals("team2")) border.setTitleColor(Color.BLACK);
		if(team.getTeamName().equals("team3")) border.setTitleColor(Color.ORANGE);
		if(team.getTeamName().equals("team4")) border.setTitleColor(Color.BLUE);
		
		super.setBorder(border);
		
		super.setPreferredSize(new Dimension(150, 50));

		this.food = new JLabel(ImageLoader.foodIcon);
		this.wood = new JLabel(ImageLoader.woodIcon);
		
		super.add(this.food);
		super.add(this.wood);
	}
	
	public void update () {
		if (team != null) {
			this.food.setText(""+team.getFood()); 
			this.wood.setText(""+team.getWood());
		}
	}
	
	public JLabel getFood() {
		return food;
	}

	public void setFood(JLabel food) {
		this.food = food;
	}

	public JLabel getWood() {
		return wood;
	}

	public void setWood(JLabel wood) {
		this.wood = wood;
	}

}
