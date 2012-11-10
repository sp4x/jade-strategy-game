package com.jrts.gui;

import java.awt.Color;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.border.Border;

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
			public void mouseClicked(MouseEvent e) {
				Cell cell = World.getInstance().getCell(new Position(i, j));
				if(MainFrame.getInstance().clickType.equals(MainFrame.selectionClick))
				{
					if(cell.getType() == CellType.WORKER || cell.getType() == CellType.SOLDIER) {
						MainFrame.getInstance().selectedUnit = cell.getUnit();
					} else {
						MainFrame.getInstance().selectedUnit = null;
						MainFrame.getInstance().showCellInfo(i, j, cell.getResourceEnergy());
						MainFrame.getInstance().setSelectedCell(i,j);
					}
				} else {
					if(cell.getType() != CellType.WORKER && cell.getType() != CellType.SOLDIER 
							&& cell.getType() != CellType.CITY_CENTER)
					{
//						if(MainFrame.getInstance().clickType.equals(MainFrame.addTreeClick))
//							World.getInstance().getFloor().set(CellLabel.this.i, CellLabel.this.j, new Cell(CellType.WOOD));
//						else if(MainFrame.getInstance().clickType.equals(MainFrame.addFoodClick))
//							World.getInstance().getFloor().set(CellLabel.this.i, CellLabel.this.j, new Cell(CellType.FOOD));
//						else if(MainFrame.getInstance().clickType.equals(MainFrame.deleteCellClick))
//							World.getInstance().getFloor().set(CellLabel.this.i, CellLabel.this.j, new Cell(CellType.FREE));
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
