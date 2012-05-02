package com.jrts.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.jrts.common.GameConfig;
import com.jrts.environment.Cell;
import com.jrts.environment.Floor;
import com.jrts.environment.Hit;

/**
 * Implement a JPanel to represent the environment and the agent
 *
 */
@SuppressWarnings("serial")
public class WorldViewPanel extends JPanel {

	private Floor floor;
	private ArrayList<Hit> hits;
	
	private JLabel[][] labelMatrix;

	public WorldViewPanel(Floor floor, ArrayList<Hit> hits) {
		this.floor = floor;
		this.hits = hits;
		init();
		update();
	}

	private void init() {
		setLayout(null);
		super.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		
		labelMatrix = new JLabel[floor.getRows()][floor.getCols()];
		for (int i = floor.getRows()-1; i >= 0 ; i--)
			for (int j = floor.getCols()-1; j >= 0; j--) {
				CellLabel label = new CellLabel(floor.get(i, j), i, j);
				labelMatrix[i][j] = label;
				super.add(label);
			}
		
		Dimension d = new Dimension((floor.getCols())*ImageLoader.iconSize, (floor.getRows())*ImageLoader.iconSize);
		setPreferredSize(d);
		setMinimumSize(d);
		setMaximumSize(d);
		setSize(d);
	}

	public void update() {
		for (int i = 0; i < floor.getRows(); i++)
			for (int j = 0; j < floor.getCols(); j++) {
				int y = (int) (j*ImageLoader.iconSize*GameConfig.HORIZONTAL_OVERLAP);
				int x = (int) (i*ImageLoader.iconSize*GameConfig.VERTICAL_OVERLAP);
				labelMatrix[i][j].setBounds( y, x, ImageLoader.iconSize, ImageLoader.iconSize);
				if(floor.get(i, j) == Cell.UNIT)
					labelMatrix[i][j].setIcon(ImageLoader.workerIcon);
				else if(Hit.isThereAnHit(hits,i,j))
					labelMatrix[i][j].setIcon(ImageLoader.foodIcon);
				else if(floor.get(i, j) == Cell.WOOD)
					labelMatrix[i][j].setIcon(ImageLoader.treeIcon);
				else if(floor.get(i, j) == Cell.FOOD)
					labelMatrix[i][j].setIcon(ImageLoader.foodIcon);
				else if(floor.get(i, j) == Cell.BUILDING)
					labelMatrix[i][j].setIcon(ImageLoader.workerFactoryIcon);
				else if(floor.get(i, j) == Cell.FREE)
					labelMatrix[i][j].setIcon(ImageLoader.freeIcon);
			}
	}
}
