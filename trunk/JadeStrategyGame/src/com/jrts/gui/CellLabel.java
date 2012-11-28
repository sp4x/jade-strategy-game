package com.jrts.gui;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.border.Border;

import com.jrts.common.GameConfig;
import com.jrts.environment.Cell;
import com.jrts.environment.CellType;
import com.jrts.environment.Position;
import com.jrts.environment.World;

public class CellLabel extends JLabel {
	private static final long serialVersionUID = 1L;
	int i;
	int j;
	boolean selected;
	Border selectedBorder = BorderFactory.createLineBorder(Color.green, 2);
	
	public CellLabel(int row, int col) {
		this.i = row;
		this.j = col;
		
		super.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				e.getComponent().setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
				super.mouseEntered(e);
			}
			
			@Override
			public void mouseExited(MouseEvent e) {
				e.getComponent().setCursor(Cursor.getDefaultCursor());
				super.mouseExited(e);
			}
			
			@Override
			public void mouseClicked(MouseEvent e) {
				if (e.getButton() != MouseEvent.BUTTON1)
					return;
				
				Cell cell = World.getInstance().getCellClone(new Position(i, j));
				if(MainFrame.getInstance().clickType.equals(MainFrame.selectionClick))
				{
					if(cell.getType() == CellType.WORKER || cell.getType() == CellType.SOLDIER) {
						MainFrame.getInstance().selectedUnit = cell.getUnit();
					} else {
						MainFrame.getInstance().selectedUnit = null;
						MainFrame.getInstance().setSelectedCell(i,j);
					}
				} else {
					if(cell.getType() != CellType.WORKER && cell.getType() != CellType.SOLDIER 
							&& cell.getType() != CellType.CITY_CENTER)
					{
						if(MainFrame.getInstance().clickType.equals(MainFrame.addTreeClick))
							World.getInstance().changeCell(CellLabel.this.i, CellLabel.this.j, new Cell(CellType.WOOD, GameConfig.TREE_ENERGY));
						else if(MainFrame.getInstance().clickType.equals(MainFrame.addFoodClick))
							World.getInstance().changeCell(CellLabel.this.i, CellLabel.this.j, new Cell(CellType.FOOD, GameConfig.FARM_ENERGY));
						else if(MainFrame.getInstance().clickType.equals(MainFrame.deleteCellClick))
							World.getInstance().changeCell(CellLabel.this.i, CellLabel.this.j, new Cell(CellType.FREE));
					}
					// JUST DEBUG
					if (cell.getType() == CellType.CITY_CENTER) {
						if(MainFrame.getInstance().clickType.equals(MainFrame.deleteCellClick)) {
							MainFrame.getInstance().removeTeam(cell.getId());
							World.getInstance().takeEnergy(new Position(i, j), 1000);
						}
					}
				}
			}
		});
	}
	
	
	public int getI() {
		return i;
	}


	public void setI(int i) {
		this.i = i;
	}


	public int getJ() {
		return j;
	}


	public void setJ(int j) {
		this.j = j;
	}

	public boolean isSelected() {
		return selected;
	}
	
	public void setSelected(boolean selected) {
		this.selected = selected;
//		if (selected) {
//			setBorder(selectedBorder);
//		} else {
//			setBorder(null);
//		}
	}
}
