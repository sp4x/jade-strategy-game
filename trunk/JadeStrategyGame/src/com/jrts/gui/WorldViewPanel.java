package com.jrts.gui;

import jade.domain.introspection.GetValue;

import java.awt.Color;
import java.awt.Dimension;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.jrts.common.GameConfig;
import com.jrts.environment.Cell;
import com.jrts.environment.CellType;
import com.jrts.environment.Floor;

/**
 * Implement a JPanel to represent the environment and the agent
 *
 */
@SuppressWarnings("serial")
public class WorldViewPanel extends JPanel {

	private Floor floor;
	
	CellLabel[][] labelMatrix;

	public WorldViewPanel(Floor floor) {
		this.floor = floor;
		init();
		update();
	}

	private void init() {
		setLayout(null);
		super.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		
		labelMatrix = new CellLabel[floor.getRows()][floor.getCols()];
		for (int i = floor.getRows()-1; i >= 0 ; i--)
			for (int j = floor.getCols()-1; j >= 0; j--) {
				CellLabel label = new CellLabel(i, j);
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
		AttacksManager.update();
		for (int i = 0; i < floor.getRows(); i++)
			for (int j = 0; j < floor.getCols(); j++) {
				int y = (int) (j*ImageLoader.iconSize*GameConfig.HORIZONTAL_OVERLAP);
				int x = (int) (i*ImageLoader.iconSize*GameConfig.VERTICAL_OVERLAP);
				labelMatrix[i][j].setBounds( y, x, ImageLoader.iconSize, ImageLoader.iconSize);
				
				if(floor.get(i, j).getType() == CellType.UNIT)
					labelMatrix[i][j].setIcon(ImageLoader.workerIcon);
				else if(floor.get(i, j).getType() == CellType.WOOD)
					labelMatrix[i][j].setIcon(ImageLoader.treeIcon);
				else if(floor.get(i, j).getType() == CellType.FOOD)
					labelMatrix[i][j].setIcon(ImageLoader.foodIcon);
				else if(floor.get(i, j).getType() == CellType.BUILDING)
					labelMatrix[i][j].setIcon(ImageLoader.workerFactoryIcon);
				else if(AttacksManager.isThereAnHit(i,j))
					labelMatrix[i][j].setIcon(ImageLoader.hitIcon);
				else if(floor.get(i, j).getType() == CellType.FREE)
					labelMatrix[i][j].setIcon(ImageLoader.freeIcon);
			}
	}
}
