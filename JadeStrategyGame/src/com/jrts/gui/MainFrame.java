package com.jrts.gui;
import javax.swing.JFrame;
import javax.swing.WindowConstants;

import com.jrts.environment.Floor;


/**
 * Graphic interface, allow to create the environment configuration 
 * and display the sequence of actions performed by the agent
 *
 */
@SuppressWarnings("serial")
public class MainFrame extends JFrame {


	public WorldViewPanel worldViewPanel;

	public MainFrame(Floor floor) {
		super();
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		
		worldViewPanel = new WorldViewPanel(floor);
		add(worldViewPanel);

		setSize((floor.getCols()+1)*ImageLoader.iconSize, (floor.getRows()+4)*ImageLoader.iconSize*3/5);
		this.setVisible(true);

		class RefreshGUI implements Runnable{
			@Override
			public void run() {
				while(true){
//					System.out.println("Update GUI");
					MainFrame.this.update();
				}
			}
		}

		new Thread(new RefreshGUI()).start();
	}

	public void update(){
		worldViewPanel.update();
	}
}
