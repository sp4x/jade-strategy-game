package gui;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridLayout;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

import logic.Cell;

import agents.WorldManager;

/**
 * Implement a JPanel to represent the environment and the agent
 *
 */
public class GridPanel extends JPanel {

	private WorldManager wm;

	public GridPanel(WorldManager wm) {
		this.wm = wm;
		init();
		update();
	}

	private JLabel[][] labelMatrix;

	/**
	 * Generate a GridLayout according with the width and length of the floor
	 * and set the images that correspond to each element of the environment
	 */
	void init() {

		setLayout(new FlowLayout());

		JPanel flowPanel = new JPanel();
		add(flowPanel);
		flowPanel.setLayout(new GridLayout(wm.getFloor().getRows(), wm.getFloor().getCols()));

		labelMatrix = new JLabel[wm.getFloor().getRows()][wm.getFloor().getCols()];

		for(int i=0; i<wm.getFloor().getRows(); i++)
			for(int j=0; j<wm.getFloor().getCols(); j++){
				GridBagConstraints constraints = new GridBagConstraints();
				constraints.fill = GridBagConstraints.BOTH;
				constraints.gridx = i;
				constraints.gridy = j;
				final JLabel label = new JLabel();
				label.setPreferredSize(new Dimension(ImageLoader.iconSize,ImageLoader.iconSize));
				//				label.addMouseListener(new ClickHandler(label,i,j));
				labelMatrix[i][j] = label;
				flowPanel.add(label, constraints);
			}
	}

	/**
	 * Generate a graphic representation of the environment 
	 * on a thread, according with the state of each tile (square)
	 * and the position of the agent
	 */
	public void update() {
		for(int i=0; i<wm.getFloor().getRows(); i++)
			for(int j=0; j<wm.getFloor().getCols(); j++){
				if(wm.getFloor().get(i,j) == Cell.FREE)
					labelMatrix[i][j].setIcon(ImageLoader.freeIcon);
				if(wm.getFloor().get(i,j) == Cell.UNIT)
					labelMatrix[i][j].setIcon(ImageLoader.workerIcon);
			}
		try {
			Thread.sleep(250);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
