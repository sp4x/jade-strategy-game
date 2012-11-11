package com.jrts.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;

import com.jrts.O2Ainterfaces.Team;
import com.jrts.common.GameConfig;
import com.jrts.environment.Cell;
import com.jrts.environment.CellType;

public class TeamVisibilityPanel extends JPanel {
	private static final long serialVersionUID = 1L;

	//Team team;
	
	public TeamVisibilityPanel(Team team) {
		super(new FlowLayout());

		//this.team = team;

		TitledBorder border = BorderFactory.createTitledBorder(team
				.getTeamName());

		if (team.getTeamName().equals("team1"))
			border.setTitleColor(Color.RED);
		else if (team.getTeamName().equals("team2"))
			border.setTitleColor(Color.BLACK);
		else if (team.getTeamName().equals("team3"))
			border.setTitleColor(Color.ORANGE);
		else if (team.getTeamName().equals("team4"))
			border.setTitleColor(Color.BLUE);

		super.setBorder(border);

		// The Map is (40*15)x(40*15) => (row*iconSize)x(row*iconSize)
		int width = GameConfig.WORLD_COLS * GameConfig.WORLD_VISIBILITY_MAP_FACTOR + 20;
		int height = GameConfig.WORLD_ROWS * GameConfig.WORLD_VISIBILITY_MAP_FACTOR + 30;
		
		Dimension d = new Dimension(width, height);
		super.setSize(d);
		super.setMinimumSize(d);
		super.setMaximumSize(d);
		super.setPreferredSize(d);super.setPreferredSize(new Dimension(width, height));

		super.setLayout(new FlowLayout(FlowLayout.CENTER));
		super.add(new VisibilityPanel(team), BorderLayout.CENTER);
	}

	class VisibilityPanel extends JPanel {
		
		private static final long serialVersionUID = 1L;
		Team team;
		
		public VisibilityPanel(Team t) {
			
			this.team = t;
			
			int width = GameConfig.WORLD_COLS * GameConfig.WORLD_VISIBILITY_MAP_FACTOR;
			int height = GameConfig.WORLD_ROWS * GameConfig.WORLD_VISIBILITY_MAP_FACTOR;
			
			Dimension d = new Dimension(width, height);
			
			super.setSize(d);
			super.setMinimumSize(d);
			super.setMaximumSize(d);
			super.setPreferredSize(d);
		}

		public void paintComponent(Graphics g) {
			super.paintComponent(g);

			Graphics2D g2d = (Graphics2D) g;
			
			for (int i = 0; i < GameConfig.WORLD_COLS; i++) {
				for (int j = 0; j < GameConfig.WORLD_ROWS; j++) {
				    
					Cell cell = this.team.getWorldMap().get(j, i);
					CellType type = cell.getType();
					if(type.equals(CellType.UNKNOWN))
						g2d.setPaint(Color.DARK_GRAY);
					else if(type.equals(CellType.WOOD))
						g2d.setPaint(Color.GREEN);
					else if(type.equals(CellType.FOOD))
						g2d.setPaint(Color.RED);
					else if(type.equals(CellType.FREE))
						g2d.setPaint(Color.GRAY);
//					else if (team.getTeamName().contains("team1"))
//						g2d.setPaint(Color.RED);
//					else if (team.getTeamName().contains("team2"))
//						g2d.setPaint(Color.BLACK);
//					else if (team.getTeamName().contains("team3"))
//						g2d.setPaint(Color.ORANGE);
//					else if (team.getTeamName().contains("team4"))
//						g2d.setPaint(Color.BLUE);
					else 
						g2d.setPaint(Color.BLUE);
					
					/*
					switch (r.nextInt(50)) {
					case 0:
						g2d.setPaint(Color.BLACK);
						break;
					case 1:
						g2d.setPaint(Color.BLUE);
						break;
					default:
						g2d.setPaint(Color.WHITE);
						break;
					}
					*/
					
					/*
					if(j == GameConfig.WORLD_ROWS-1 || j == 0 || i == GameConfig.WORLD_COLS-1 || i == 0)
						g2d.setPaint(Color.RED);
					*/
					int scaledI = i*GameConfig.WORLD_VISIBILITY_MAP_FACTOR;
					int scaledJ = j*GameConfig.WORLD_VISIBILITY_MAP_FACTOR;
					
				    g2d.fill(new Rectangle2D.Double(scaledI, scaledJ, GameConfig.WORLD_VISIBILITY_MAP_FACTOR, GameConfig.WORLD_VISIBILITY_MAP_FACTOR));
				}
			}
		}
		
	}
}
