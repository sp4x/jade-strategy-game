package gui;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridLayout;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
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
				class InfoListener implements MouseListener{
					
					int i, j;
					
					public InfoListener(int i, int j) {
						super();
						this.i = i;
						this.j = j;
					}

					@Override
					public void mouseClicked(MouseEvent e) {
						JOptionPane.showMessageDialog(null, i + "," + j);
					}

					@Override
					public void mouseEntered(MouseEvent e) {}
					@Override
					public void mouseExited(MouseEvent e) {}
					@Override
					public void mousePressed(MouseEvent e) {}
					@Override
					public void mouseReleased(MouseEvent e) {}
				}
				label.addMouseListener(new InfoListener(i, j));
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
				if(wm.getFloor().get(i,j) == Cell.WOOD)
					labelMatrix[i][j].setIcon(ImageLoader.treeIcon);
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
