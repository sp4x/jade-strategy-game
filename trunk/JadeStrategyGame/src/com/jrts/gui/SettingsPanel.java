package com.jrts.gui;
//package gui;
//
//import java.awt.BorderLayout;
//import java.awt.Color;
//import java.awt.Dimension;
//import java.awt.FlowLayout;
//import java.awt.GridBagConstraints;
//import java.awt.GridBagLayout;
//import java.awt.GridLayout;
//import java.awt.Insets;
//import java.awt.event.ActionEvent;
//import java.awt.event.ActionListener;
//import java.awt.event.ItemEvent;
//import java.awt.event.ItemListener;
//import java.lang.reflect.Constructor;
//import java.util.Vector;
//
//import javax.swing.AbstractButton;
//import javax.swing.BorderFactory;
//import javax.swing.ImageIcon;
//import javax.swing.JButton;
//import javax.swing.JComboBox;
//import javax.swing.JLabel;
//import javax.swing.JOptionPane;
//import javax.swing.JPanel;
//import javax.swing.JSpinner;
//import javax.swing.JTextField;
//import javax.swing.border.Border;
//import javax.swing.border.CompoundBorder;
//import javax.swing.border.EmptyBorder;
//import javax.swing.border.TitledBorder;
//import javax.swing.event.ChangeEvent;
//import javax.swing.event.ChangeListener;
//import javax.swing.event.DocumentEvent;
//import javax.swing.event.DocumentListener;
//import javax.swing.plaf.ColorUIResource;
//
//import vacuumCleaner.AbstractAgent.VisibilityType;
//import vacuumCleaner.AbstractAgent;
//import vacuumCleaner.Environment;
//import vacuumCleaner.Square;
//import vacuumCleaner.Environment.Type;
//import vacuumCleaner.Floor;
//
//public class SettingsPanel extends JPanel {
//
//	private static final long serialVersionUID = 1L;
//
//	public MainJFrame mainFrame;
//
//	private JPanel dimensionPanel;
//	private JSpinner sizeField;
//	private JLabel sizeLabel;
//
//	private JPanel generationPanel;
//	private JSpinner dirtField;
//	private JLabel dirtLabel;
//	private JSpinner obstaclesField;
//	private JLabel obstaclesLabel;
//
//	private JPanel agentPanel;
//	private JSpinner agentEnergyField;
//	private JLabel agentEnergylabel;
//	private JLabel agentVisibilityLabel;
//	private JComboBox agentVisibilityCombobox;
//
//	private JPanel commandPanel;
//	private JButton refreshButton;
//	JButton controlButton;
//
//	private JPanel statusPanel;
//	private JLabel pmLabel;
//	private JLabel pmValueLabel;
//	private JLabel stepsLabel;
//	private JLabel stepsValueLabel;
//	private JLabel currEnergyLabel;
//	private JLabel currEnergyValueLabel;
//
//	private int max_dim = 12;
//	private int min_dim = 2;
//
//	private JLabel envTypeLabel;
//	private JComboBox envTypeCombobox;
//
//	ImageIcon playIcon, stopIcon, nextIcon;
//
//	private JButton onesButton;
//	private JLabel currVisibilityLabel;
//	private JLabel currVisibilityValueLabel;
//	private JLabel currTypeEnvLabel;
//	private JLabel currTypeEnvValueLabel;
//
//	private JButton resetButton;
//
//	private JLabel goalReachedLabel;
//
//	/**
//	 * 
//	 * @param mainFrame
//	 */
//	public SettingsPanel(final MainJFrame mainFrame) {
//		{
//			this.mainFrame = mainFrame;
//
//			playIcon = ImageLoader.playIcon;
//			stopIcon = ImageLoader.stopIcon;
//			nextIcon = ImageLoader.nextIcon;
//
//			GridBagLayout jPanel2Layout = new GridBagLayout();
//			jPanel2Layout.rowWeights = new double[] {0.1, 0.1, 0.1, 0.1};
//			jPanel2Layout.rowHeights = new int[] {1,1,1,1};
//			jPanel2Layout.columnWeights = new double[] {0.1};
//			jPanel2Layout.columnWidths = new int[] {1};
//
//			setLayout(jPanel2Layout);
//			{
//				dimensionPanel = new JPanel();
//				dimensionPanel.setPreferredSize(new Dimension(300,100));
//				Border marginOutside = new EmptyBorder(10,10,10,10);        
//				TitledBorder title = BorderFactory.createTitledBorder("Size Settings");
//				CompoundBorder upperBorder = new CompoundBorder(marginOutside, title);
//				Border marginInside = new EmptyBorder(10,10,10,10);
//				dimensionPanel.setBorder(new CompoundBorder(upperBorder, marginInside));
//
//				/*input field to set the size of the floor*/
//				add(dimensionPanel, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
//				sizeLabel = new JLabel();
//				dimensionPanel.add(sizeLabel);
//				sizeLabel.setText("Size");
//				sizeField = new JSpinner();
//				dimensionPanel.add(sizeField);
//				sizeField.setValue(mainFrame.env.floor.length);
//				sizeField.setPreferredSize(new Dimension(40, 30));
//				sizeField.addChangeListener(new ChangeListener() {
//
//					@Override
//					public void stateChanged(ChangeEvent arg0) {
//						int size = (Integer) sizeField.getValue();
//						if(size < min_dim ){
//							size = min_dim;
//							sizeField.setValue(min_dim);
//							JOptionPane.showMessageDialog(null,"Minimun allowed size is " + min_dim, "Warning", JOptionPane.WARNING_MESSAGE);	
//						}
//						if(size > max_dim ){
//							size = max_dim;
//							sizeField.setValue(max_dim);
//							JOptionPane.showMessageDialog(null,"Maximun allowed size is " + max_dim, "Warning", JOptionPane.WARNING_MESSAGE);
//						}
//						mainFrame.env.floor = new Floor(size, size, Square.Type.CLEAN);
//						mainFrame.env.floor.initialDirt = 0;
//						mainFrame.getContentPane().remove(mainFrame.gridPanel);
//						mainFrame.gridPanel = new GridPanel(mainFrame.env);
//						mainFrame.getContentPane().add(mainFrame.gridPanel, BorderLayout.EAST);
//						mainFrame.pack();
//					}
//				});
//
//				envTypeLabel = new JLabel("Type");
//
//				Vector<Environment.Type> envTypeVector = new Vector<Environment.Type>();
//				envTypeVector.add(Environment.Type.DYNAMIC);
//				envTypeVector.add(Environment.Type.STATIC);
//				envTypeCombobox = new JComboBox(envTypeVector);
//				envTypeCombobox.setSelectedItem(mainFrame.env.type);
//				envTypeCombobox.addItemListener(new ItemListener() {
//
//					@Override
//					public void itemStateChanged(ItemEvent arg0) {
//						mainFrame.env.type = (Type) envTypeCombobox.getSelectedItem();
//						update();
//					}
//				});
//
//				dimensionPanel.add(envTypeLabel);
//				dimensionPanel.add(envTypeCombobox);
//			}
//			{
//				/*setting input fields*/
//				generationPanel = new JPanel();
//				generationPanel.setPreferredSize(new Dimension(380,110));
//				Border marginOutside = new EmptyBorder(10,10,10,10);        
//				TitledBorder title = BorderFactory.createTitledBorder("Build Settings");
//				CompoundBorder upperBorder = new CompoundBorder(marginOutside, title);
//				Border marginInside = new EmptyBorder(10,10,10,10);
//				generationPanel.setBorder(new CompoundBorder(upperBorder, marginInside));
//
//				add(generationPanel, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
//				{
//					/*number of obstacles*/
//					obstaclesLabel = new JLabel();
//					obstaclesLabel.setText("Obstacles");
//					obstaclesField = new JSpinner();
//					obstaclesField.setValue(0);
//					obstaclesField.setPreferredSize(new Dimension(40, 30));
//					obstaclesField.addChangeListener(new ChangeListener() {
//
//						@Override
//						public void stateChanged(ChangeEvent arg0) {
//							refreshButton.setBackground(Color.YELLOW);
//						}
//					});
//
//					/*number of dirty tiles*/
//					dirtLabel = new JLabel();
//					dirtLabel.setText("Dirt");
//					dirtField = new JSpinner();
//					dirtField.setValue(0);
//					dirtField.setPreferredSize(new Dimension(40, 30));
//					dirtField.addChangeListener(new ChangeListener() {
//
//						@Override
//						public void stateChanged(ChangeEvent arg0) {
//							refreshButton.setBackground(Color.YELLOW);
//						}
//					});
//
//					generationPanel.add(obstaclesLabel);
//					generationPanel.add(obstaclesField);
//					generationPanel.add(dirtLabel);
//					generationPanel.add(dirtField);
//					/*Refresh current configuration*/
//					refreshButton = new JButton();
//					generationPanel.add(refreshButton);
//					refreshButton.setText("Generate");
//					refreshButton.addActionListener(new ActionListener() {
//						@Override
//						public void actionPerformed(ActionEvent arg0) {
//							refreshButton.setBackground(new ColorUIResource(238,238,238));
//							int numDirtySquares = (Integer) dirtField.getValue();
//							int numObstacles = (Integer) obstaclesField.getValue();
//							mainFrame.env.floor.clear();
//							mainFrame.env.floor.generateObject(numDirtySquares, numObstacles);
//							mainFrame.env.floor.set(0, 0, Square.Type.CLEAN);
//							mainFrame.env.floor.initialDirt = numDirtySquares;
//							mainFrame.getContentPane().remove(mainFrame.gridPanel);
//							mainFrame.gridPanel = new GridPanel(mainFrame.env);
//							mainFrame.getContentPane().add(mainFrame.gridPanel, BorderLayout.EAST);
//							mainFrame.pack();
//						}
//					});
//				}
//			}
//			{
//				agentPanel = new JPanel();
//				add(agentPanel, new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
//				Border marginOutside = new EmptyBorder(10,10,10,10);        
//				TitledBorder title = BorderFactory.createTitledBorder("Agent's settings");
//				CompoundBorder upperBorder = new CompoundBorder(marginOutside, title);
//				Border marginInside = new EmptyBorder(10,10,10,10);
//				agentPanel.setBorder(new CompoundBorder(upperBorder, marginInside));
//
//				agentPanel.setLayout(new GridLayout(2,1));
//
//				JPanel agentEnergyPanel = new JPanel();
//				agentEnergyPanel.setLayout(new FlowLayout());
//				agentPanel.add(agentEnergyPanel);
//
//				agentEnergylabel = new JLabel("Energy");
//				agentEnergyPanel.add(agentEnergylabel);
//				agentEnergyField = new JSpinner();
//				agentEnergyField.setValue(mainFrame.agent.energy);
//				agentEnergyField.setPreferredSize(new Dimension(60, 30));
//				agentEnergyField.addChangeListener(new ChangeListener() {
//
//					@Override
//					public void stateChanged(ChangeEvent arg0) {
//						mainFrame.agent.actionList.clear();
//						int energy = (Integer) SettingsPanel.this.agentEnergyField.getValue();
//						if(energy < 0){
//							energy = 0;
//							SettingsPanel.this.agentEnergyField.setValue(0);
//						}
//						mainFrame.agent.energy = energy;
//						update();
//					}
//				});
//				agentEnergyPanel.add(agentEnergyField);
//
//				JPanel agentVisibilityPanel = new JPanel();
//
//				agentPanel.add(agentVisibilityPanel);
//
//				agentVisibilityLabel = new JLabel("Visibility");
//
//				Vector<VisibilityType> visTypeVector = new Vector<VisibilityType>();
//				visTypeVector.add(VisibilityType.MY_CELL);
//				visTypeVector.add(VisibilityType.MY_NEIGHBOURS);
//				visTypeVector.add(VisibilityType.ALL);
//				agentVisibilityCombobox = new JComboBox(visTypeVector);
//				agentVisibilityCombobox.setSelectedItem(mainFrame.agent.visType);
//				agentVisibilityCombobox.addItemListener(new ItemListener() {
//
//					@Override
//					public void itemStateChanged(ItemEvent arg0) {
//						mainFrame.agent.visType = (VisibilityType) agentVisibilityCombobox.getSelectedItem();
//						update();
//					}
//				});
//
//				agentVisibilityPanel.setLayout(new FlowLayout());
//				agentVisibilityPanel.add(agentVisibilityLabel);
//				agentVisibilityPanel.add(agentVisibilityCombobox);
//			}
//			{
//				commandPanel = new JPanel();
//				commandPanel.setPreferredSize(new Dimension(300,110));
//				Border marginOutside = new EmptyBorder(10,10,10,10);        
//				TitledBorder title = BorderFactory.createTitledBorder("Commands");
//				CompoundBorder upperBorder = new CompoundBorder(marginOutside, title);
//				Border marginInside = new EmptyBorder(10,10,10,10);
//				commandPanel.setBorder(new CompoundBorder(upperBorder, marginInside));
//
//				add(commandPanel, new GridBagConstraints(0, 3, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
//				{   
//					/*Start simulation of agent*/
//					resetButton = new JButton();
//					commandPanel.add(resetButton);
//					resetButton.setText("Reset");
//					resetButton.addActionListener(new ActionListener() {
//						@Override
//						public void actionPerformed(ActionEvent arg0) {
////							mainFrame.agent.x = 0;
////							mainFrame.agent.y = 0;
////							mainFrame.agent.actionList.clear();
////							mainFrame.agent.energy = (Integer) SettingsPanel.this.agentEnergyField.getValue();
////							mainFrame.agent.goalReached = false;
//							
//							Class[] argsConstructor = new Class[] { int.class, int.class, VisibilityType.class, int.class };
//						    Integer x = new Integer(0);
//						    Integer y = new Integer(0);
//						    VisibilityType visType = (VisibilityType) SettingsPanel.this.agentVisibilityCombobox.getSelectedItem();
//						    Integer energy = new Integer((Integer) SettingsPanel.this.agentEnergyField.getValue());
//						    Object[] intArgs = new Object[] { x, y, visType, energy };
//							try {
//								Constructor constructor = mainFrame.agent.getClass().getConstructor(argsConstructor);
//								mainFrame.agent = (AbstractAgent) constructor.newInstance(intArgs);
//							} catch (Exception e) {
//								e.printStackTrace();
//							}
//						    
//							mainFrame.env.agent = mainFrame.agent;
//							
//							mainFrame.gridPanel.update();
//							mainFrame.settingsPanel.update();
//							mainFrame.pack();
//						}
//					});
//					/*Start simulation of agent*/
//					controlButton = new JButton();
//					commandPanel.add(controlButton);
//					controlButton.setIcon(playIcon);
//					controlButton.addActionListener(new ActionListener() {
//						@Override
//						public void actionPerformed(ActionEvent arg0) {
//							System.out.println("Clicked " + mainFrame.stopped);
//							if(mainFrame.stopped){
//								mainFrame.stopped = false;
//								System.out.println("Start thread");
//								class myThread implements Runnable{
//									public void run() {
//										mainFrame.mainLoop();
//									}
//								}
//								new Thread(new myThread()).start();
//							}
//							else{
//								mainFrame.stopped = true;
//								mainFrame.env.agent.x = 0;
//								mainFrame.env.agent.y = 0;
//								mainFrame.gridPanel.update();
//							}	
//							update();
//						}
//					});
//					/*Start simulation of agent*/
//					onesButton = new JButton();
//					commandPanel.add(onesButton);
//					onesButton.setIcon(nextIcon);
//					onesButton.addActionListener(new ActionListener() {
//						@Override
//						public void actionPerformed(ActionEvent arg0) {
//							class myThread implements Runnable{
//								public void run() {
//									mainFrame.mainLoopOnes();
//								}
//							}
//							new Thread(new myThread()).start();
//						}
//					});
//				}
//			}
//			{
//				statusPanel = new JPanel();
//				Border marginOutside = new EmptyBorder(10,10,10,10);        
//				TitledBorder title = BorderFactory.createTitledBorder("Status");
//				CompoundBorder upperBorder = new CompoundBorder(marginOutside, title);
//				Border marginInside = new EmptyBorder(10,10,10,10);
//				statusPanel.setBorder(new CompoundBorder(upperBorder, marginInside));
//				statusPanel.setLayout(new GridLayout(6,1));
//				add(statusPanel, new GridBagConstraints(0, 4, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
//
//				JPanel pmPanel = new JPanel();
//				JPanel currTypeEnvPanel = new JPanel();
//				statusPanel.add(currTypeEnvPanel);
//				{
//					currTypeEnvLabel = new JLabel("Environment's type:");
//					currTypeEnvValueLabel = new JLabel("" + mainFrame.env.type);
//					currTypeEnvPanel.add(currTypeEnvLabel);
//					currTypeEnvPanel.add(currTypeEnvValueLabel);
//				}
//				statusPanel.add(pmPanel);
//				{
//					pmLabel = new JLabel("Performance Measure:");
//					pmValueLabel = new JLabel("" + mainFrame.env.performanceMeasure());
//					pmPanel.add(pmLabel);
//					pmPanel.add(pmValueLabel);
//				}
//				JPanel stepPanel = new JPanel();
//				statusPanel.add(stepPanel);
//				{
//					stepsLabel = new JLabel("Steps Number:");
//					stepsValueLabel = new JLabel("" + mainFrame.agent.actionList.size());
//					stepPanel.add(stepsLabel);
//					stepPanel.add(stepsValueLabel);
//				}
//				JPanel currEnergyPanel = new JPanel();
//				statusPanel.add(currEnergyPanel);
//				{
//					currEnergyLabel = new JLabel("Agent's energy:");
//					currEnergyValueLabel = new JLabel("" + (mainFrame.agent.energy - mainFrame.agent.actionList.size()));
//					currEnergyPanel.add(currEnergyLabel);
//					currEnergyPanel.add(currEnergyValueLabel);
//				}
//				JPanel currVisibilityPanel = new JPanel();
//				statusPanel.add(currVisibilityPanel);
//				{
//					currVisibilityLabel = new JLabel("Agent's visibility:");
//					currVisibilityValueLabel = new JLabel("" + mainFrame.agent.visType);
//					currVisibilityPanel.add(currVisibilityLabel);
//					currVisibilityPanel.add(currVisibilityValueLabel);
//				}
//				JPanel goalPanel = new JPanel();
//				statusPanel.add(goalPanel);
//				{
//					goalReachedLabel = new JLabel("GOAL NOT REACHED");
//					goalReachedLabel.setForeground(Color.RED);
//					goalPanel.add(goalReachedLabel);
//				}
//			}
//		}
//	}
//	public void update() {
//		if(mainFrame.stopped)
//			controlButton.setIcon(playIcon);
//		else
//			controlButton.setIcon(stopIcon);
//
//		currTypeEnvValueLabel.setText("" + mainFrame.env.type);
//		pmValueLabel.setText("" + mainFrame.env.performanceMeasure());
//		stepsValueLabel.setText("" + mainFrame.agent.actionList.size());
//		currEnergyValueLabel.setText("" + mainFrame.agent.energy);
//		currVisibilityValueLabel.setText("" + mainFrame.agent.visType);
//		if(mainFrame.agent.goalReached){
//			goalReachedLabel.setForeground(Color.GREEN);
//			goalReachedLabel.setText("GOAL REACHED");
//		}
//		else{
//			goalReachedLabel.setForeground(Color.RED);
//			goalReachedLabel.setText("GOAL NOT REACHED");
//		}
//	}
//}
