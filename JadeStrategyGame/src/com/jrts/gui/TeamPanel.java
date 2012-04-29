package com.jrts.gui;

import java.awt.Dimension;
import java.awt.FlowLayout;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class TeamPanel extends JPanel{
	private static final long serialVersionUID = 1L;
	protected JLabel food;
	protected JLabel wood;
	
	public TeamPanel(String teamName) {
		super(new FlowLayout());
		super.setBorder(BorderFactory.createTitledBorder(teamName));
		
		super.setPreferredSize(new Dimension(150, 50));

		this.food = new JLabel(ImageLoader.foodIcon);
		this.food.setText("3607"); 
		
		this.wood = new JLabel(ImageLoader.treeIcon);
		this.wood.setText("1500");
		
		super.add(this.food);
		super.add(this.wood);
	}

	
}
