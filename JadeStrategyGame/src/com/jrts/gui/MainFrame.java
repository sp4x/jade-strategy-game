package com.jrts.gui;
import jade.core.Runtime;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSlider;
import javax.swing.WindowConstants;

import com.jrts.common.GameConfig;
import com.jrts.environment.Floor;


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

	private JLabel labelType, labelPosition, labelEnergy, labelAction;
	
	//public static int treeClick = 0;
	
	public static String treeClick = "Add Tree";
	public static String foodClick = "Add Food";
	public static String emptyCellClick = "Add Empty Cell";
	public static String selectionClick = "Get Cell/Unit Info";
	
	public String clickType = selectionClick;
	
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
				Runtime.instance().shutDown();
				dispose();
				System.exit(0);
				//TODO: Chiudere tutto
			}
		});
		
		this.worldViewPanel = new WorldViewPanel(floor);
		this.floor = floor;
		
		JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
		
		topPanel.add(new TeamPanel("Team1"), BorderLayout.WEST);
		topPanel.add(new TeamPanel("Team2"), BorderLayout.EAST);
			    
		JSlider speed = new JSlider(JSlider.HORIZONTAL, GameConfig.MIN_REFRESH_TIME, 
				GameConfig.MAX_REFRESH_TIME, GameConfig.DEFAULT_REFRESH_TIME);
		speed.setBorder(BorderFactory.createTitledBorder("Speed"));
		
		ActionListener al = new ActionListener() {
			public void actionPerformed(ActionEvent e) { MainFrame.this.clickType = e.getActionCommand(); }
		};
		JRadioButton tree = new JRadioButton(treeClick);
		tree.addActionListener(al);
		tree.setActionCommand(treeClick);
		JRadioButton food = new JRadioButton(foodClick);
		food.addActionListener(al);
		food.setActionCommand(foodClick);
		JRadioButton emptyCell = new JRadioButton(emptyCellClick);
		emptyCell.setActionCommand(emptyCellClick);
		emptyCell.addActionListener(al);
		JRadioButton selection = new JRadioButton(selectionClick, true);
		selection.setActionCommand(selectionClick);
		selection.addActionListener(al);
	    ButtonGroup group = new ButtonGroup();
	    group.add(tree);
	    group.add(food);
	    group.add(emptyCell);
	    group.add(selection);
		
		JPanel settingsPanel = new JPanel(new GridLayout(5, 1));
		settingsPanel.add(speed);
		settingsPanel.add(tree);
		settingsPanel.add(food);
		settingsPanel.add(emptyCell);
		settingsPanel.add(selection);
		
		this.infoPanel = new JPanel();
		infoPanel.setBorder(BorderFactory.createTitledBorder("Informations"));
		infoPanel.setLayout(new GridLayout(4, 2));
		infoPanel.add(new JLabel("Type:"));
		labelType = new JLabel("cell");
		infoPanel.add(labelType);
		infoPanel.add(new JLabel("Position:"));
		labelPosition = new JLabel("10, 10");
		infoPanel.add(labelPosition);
		infoPanel.add(new JLabel("Energy:"));
		labelEnergy = new JLabel(String.valueOf(500));
		infoPanel.add(labelEnergy);
		infoPanel.add(new JLabel("Action:"));
		labelAction = new JLabel("nothing");
		infoPanel.add(labelAction);
		Dimension d = new Dimension(200, 150);
		infoPanel.setPreferredSize(d);
		infoPanel.setSize(d);
		infoPanel.setMinimumSize(d);
		infoPanel.setMaximumSize(d);
			
		JPanel leftPanel = new JPanel();
		leftPanel.setPreferredSize(d);
		leftPanel.setSize(d);
		leftPanel.setMinimumSize(d);
		leftPanel.setMaximumSize(d);
		
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
		
		if(this.clickedAgentId != null) this.showAgentInfo(this.clickedAgentId);
	}
	
	protected void showCellInfo(int i, int j, int energy)
	{
		labelType.setText("cell");
		labelPosition.setText(i + ", " + j);
		labelEnergy.setText(String.valueOf(energy));
		labelAction.setText("nothing");
	}
	
	protected void showAgentInfo(String clickedAgentId)
	{
		labelType.setText("Worker");
		labelPosition.setText("10, 10");
		labelEnergy.setText("100");
		labelAction.setText("work");
	}
	
	public static MainFrame getInstance(){
		return mainFrame;
	}
}