package com.jrts.gui;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.WindowConstants;

import com.common.GameConfig;
import com.jrts.environment.Floor;
import com.jrts.environment.Position;


/**
 * Graphic interface, allow to create the environment configuration 
 * and display the sequence of actions performed by the agent
 *
 */
@SuppressWarnings("serial")
public class MainFrame extends JFrame {

	protected static MainFrame mainFrame;	
	
	protected WorldViewPanel worldViewPanel;
	protected Floor floor;
	
	protected String clickedAgentId = null;

	protected JPanel infoPanel;
	
	public static void start(Floor floor)
	{
		mainFrame = new MainFrame(floor);
	}
	
	protected MainFrame(Floor floor) {
		super();
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		super.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosed(WindowEvent e) {
				super.windowClosed(e);
				dispose();
				//TODO: Chiudere tutto
				
			}
		});
		
		this.worldViewPanel = new WorldViewPanel(floor);
		this.floor = floor;
		
		JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
		
		topPanel.add(new TeamPanel("Team1"), BorderLayout.WEST);
		topPanel.add(new TeamPanel("Team2"), BorderLayout.EAST);

		JPanel settingsPanel = new JPanel();
		JSlider speed = new JSlider(JSlider.HORIZONTAL,
		                                      GameConfig.MIN_REFRESH_TIME, GameConfig.MAX_REFRESH_TIME, GameConfig.DEFAULT_REFRESH_TIME);
		speed.setBorder(BorderFactory.createTitledBorder("Velocità"));
		settingsPanel.add(speed);

		this.infoPanel = new JPanel();
		this.infoPanel.add(new JLabel("INFOOO"));
			
		JPanel leftPanel = new JPanel(new GridLayout(2, 1));
		leftPanel.add(this.infoPanel);
		leftPanel.add(settingsPanel);
		
		getContentPane().add(topPanel, BorderLayout.NORTH);
		getContentPane().add(leftPanel, BorderLayout.WEST);
		
		getContentPane().add(worldViewPanel, BorderLayout.CENTER);
		
		pack();

		//setSize((floor.getCols()+1)*ImageLoader.iconSize, (floor.getRows()+4)*ImageLoader.iconSize*3/5);
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
		
		if(this.clickedAgentId != null) this.showAgentInfo();
	}
	
	protected void showCellInfo(int i, int j, int energy)
	{
		infoPanel.removeAll();
		infoPanel.setBorder(BorderFactory.createTitledBorder("Informazioni"));
		infoPanel.setLayout(new GridLayout(3, 2));
		infoPanel.add(new JLabel("Tipo:"));
		infoPanel.add(new JLabel("Tipo Cella"));
		infoPanel.add(new JLabel("Posizione:"));
		infoPanel.add(new JLabel(String.valueOf(i) + " " + String.valueOf(j)));
		infoPanel.add(new JLabel("Energia:"));
		infoPanel.add(new JLabel(String.valueOf(energy)));
		
		super.validate();
		super.repaint();
	}
	
	protected void showAgentInfo()
	{
		infoPanel.removeAll();
		infoPanel.setBorder(BorderFactory.createTitledBorder("Informazioni"));
		infoPanel.setLayout(new GridLayout(3, 2));
		infoPanel.add(new JLabel("Tipo:"));
		infoPanel.add(new JLabel("Cittadino"));
		infoPanel.add(new JLabel("Posizione:"));
		infoPanel.add(new JLabel("10 10"));
		infoPanel.add(new JLabel("Energia:"));
		infoPanel.add(new JLabel(String.valueOf(500)));
		infoPanel.add(new JLabel("Azione:"));
		infoPanel.add(new JLabel("Zappa"));
		
		super.validate();
		super.repaint();
	}
	
	public static MainFrame getInstance(){
		return mainFrame;
	}
}