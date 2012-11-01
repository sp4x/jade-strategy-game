package com.jrts.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.BorderFactory;
import javax.swing.JPanel;

import com.jrts.common.GameConfig;
import com.jrts.common.ThreadMonitor;
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
		setBorder(BorderFactory.createLineBorder(Color.black, 1));
	}

	public void update() {
		AttacksManager.update();
		/** take a snapshot of the floor at this moment and dispay it */
		for (int i = 0; i < floor.getRows(); i++) {
			for (int j = 0; j < floor.getCols(); j++) {
				CellLabel currCellLabel = labelMatrix[i][j];
				int y = (int) (j * ImageLoader.iconSize * GameConfig.HORIZONTAL_OVERLAP);
				int x = (int) (i * ImageLoader.iconSize * GameConfig.VERTICAL_OVERLAP);
				currCellLabel.setBounds(y, x, ImageLoader.iconSize, ImageLoader.iconSize);

				if (floor.get(i, j).getType() == CellType.WORKER) {
					labelMatrix[i][j].setIcon(ImageLoader.getWorkerImageIcon(floor.get(new Position(i, j)).getUnit().getTeamName()));
					// labelMatrix[i][j].setIcon(ImageLoader.workerIcon);
				} else if (floor.get(i, j).getType() == CellType.SOLDIER) {
					labelMatrix[i][j].setIcon(ImageLoader.getSoldierImageIcon(floor.get(new Position(i, j)).getUnit().getTeamName()));
					// labelMatrix[i][j].setIcon(ImageLoader.workerIcon);
				} else if (floor.get(i, j).getType() == CellType.WOOD)
					labelMatrix[i][j].setIcon(ImageLoader.treeIcon);
				else if (floor.get(i, j).getType() == CellType.FOOD)
					labelMatrix[i][j].setIcon(ImageLoader.foodIcon);
				else if (floor.get(i, j).getType() == CellType.CITY_CENTER) {
					labelMatrix[i][j].setIcon(ImageLoader.getWorkerFactoryImageIcon(floor.get(new Position(i, j)).getId()));
					// labelMatrix[i][j].setIcon(ImageLoader.workerFactoryIcon);
				} else if (AttacksManager.isThereAnHit(i, j))
					labelMatrix[i][j].setIcon(ImageLoader.hitIcon);
				else if (floor.get(i, j).getType() == CellType.FREE)
					labelMatrix[i][j].setIcon(ImageLoader.freeIcon);
			}
		}
		ThreadMonitor.getInstance().sendNotifyAll();
	}

	public void paintComponent(Graphics g) {
		g.drawImage(ImageLoader.getBackgroundImage(this.getSize()).getImage(), 0, 0, null);
	}
}
