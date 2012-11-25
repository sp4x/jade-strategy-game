package com.jrts.gui;

import jade.core.Runtime;
import jade.wrapper.AgentContainer;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.HashMap;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTabbedPane;

import com.jrts.O2Ainterfaces.IUnit;
import com.jrts.O2Ainterfaces.Team;
import com.jrts.common.GameStatistics;
import com.jrts.environment.Cell;
import com.jrts.environment.CellType;
import com.jrts.environment.Floor;
import com.jrts.environment.Position;
import com.jrts.environment.World;

/**
 * Graphic interface, allow to create the environment configuration and display
 * the sequence of actions performed by the agent
 * 
 */
@SuppressWarnings("serial")
public class MainFrame extends JFrame {

	protected static MainFrame mainFrame;

	protected WorldViewPanel worldViewPanel;
	protected Floor floor;

	protected IUnit selectedUnit = null;

	protected JPanel infoPanel;
	protected JTabbedPane leftTabbedPane;
	
	private JLabel labelTeam, labelType, labelPosition, labelEnergy, labelAction, apsCounter;

	private int selectedRow = 0, selectedCol = 0;

	private boolean finished = false;

	HashMap<String, JPanel> teamResourcePanels = new HashMap<String, JPanel>();
	HashMap<String, JPanel> teamVisibilityPanels = new HashMap<String, JPanel>();
	HashMap<String, JPanel> teamInfoPanels = new HashMap<String, JPanel>();
	
	// public static int treeClick = 0;

	public static String addTreeClick = "Add Tree";
	public static String addFoodClick = "Add Food";
	public static String deleteCellClick = "Add Sand";
	public static String selectionClick = "Get Cell/Unit Info";

	public String clickType = selectionClick;

	List<Team> teams;

	JPanel topPanel;
	JPanel rightPanel;
	
	AgentContainer agentController;

	public static void start(Floor floor, List<Team> teams, AgentContainer ac) {
		mainFrame = new MainFrame(floor, teams, ac);
	}

	protected MainFrame(Floor floor, List<Team> teams, AgentContainer ac) {
		super();

		this.agentController = ac;
		
		this.teams = teams;
		
		super.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				close();
			}
		});

		this.worldViewPanel = new WorldViewPanel(floor);
		this.floor = floor;

		JPanel statisticsPanel = new JPanel();
		statisticsPanel.add(new JLabel("Actions per seconds:"));
		apsCounter = new JLabel("nothing");
		statisticsPanel.add(apsCounter);
		
		ActionListener al = new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				MainFrame.this.clickType = e.getActionCommand();
			}
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
		JPanel settingsPanel = new JPanel(new GridLayout(4, 1));
		settingsPanel.setBorder(BorderFactory.createTitledBorder("Map Cells"));
		//settingsPanel.add(speed);
		settingsPanel.add(tree);
		settingsPanel.add(food);
		settingsPanel.add(emptyCell);
		settingsPanel.add(selection);
		
		Dimension d = new Dimension(200, 150);
		settingsPanel.setPreferredSize(d);
		settingsPanel.setSize(d);
		settingsPanel.setMinimumSize(d);
		settingsPanel.setMaximumSize(d);
		
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
		
		infoPanel.setPreferredSize(d);
		infoPanel.setSize(d);
		infoPanel.setMinimumSize(d);
		infoPanel.setMaximumSize(d);

		this.leftTabbedPane = new JTabbedPane(JTabbedPane.TOP);
		JPanel panel1 = new JPanel();
		panel1.add(new JLabel("lalal"));
		for (Team t : teams) {
			TeamInfoPanel tip = new TeamInfoPanel(t);
			teamInfoPanels.put(t.getTeamName(), tip);
			leftTabbedPane.addTab(t.getTeamName(), ImageLoader.getSoldierImageIcon(t.getTeamName()), tip);
		}
		leftTabbedPane.setMnemonicAt(0, KeyEvent.VK_1);
		leftTabbedPane.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);		
		leftTabbedPane.setPreferredSize(d);
		leftTabbedPane.setSize(d);
		leftTabbedPane.setMinimumSize(d);
		leftTabbedPane.setMaximumSize(d);
		
		d = new Dimension(200, 300);
		JPanel leftPanel = new JPanel();
		leftPanel.setPreferredSize(d);
		leftPanel.setSize(d);
		leftPanel.setMinimumSize(d);
		leftPanel.setMaximumSize(d);

		leftPanel.add(statisticsPanel);
		leftPanel.add(this.infoPanel);
		leftPanel.add(settingsPanel);
		leftPanel.add(leftTabbedPane);
		/************** END CREATING LEFT PANEL *******************************/
		/**********************************************************************/

		topPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
		rightPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));

		d = new Dimension(200, 500);
		// JPanel rightVisibilityPanel = new JPanel();
		rightPanel.setPreferredSize(d);
		rightPanel.setSize(d);
		rightPanel.setMinimumSize(d);
		rightPanel.setMaximumSize(d);

		for (Team t : teams) {
			TeamResourcePanel trp = new TeamResourcePanel(t);
			teamResourcePanels.put(t.getTeamName(), trp);
			topPanel.add(trp, BorderLayout.WEST);
			
			TeamVisibilityPanel tvp = new TeamVisibilityPanel(t);
			teamVisibilityPanels.put(t.getTeamName(), tvp);
			rightPanel.add(tvp, BorderLayout.WEST);
		}

		JPanel center = new JPanel();
		center.setLayout(new BorderLayout());
		JPanel p = new JPanel();
		// p.setBorder(BorderFactory.createLineBorder(Color.black, 1));
		p.setSize(100, 40);
		center.add(p, BorderLayout.SOUTH);
		center.add(worldViewPanel, BorderLayout.CENTER);

		getContentPane().add(topPanel, BorderLayout.NORTH);
		getContentPane().add(rightPanel, BorderLayout.EAST);
		getContentPane().add(center, BorderLayout.CENTER);
		// getContentPane().add(worldViewPanel, BorderLayout.CENTER);
		getContentPane().add(leftPanel, BorderLayout.WEST);

		JPanel footer = new JPanel();
		d = new Dimension(500, 30);
		// JPanel rightVisibilityPanel = new JPanel();
		footer.setPreferredSize(d);
		footer.add(new JLabel("Jade Strategy Game - Loria Salvatore, Martire Andrea, Parisi Daniele, Pirrone Vincenzo", JLabel.CENTER));
		getContentPane().add(footer, BorderLayout.SOUTH);

		pack();

		setResizable(false);

		// setSize((floor.getCols()+1)*ImageLoader.iconSize,
		// (floor.getRows()+4)*ImageLoader.iconSize*3/5);
		this.setVisible(true);

		class RefreshGUI implements Runnable {
			@Override
			public void run() {
				while (!finished) {
//					try {
//						Thread.sleep(10);
//					} catch (InterruptedException e) {
//					}
					update();
				}
			}
		}
		new Thread(new RefreshGUI()).start();
	}

	private void close() {
		finished = true;
		dispose();
		try {
			for (Team t : teams) {
				t.decease();
			}
			agentController.kill();
			Runtime.instance().shutDown();
		} catch (Exception e1) {
		} 
		System.exit(0);
	}

	public void update() {
		apsCounter.setText(GameStatistics.getFrameRate() + "");
		/** update all teampanels */
		for (int i = 0; i < topPanel.getComponentCount(); i++) {
			if (topPanel.getComponent(i) instanceof TeamResourcePanel)
				((TeamResourcePanel) topPanel.getComponent(i)).update();
		}
		
		for (int i = 0; i < leftTabbedPane.getComponentCount(); i++) {
			if (leftTabbedPane.getComponent(i) instanceof TeamInfoPanel)
				((TeamInfoPanel) leftTabbedPane.getComponent(i)).update();
		}
		
		/** follow the selected unit */
		if (selectedUnit != null) {
			try {
				showAgentInfo();
				setSelectedCell(selectedUnit.getPosition().getRow(), selectedUnit.getPosition().getCol());
			} catch (Exception e) {
				setSelectedCell(0,0);
				selectedUnit = null;
			}
		} else {
			showCellInfo(selectedRow, selectedCol);
		}

		/** update world panel */
		worldViewPanel.update();
		
		repaint();
	}

	protected void showCellInfo(int i, int j) {
		Cell cell = World.getInstance().getCell(new Position(i, j));

		if (cell.getUnit() == null) {
			try {
				if (cell.getId().isEmpty() || cell.getId().trim().equals(""))
					labelTeam.setText("None");
				else
					labelTeam.setText(cell.getId());
			} catch (NullPointerException e) {
				labelTeam.setText("None");
			}
	
			labelType.setText(cell.getType().toString());
			labelPosition.setText(i + ", " + j);
			labelEnergy.setText(String.valueOf(cell.getEnergy()));
			labelAction.setText("nothing");
		}
	}

	protected void showAgentInfo() {
		// World.getInstance().getCell(selectedUnit.getPosition()).getUnit();
		labelTeam.setText(selectedUnit.getTeamName());
		if (selectedUnit.getType().equals(CellType.WORKER))
			labelType.setText("Worker");
		else if (selectedUnit.getType().equals(CellType.SOLDIER))
			labelType.setText("Soldier");
		labelPosition.setText(selectedUnit.getPosition().getRow() + ", " + selectedUnit.getPosition().getCol());
		labelEnergy.setText("" + selectedUnit.getLife());
		labelAction.setText(selectedUnit.getStatus());
	}

	public static MainFrame getInstance() {
		return mainFrame;
	}

	public void setSelectedCell(int row, int col) {
		worldViewPanel.labelMatrix[row][col].setSelected(true);
		if (selectedRow != row || selectedCol != col) {
			worldViewPanel.labelMatrix[selectedRow][selectedCol].setSelected(false);
			selectedRow = row;
			selectedCol = col;
		}
	}

	public synchronized void removeTeam(String teamName) {
		int toRemove = -1;
		for (int i = 0; i < teams.size(); i++) {
			if (teams.get(i).getTeamName().equals(teamName)) {
				toRemove = i;
			}
		}
		if (toRemove != -1)
			teams.remove(toRemove);
		
		rightPanel.remove(teamVisibilityPanels.get(teamName));
		topPanel.remove(teamResourcePanels.get(teamName));
		leftTabbedPane.remove(teamInfoPanels.get(teamName));
		
		repaint();
	}
}