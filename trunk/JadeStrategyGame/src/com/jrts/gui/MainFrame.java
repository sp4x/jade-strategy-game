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
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.jrts.O2Ainterfaces.IUnit;
import com.jrts.O2Ainterfaces.Team;
import com.jrts.common.GameConfig;
import com.jrts.environment.Cell;
import com.jrts.environment.CellType;
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
	
	protected IUnit selectedUnit = null;

	protected JPanel infoPanel;

	private JLabel labelTeam, labelType, labelPosition, labelEnergy, labelAction, labelKnapsack;
	
	private int selectedRow = 0, selectedCol = 0;
	
	//public static int treeClick = 0;
	
	public static String addTreeClick = "Add Tree";
	public static String addFoodClick = "Add Food";
	public static String deleteCellClick = "Empty Cell";
	public static String selectionClick = "Get Cell/Unit Info";
	
	public String clickType = selectionClick;
	
	List<Team> teams;
	
	JPanel topPanel;
	JPanel rightPanel;
	
	public static void start(Floor floor, List<Team> teams)
	{
		mainFrame = new MainFrame(floor, teams);
	}
	
	protected MainFrame(Floor floor, List<Team> teams) {
		super();
		
		this.teams = teams;
		
//		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		super.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				Runtime.instance().shutDown();
				//TODO killare tutti gli agenti e chiudere la runtime
				System.exit(0);
			}
		});
		
		this.worldViewPanel = new WorldViewPanel(floor);
		this.floor = floor;
		
		JSlider speed = new JSlider(JSlider.HORIZONTAL, GameConfig.MIN_REFRESH_TIME, 
				GameConfig.MAX_REFRESH_TIME, GameConfig.REFRESH_TIME);
		speed.setPaintTicks(true);
		speed.setPaintLabels(true);
		speed.setBorder(BorderFactory.createTitledBorder("Speed"));
		speed.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent ce) {
			    JSlider source = (JSlider)ce.getSource();
			    if (!source.getValueIsAdjusting())
			        GameConfig.REFRESH_TIME = (int)source.getValue();
			}
		});

		ActionListener al = new ActionListener() {
			public void actionPerformed(ActionEvent e) { MainFrame.this.clickType = e.getActionCommand(); }
		};
		JRadioButton tree = new JRadioButton(addTreeClick);
		tree.addActionListener(al);
		tree.setActionCommand(addTreeClick);
		JRadioButton food = new JRadioButton(addFoodClick);
		food.addActionListener(al);
		food.setActionCommand(addFoodClick);
		JRadioButton emptyCell = new JRadioButton(deleteCellClick);
		emptyCell.setActionCommand(deleteCellClick);
		emptyCell.addActionListener(al);
		JRadioButton selection = new JRadioButton(selectionClick, true);
		selection.setActionCommand(selectionClick);
		selection.addActionListener(al);
	    ButtonGroup group = new ButtonGroup();
	    group.add(tree);
	    group.add(food);
	    group.add(emptyCell);
	    group.add(selection);
		
	    /**********************************************************************/
	    /************** CREATING LEFT PANEL ***********************************/
		JPanel settingsPanel = new JPanel(new GridLayout(5, 1));
		settingsPanel.add(speed);
		settingsPanel.add(tree);
		settingsPanel.add(food);
		settingsPanel.add(emptyCell);
		settingsPanel.add(selection);
		
		this.infoPanel = new JPanel();
		infoPanel.setBorder(BorderFactory.createTitledBorder("Informations"));
		infoPanel.setLayout(new GridLayout(5, 2));
		infoPanel.add(new JLabel("Team:"));
		labelTeam = new JLabel("None");		
		infoPanel.add(labelTeam);
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
			
		d = new Dimension(200, 300);
		JPanel leftInformationPanel = new JPanel();
		leftInformationPanel.setPreferredSize(d);
		leftInformationPanel.setSize(d);
		leftInformationPanel.setMinimumSize(d);
		leftInformationPanel.setMaximumSize(d);
		
		leftInformationPanel.add(this.infoPanel);
		leftInformationPanel.add(settingsPanel);
		/************** END CREATING LEFT PANEL *******************************/
	    /**********************************************************************/
		
		
		topPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
		rightPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));

		d = new Dimension(200, GameConfig.WORLD_COLS*GameConfig.WORLD_VISIBILITY_MAP_FACTOR + 20);
		//JPanel rightVisibilityPanel = new JPanel();
		rightPanel.setPreferredSize(d);
		rightPanel.setSize(d);
		rightPanel.setMinimumSize(d);
		rightPanel.setMaximumSize(d);
		
		for (Team t : teams) {
			topPanel.add(new TeamResourcePanel(t), BorderLayout.WEST);
			rightPanel.add(new TeamVisibilityPanel(t), BorderLayout.WEST);
		}
		
		getContentPane().add(topPanel, BorderLayout.NORTH);
		getContentPane().add(rightPanel, BorderLayout.EAST);
		getContentPane().add(worldViewPanel, BorderLayout.CENTER);
		getContentPane().add(leftInformationPanel, BorderLayout.WEST);
		getContentPane().add(new JLabel("Jade Strategy Game - Loria Salvatore, Martire Andrea, Parisi Daniele, Pirrone Vincenzo", JLabel.CENTER), BorderLayout.SOUTH);
		
		pack();

		//setSize((floor.getCols()+1)*ImageLoader.iconSize, (floor.getRows()+4)*ImageLoader.iconSize*3/5);
		this.setVisible(true);

		class RefreshGUI implements Runnable{
			@Override
			public void run() {
				while(true) {
					try {
						Thread.sleep(10);
					} catch (InterruptedException e) {
					}
					update();
				}
			}
		}

		new Thread(new RefreshGUI()).start();
	}
	
	public void update() {
		/** update world panel */
		worldViewPanel.update();
				
		/** update all teampanels */
		for (int i = 0; i < topPanel.getComponentCount(); i++) {
			if (topPanel.getComponent(i) instanceof TeamResourcePanel)
				((TeamResourcePanel) topPanel.getComponent(i)).update();
		}
		
		/** follow the selected unit */
		if(selectedUnit != null) {
			showAgentInfo();
			setSelectedCell(selectedUnit.getPosition().getRow(), selectedUnit.getPosition().getCol());
		}
		
		repaint();
	}
	
	protected void showCellInfo(int i, int j, int energy)
	{
		Cell cell = floor.get(new Position(i, j));
		
		try{
			if(cell.getId().isEmpty() || cell.getId().trim().equals(""))
				labelTeam.setText("None");
			else
				labelTeam.setText(cell.getId());
		} catch(NullPointerException e){ 
			labelTeam.setText("None"); 
		}
		
		if(cell.getType().equals(CellType.CITY_CENTER)) 
			labelType.setText("Town Center");
		else if(cell.getType().equals(CellType.FOOD)) 
			labelType.setText("Food");
		else if(cell.getType().equals(CellType.WOOD)) 
			labelType.setText("Wood");
		else if(cell.getType().equals(CellType.FREE)) 
			labelType.setText("Ground");
		else  
			labelType.setText("Cell");
		
		labelPosition.setText(i + ", " + j);
		labelEnergy.setText(String.valueOf(energy));
		labelAction.setText("nothing");
	}
	
	protected void showAgentInfo()
	{
		//IUnit  World.getInstance().getCell(selectedUnit.getPosition()).getUnit();
		labelTeam.setText(selectedUnit.getTeamName());
		if(selectedUnit.getType().equals(CellType.WORKER))
			labelType.setText("Worker");
		else if(selectedUnit.getType().equals(CellType.SOLDIER))
			labelType.setText("Soldier");
		labelPosition.setText(selectedUnit.getPosition().getRow() +", "+ selectedUnit.getPosition().getCol());
		labelEnergy.setText("" + selectedUnit.getLife());
		labelAction.setText(selectedUnit.getStatus());
	}
	
	public static MainFrame getInstance(){
		return mainFrame;
	}

	public void setSelectedCell(int row, int col) {
		worldViewPanel.labelMatrix[row][col].setSelected(true);
		worldViewPanel.labelMatrix[selectedRow][selectedCol].setSelected(false);
		selectedRow = row;
		selectedCol = col;
	}
}