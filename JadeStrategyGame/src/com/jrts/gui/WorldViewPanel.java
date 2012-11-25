package com.jrts.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.JPanel;

import com.jrts.common.GameConfig;
import com.jrts.environment.Cell;
import com.jrts.environment.Floor;
import com.jrts.environment.Position;
import com.jrts.environment.World;
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
		for (int j = 0; j < floor.getRows(); j++)
			for (int i = 0; i < floor.getCols(); i++) {
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
		floor = World.getInstance().getSnapshot();
		Icon icon;
		for (int j = 0; j < floor.getRows(); j++) {
			for (int i = 0; i < floor.getCols(); i++) {
				CellLabel currCellLabel = labelMatrix[i][j];
				currCellLabel.setBounds(j * GameConfig.ICON_SIZE, i * GameConfig.ICON_SIZE, GameConfig.ICON_SIZE, GameConfig.ICON_SIZE);
				icon = null;
				Cell currCell = floor.get(i, j);
				try {
					switch (currCell.getType()) {
					case WORKER:
						if (currCell.getUnit() == MainFrame.getInstance().selectedUnit)
							icon = ImageLoader.getSelectedWorkerImageIcon(currCell.getUnit().getTeamName());
						else
							icon = ImageLoader.getWorkerImageIcon(currCell.getUnit().getTeamName());
						break;
					case SOLDIER:
						if (currCell.getUnit() == MainFrame.getInstance().selectedUnit)
							icon = ImageLoader.getSelectedSoldierImageIcon(currCell.getUnit().getTeamName());
						else
							icon = ImageLoader.getSoldierImageIcon(currCell.getUnit().getTeamName());
						break;
					case EXPLOSION:
						icon = ImageLoader.explosionIcon;
						
						new DeleteExplosionThread(new Position(i, j));
						//if(Utils.random.nextInt(5) == 0)
							//World.getInstance().clear(new Position(i, j));
						
						
						
						//currCell.setType(CellType.FREE);
						break;
					case CITY_CENTER: 
						if (currCellLabel.isSelected())
							icon = ImageLoader.getSelectedWorkerFactoryImageIcon(currCell.getId());
						else
							icon = ImageLoader.getWorkerFactoryImageIcon(currCell.getId());
						break;
					case WOOD:
						if (currCellLabel.isSelected())
							icon = ImageLoader.treeIcon_s;
						else
							icon = ImageLoader.treeIcon;
						break;
					case FOOD:
						if (currCellLabel.isSelected())
							icon = ImageLoader.farmIcon_s;
						else
							icon = ImageLoader.farmIcon;
						break;
					default:
						if (currCellLabel.isSelected())
							icon = ImageLoader.freeIcon_s;
						else
							icon = ImageLoader.freeIcon;
						break;
					}
				} catch (Exception e) {
//					System.out.println("WORLDVIEWPANEL: NON RIESCO A DISEGNARE LA CELLA " + i + "," + j);
				}
				
				/*
				if (AttacksManager.isThereAnHit(i, j))
					icon = ImageLoader.hitIcon;
				*/
				
				currCellLabel.setIcon(icon);
			}
		}
	}

	public void paintComponent(Graphics g) {
		g.drawImage(ImageLoader.getBackgroundImage(this.getSize()).getImage(), 0, 0, null);
	}
}
