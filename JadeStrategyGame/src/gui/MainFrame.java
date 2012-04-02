package gui;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.WindowConstants;

import agents.WorldManager;


/**
 * Graphic interface, allow to create the environment configuration 
 * and display the sequence of actions performed by the agent
 *
 */
public class MainFrame extends javax.swing.JFrame {

	private static final long serialVersionUID = 1L;
	
	public GridPanel gridPanel;
//	public SettingsPanel settingsPanel;
	
	public MainFrame(WorldManager wm) {
		super();
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		getContentPane().setLayout(new BorderLayout());
//		setResizable(false);
		
//		settingsPanel = new SettingsPanel(this);
//		getContentPane().add(settingsPanel, BorderLayout.WEST);
//		
		gridPanel = new GridPanel(wm);
		getContentPane().add(gridPanel, BorderLayout.EAST);
		
		pack();
		this.setVisible(true);
	}
	
	public void update(){
		gridPanel.update();
	}
}
