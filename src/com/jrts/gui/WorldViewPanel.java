package com.jrts.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.BorderFactory;
import javax.swing.Icon;
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

		labelMatrix = new CellLabel[floor.getRows()][floor.getCols()];
		for (int i = floor.getRows() - 1; i >= 0; i--)
			for (int j = floor.getCols() - 1; j >= 0; j--) {
				CellLabel label = new CellLabel(i, j);
				labelMatrix[i][j] = label;
				super.add(label);
			}

		Dimension d = new Dimension((floor.getCols()) * GameConfig.ICON_SIZE, (floor.getRows()) * GameConfig.ICON_SIZE);
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
				int y = (int) (j * GameConfig.ICON_SIZE * GameConfig.HORIZONTAL_OVERLAP);
				int x = (int) (i * GameConfig.ICON_SIZE * GameConfig.VERTICAL_OVERLAP);
				currCellLabel.setBounds(y, x, GameConfig.ICON_SIZE, GameConfig.ICON_SIZE);

				Icon icon = null;
				switch (floor.get(i, j).getType()) {
				case WORKER:
					icon = ImageLoader.getWorkerImageIcon(floor.get(new Position(i, j)).getUnit().getTeamName());
					break;
				case SOLDIER:
					icon = ImageLoader.getSoldierImageIcon(floor.get(new Position(i, j)).getUnit().getTeamName());
					break;
				case CITY_CENTER: 
					if (currCellLabel.isSelected())
						icon = ImageLoader.getSelectedWorkerFactoryImageIcon(floor.get(new Position(i, j)).getId());
					else
						icon = ImageLoader.getWorkerFactoryImageIcon(floor.get(new Position(i, j)).getId());
					break;
				case WOOD:
					icon = ImageLoader.treeIcon;
					break;
				case FOOD:
					icon = ImageLoader.farmIcon;
					break;
				default:
					icon = ImageLoader.freeIcon;
					break;
				}
				
				if (AttacksManager.isThereAnHit(i, j))
					icon = ImageLoader.hitIcon;
				
				currCellLabel.setIcon(icon);
			}
		}
	}

	public void paintComponent(Graphics g) {
		g.drawImage(ImageLoader.getBackgroundImage(this.getSize()).getImage(), 0, 0, null);
	}
}
