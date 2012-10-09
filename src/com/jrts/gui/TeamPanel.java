package com.jrts.gui;

import java.awt.Dimension;
import java.awt.FlowLayout;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.jrts.O2Ainterfaces.Team;

public class TeamPanel extends JPanel{
	private static final long serialVersionUID = 1L;
	
	JLabel food;
	JLabel wood;
	Team team;
	
	public TeamPanel(Team team) {
		super(new FlowLayout());
		
		this.team = team;
		
		super.setBorder(BorderFactory.createTitledBorder(team.getTeamName()));
		
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
		} else {
			System.err.println("CHE SUCCEDE?");
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
