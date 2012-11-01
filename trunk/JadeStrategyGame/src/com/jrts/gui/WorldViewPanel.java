package com.jrts.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.BorderFactory;
import javax.swing.JPanel;

import com.jrts.common.GameConfig;
import com.jrts.environment.CellType;
import com.jrts.environment.Floor;
import com.jrts.environment.Position;
import com.jrts.logic.AttacksManager;

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
		for (int i = floor.getRows() - 1; i >= 0; i--)
			for (int j = floor.getCols() - 1; j >= 0; j--) {
				CellLabel label = new CellLabel(i, j);
				labelMatrix[i][j] = label;
				super.add(label);
			}

		Dimension d = new Dimension((floor.getCols()) * ImageLoader.iconSize, (floor.getRows()) * ImageLoader.iconSize);
		setPreferredSize(d);
		setMinimumSize(d);
		setMaximumSize(d);
		setSize(d);
	}

	public void update() {
		AttacksManager.update();
		/** take a snapshot of the floor at this moment and dispay it */
		Floor floorSnapshot = new Floor(floor);
		for (int i = 0; i < floorSnapshot.getRows(); i++)
			for (int j = 0; j < floorSnapshot.getCols(); j++) {
				CellLabel currCellLabel = labelMatrix[i][j];
				int y = (int) (j * ImageLoader.iconSize * GameConfig.HORIZONTAL_OVERLAP);
				int x = (int) (i * ImageLoader.iconSize * GameConfig.VERTICAL_OVERLAP);
				currCellLabel.setBounds(y, x, ImageLoader.iconSize, ImageLoader.iconSize);

				if (floorSnapshot.get(i, j).getType() == CellType.WORKER) {
					labelMatrix[i][j].setIcon(ImageLoader.getWorkerImageIcon(floorSnapshot.get(new Position(i, j)).getUnit().getTeamName()));
					// labelMatrix[i][j].setIcon(ImageLoader.workerIcon);
				} else if (floorSnapshot.get(i, j).getType() == CellType.SOLDIER) {
					labelMatrix[i][j].setIcon(ImageLoader.getSoldierImageIcon(floorSnapshot.get(new Position(i, j)).getUnit().getTeamName()));
					// labelMatrix[i][j].setIcon(ImageLoader.workerIcon);
				} else if (floorSnapshot.get(i, j).getType() == CellType.WOOD)
					labelMatrix[i][j].setIcon(ImageLoader.treeIcon);
				else if (floorSnapshot.get(i, j).getType() == CellType.FOOD)
					labelMatrix[i][j].setIcon(ImageLoader.foodIcon);
				else if (floorSnapshot.get(i, j).getType() == CellType.CITY_CENTER) {
					labelMatrix[i][j].setIcon(ImageLoader.getWorkerFactoryImageIcon(floorSnapshot.get(new Position(i, j)).getId()));
					// labelMatrix[i][j].setIcon(ImageLoader.workerFactoryIcon);
				} else if (AttacksManager.isThereAnHit(i, j))
					labelMatrix[i][j].setIcon(ImageLoader.hitIcon);
				else if (floorSnapshot.get(i, j).getType() == CellType.FREE)
					labelMatrix[i][j].setIcon(ImageLoader.freeIcon);
			}
	}

	public void paintComponent(Graphics g) {
		g.drawImage(ImageLoader.getBackgroundImage(this.getSize()).getImage(), 0, 0, null);
	}
}
