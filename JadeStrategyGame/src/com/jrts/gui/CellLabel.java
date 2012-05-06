package com.jrts.gui;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JLabel;
import javax.swing.JOptionPane;

import com.jrts.environment.Cell;
import com.jrts.environment.CellType;
import com.jrts.environment.World;

public class CellLabel extends JLabel {
	private static final long serialVersionUID = 1L;
	Cell cell;
	int i;
	int j;
	
	public CellLabel(Cell cell, int i, int j) {
		this.cell = cell;
		this.i = i;
		this.j = j;
		
		super.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				
				if(MainFrame.getInstance().clickType.equals(MainFrame.selectionClick))
				{
					if(CellLabel.this.cell.getType() != CellType.UNIT) {
						MainFrame.getInstance().clickedAgentId = null;
						MainFrame.getInstance().showCellInfo(CellLabel.this.i, CellLabel.this.j, CellLabel.this.cell.getResourceEnergy());
					} else {
						MainFrame.getInstance().clickedAgentId = CellLabel.this.cell.getId(); 
					}
				} else {
					if(CellLabel.this.cell.getType() != CellType.UNIT)
					{
						JOptionPane.showMessageDialog(MainFrame.getInstance(), "Tipo Cell:" + CellLabel.this.cell.toString() + " Posizione: " + CellLabel.this.i + " - " + CellLabel.this.j);
						
						if(MainFrame.getInstance().clickType.equals(MainFrame.treeClick))
							World.getInstance().getFloor().set(CellLabel.this.i, CellLabel.this.j, new Cell(CellType.WOOD));
						else if(MainFrame.getInstance().clickType.equals(MainFrame.foodClick))
							World.getInstance().getFloor().set(CellLabel.this.i, CellLabel.this.j, new Cell(CellType.FOOD));
						else if(MainFrame.getInstance().clickType.equals(MainFrame.emptyCellClick))
							World.getInstance().getFloor().set(CellLabel.this.i, CellLabel.this.j, new Cell(CellType.FREE));
					}
				}
			}
		});
	}
	
}
