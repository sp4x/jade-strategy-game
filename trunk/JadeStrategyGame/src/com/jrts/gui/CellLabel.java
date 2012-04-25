package com.jrts.gui;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JLabel;
import javax.swing.JOptionPane;

import com.jrts.environment.Cell;

public class CellLabel extends JLabel {

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
				if(CellLabel.this.cell.getId() == null)
					MainFrame.getInstance().showCellInfo(CellLabel.this.i, CellLabel.this.j, CellLabel.this.cell.getEnergy());
				else 
					MainFrame.getInstance().clickedAgentId = CellLabel.this.cell.getId(); 
			}
		});
	}
	
}
