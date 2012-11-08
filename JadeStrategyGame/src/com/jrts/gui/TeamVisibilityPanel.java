package com.jrts.gui;

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
import com.jrts.environment.CellType;

public class TeamVisibilityPanel extends JPanel {
	private static final long serialVersionUID = 1L;

	Team team;

	public TeamVisibilityPanel(Team team) {
		super(new FlowLayout());

		this.team = team;

		TitledBorder border = BorderFactory.createTitledBorder(team
				.getTeamName());

		if (team.getTeamName().equals("team1"))
			border.setTitleColor(Color.RED);
		if (team.getTeamName().equals("team2"))
			border.setTitleColor(Color.BLACK);
		if (team.getTeamName().equals("team3"))
			border.setTitleColor(Color.ORANGE);
		if (team.getTeamName().equals("team4"))
			border.setTitleColor(Color.BLUE);

		super.setBorder(border);

		// The Map is (40*15)x(40*15) => (row*iconSize)x(row*iconSize)
		super.setPreferredSize(new Dimension(GameConfig.WORLD_ROWS
				* GameConfig.WORLD_VISIBILITY_MAP_FACTOR + 15, GameConfig.WORLD_COLS
				* GameConfig.WORLD_VISIBILITY_MAP_FACTOR + 35));

		super.setLayout(new FlowLayout(FlowLayout.CENTER));
		super.add(new VisibilityPanel());
	}

	class VisibilityPanel extends JPanel {
		
		public VisibilityPanel() {
			// The Map is (40*15)x(40*15) => (row*iconSize)x(row*iconSize)
			Dimension d = new Dimension(GameConfig.WORLD_ROWS
					* GameConfig.WORLD_VISIBILITY_MAP_FACTOR,
					GameConfig.WORLD_COLS
							* GameConfig.WORLD_VISIBILITY_MAP_FACTOR);
			
			super.setSize(d);
			super.setMinimumSize(d);
			super.setMaximumSize(d);
			super.setPreferredSize(d);
		}

		public void paintComponent(Graphics g) {
			super.paintComponent(g);

			Graphics2D g2d = (Graphics2D) g;
			g2d.setColor(Color.BLACK);
			
			/*
			WorldMap map = new WorldMap(40, 40);
			map.set(10, 10, new Cell(CellType.FOOD));
			map.set(20, 20, new Cell(CellType.FOOD));
			map.set(30, 15, new Cell(CellType.FOOD));
			*/
			
			for (int i = 0; i <= super.getSize().getWidth(); i++) {
				for (int j = 0; j <= super.getSize().getHeight(); j++) {
				    
					if(team.getWorldMap().get(i, j).getType().equals(CellType.UNKNOWN))
						g2d.setPaint(Color.BLACK);
					else 
						g2d.setPaint(Color.WHITE);

					int scaledI = i*GameConfig.WORLD_VISIBILITY_MAP_FACTOR;
					int scaledJ = j*GameConfig.WORLD_VISIBILITY_MAP_FACTOR;
					
				    g2d.fill(new Rectangle2D.Double(scaledI, scaledJ, GameConfig.WORLD_VISIBILITY_MAP_FACTOR, GameConfig.WORLD_VISIBILITY_MAP_FACTOR));
				}
			}
		}
	}
}
