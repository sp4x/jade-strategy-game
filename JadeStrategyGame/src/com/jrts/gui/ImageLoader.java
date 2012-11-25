package com.jrts.gui;


import java.awt.Dimension;
import java.util.logging.Logger;

import javax.swing.Icon;
import javax.swing.ImageIcon;

import com.jrts.common.GameConfig;

class ImageLoader {
	
	static Logger logger = Logger.getLogger(ImageLoader.class.getName());
	
	public static ImageIcon freeIcon, explosionIcon, workerIcon1, workerIcon2, workerIcon3, workerIcon4, 
							treeIcon, farmIcon, woodIcon, foodIcon, workerFactoryIcon1, soldierIcon1, 
							soldierIcon2, soldierIcon3, soldierIcon4, background,
							workerFactoryIcon2, workerFactoryIcon3, workerFactoryIcon4;
	
	public static ImageIcon freeIcon_s, workerIcon1_s, workerIcon2_s, workerIcon3_s, workerIcon4_s, 
							treeIcon_s, farmIcon_s, woodIcon_s, foodIcon_s, workerFactoryIcon1_s, soldierIcon1_s, 
							soldierIcon2_s, soldierIcon3_s, soldierIcon4_s, background_s,
							workerFactoryIcon2_s, workerFactoryIcon3_s, workerFactoryIcon4_s;
	
	public static ImageIcon hitIcon;
	
	public static ImageIcon getWorkerFactoryImageIcon(String id){
		if(id.equals("team1")) return workerFactoryIcon1;
		if(id.equals("team2")) return workerFactoryIcon2;
		if(id.equals("team3")) return workerFactoryIcon3;
		if(id.equals("team4")) return workerFactoryIcon4;
		
		return null;
	}
	
	public static ImageIcon getSelectedWorkerFactoryImageIcon(String id){
		if(id.equals("team1")) return workerFactoryIcon1_s;
		if(id.equals("team2")) return workerFactoryIcon2_s;
		if(id.equals("team3")) return workerFactoryIcon3_s;
		if(id.equals("team4")) return workerFactoryIcon4_s;
		
		return null;
	}
	
	public static ImageIcon getWorkerImageIcon(String id){
		if(id == null){
			logger.severe("Requested id null!");
			return null;
		}
		if(id.equals("team1")) return workerIcon1;
		if(id.equals("team2")) return workerIcon2;
		if(id.equals("team3")) return workerIcon3;
		if(id.equals("team4")) return workerIcon4;
		
		return null;
	}
	
	public static ImageIcon getSelectedWorkerImageIcon(String id){
		if(id == null){
			logger.severe("Requested id null!");
			return null;
		}
		if(id.equals("team1")) return workerIcon1_s;
		if(id.equals("team2")) return workerIcon2_s;
		if(id.equals("team3")) return workerIcon3_s;
		if(id.equals("team4")) return workerIcon4_s;
		
		return null;
	}
	
	public static Icon getSoldierImageIcon(String teamName) {
		if(teamName == null){
			logger.severe("Requested id null!");
			return null;
		}
		
		if(teamName.equals("team1")) return soldierIcon1;
		if(teamName.equals("team2")) return soldierIcon2;
		if(teamName.equals("team3")) return soldierIcon3;
		if(teamName.equals("team4")) return soldierIcon4;
		
		return null;
	}
	
	public static Icon getSelectedSoldierImageIcon(String id) {
		if(id == null){
			logger.severe("Requested id null!");
			return null;
		}
		
		if(id.equals("team1")) return soldierIcon1_s;
		if(id.equals("team2")) return soldierIcon2_s;
		if(id.equals("team3")) return soldierIcon3_s;
		if(id.equals("team4")) return soldierIcon4_s;
		
		return null;
	}
	
	public static ImageIcon getBackgroundImage(Dimension d)
	{
		return new ImageIcon(new ImageIcon("img/sabbia.jpg").getImage().getScaledInstance((int)d.getWidth(), (int)d.getHeight(), 15));
	}
	
	static {
		freeIcon = new ImageIcon(new ImageIcon("img/tile.png").getImage().getScaledInstance(GameConfig.ICON_SIZE,GameConfig.ICON_SIZE,GameConfig.ICON_SIZE));
		explosionIcon = new ImageIcon("img/esplosione.gif"); //new ImageIcon(new ImageIcon("img/esplosione.gif").getImage().getScaledInstance(GameConfig.ICON_SIZE,GameConfig.ICON_SIZE,GameConfig.ICON_SIZE));
		
		workerFactoryIcon1 = new ImageIcon(new ImageIcon("img/workerFactory1.png").getImage().getScaledInstance(GameConfig.ICON_SIZE,GameConfig.ICON_SIZE,GameConfig.ICON_SIZE));
		workerFactoryIcon2 = new ImageIcon(new ImageIcon("img/workerFactory2.png").getImage().getScaledInstance(GameConfig.ICON_SIZE,GameConfig.ICON_SIZE,GameConfig.ICON_SIZE));
		workerFactoryIcon3 = new ImageIcon(new ImageIcon("img/workerFactory3.png").getImage().getScaledInstance(GameConfig.ICON_SIZE,GameConfig.ICON_SIZE,GameConfig.ICON_SIZE));
		workerFactoryIcon4 = new ImageIcon(new ImageIcon("img/workerFactory4.png").getImage().getScaledInstance(GameConfig.ICON_SIZE,GameConfig.ICON_SIZE,GameConfig.ICON_SIZE));
		
		soldierIcon1 = new ImageIcon(new ImageIcon("img/soldier1.png").getImage().getScaledInstance(GameConfig.ICON_SIZE,GameConfig.ICON_SIZE,GameConfig.ICON_SIZE));
		soldierIcon2 = new ImageIcon(new ImageIcon("img/soldier2.png").getImage().getScaledInstance(GameConfig.ICON_SIZE,GameConfig.ICON_SIZE,GameConfig.ICON_SIZE));
		soldierIcon3 = new ImageIcon(new ImageIcon("img/soldier3.png").getImage().getScaledInstance(GameConfig.ICON_SIZE,GameConfig.ICON_SIZE,GameConfig.ICON_SIZE));
		soldierIcon4 = new ImageIcon(new ImageIcon("img/soldier4.png").getImage().getScaledInstance(GameConfig.ICON_SIZE,GameConfig.ICON_SIZE,GameConfig.ICON_SIZE));
		
		workerIcon1 = new ImageIcon(new ImageIcon("img/worker1.png").getImage().getScaledInstance(GameConfig.ICON_SIZE,GameConfig.ICON_SIZE,GameConfig.ICON_SIZE));
		workerIcon2 = new ImageIcon(new ImageIcon("img/worker2.png").getImage().getScaledInstance(GameConfig.ICON_SIZE,GameConfig.ICON_SIZE,GameConfig.ICON_SIZE));
		workerIcon3 = new ImageIcon(new ImageIcon("img/worker3.png").getImage().getScaledInstance(GameConfig.ICON_SIZE,GameConfig.ICON_SIZE,GameConfig.ICON_SIZE));
		workerIcon4 = new ImageIcon(new ImageIcon("img/worker4.png").getImage().getScaledInstance(GameConfig.ICON_SIZE,GameConfig.ICON_SIZE,GameConfig.ICON_SIZE));
		
		treeIcon = new ImageIcon(new ImageIcon("img/tree.png").getImage().getScaledInstance(GameConfig.ICON_SIZE,GameConfig.ICON_SIZE,GameConfig.ICON_SIZE));
		farmIcon = new ImageIcon(new ImageIcon("img/farm.png").getImage().getScaledInstance(GameConfig.ICON_SIZE,GameConfig.ICON_SIZE,GameConfig.ICON_SIZE));
		
		// SELECTED
		freeIcon_s = new ImageIcon(new ImageIcon("img/tile_s.png").getImage().getScaledInstance(GameConfig.ICON_SIZE,GameConfig.ICON_SIZE,GameConfig.ICON_SIZE));

		workerFactoryIcon1_s = new ImageIcon(new ImageIcon("img/workerFactory1_s.png").getImage().getScaledInstance(GameConfig.ICON_SIZE,GameConfig.ICON_SIZE,GameConfig.ICON_SIZE));
		workerFactoryIcon2_s = new ImageIcon(new ImageIcon("img/workerFactory2_s.png").getImage().getScaledInstance(GameConfig.ICON_SIZE,GameConfig.ICON_SIZE,GameConfig.ICON_SIZE));
		workerFactoryIcon3_s = new ImageIcon(new ImageIcon("img/workerFactory3_s.png").getImage().getScaledInstance(GameConfig.ICON_SIZE,GameConfig.ICON_SIZE,GameConfig.ICON_SIZE));
		workerFactoryIcon4_s = new ImageIcon(new ImageIcon("img/workerFactory4_s.png").getImage().getScaledInstance(GameConfig.ICON_SIZE,GameConfig.ICON_SIZE,GameConfig.ICON_SIZE));
		
		soldierIcon1_s = new ImageIcon(new ImageIcon("img/soldier1_s.png").getImage().getScaledInstance(GameConfig.ICON_SIZE,GameConfig.ICON_SIZE,GameConfig.ICON_SIZE));
		soldierIcon2_s = new ImageIcon(new ImageIcon("img/soldier2_s.png").getImage().getScaledInstance(GameConfig.ICON_SIZE,GameConfig.ICON_SIZE,GameConfig.ICON_SIZE));
		soldierIcon3_s = new ImageIcon(new ImageIcon("img/soldier3_s.png").getImage().getScaledInstance(GameConfig.ICON_SIZE,GameConfig.ICON_SIZE,GameConfig.ICON_SIZE));
		soldierIcon4_s = new ImageIcon(new ImageIcon("img/soldier4_s.png").getImage().getScaledInstance(GameConfig.ICON_SIZE,GameConfig.ICON_SIZE,GameConfig.ICON_SIZE));
		
		workerIcon1_s = new ImageIcon(new ImageIcon("img/worker1_s.png").getImage().getScaledInstance(GameConfig.ICON_SIZE,GameConfig.ICON_SIZE,GameConfig.ICON_SIZE));
		workerIcon2_s = new ImageIcon(new ImageIcon("img/worker2_s.png").getImage().getScaledInstance(GameConfig.ICON_SIZE,GameConfig.ICON_SIZE,GameConfig.ICON_SIZE));
		workerIcon3_s = new ImageIcon(new ImageIcon("img/worker3_s.png").getImage().getScaledInstance(GameConfig.ICON_SIZE,GameConfig.ICON_SIZE,GameConfig.ICON_SIZE));
		workerIcon4_s = new ImageIcon(new ImageIcon("img/worker4_s.png").getImage().getScaledInstance(GameConfig.ICON_SIZE,GameConfig.ICON_SIZE,GameConfig.ICON_SIZE));
		
		treeIcon_s = new ImageIcon(new ImageIcon("img/tree_s.png").getImage().getScaledInstance(GameConfig.ICON_SIZE,GameConfig.ICON_SIZE,GameConfig.ICON_SIZE));
		farmIcon_s = new ImageIcon(new ImageIcon("img/farm_s.png").getImage().getScaledInstance(GameConfig.ICON_SIZE,GameConfig.ICON_SIZE,GameConfig.ICON_SIZE));
		
		
		foodIcon = new ImageIcon(new ImageIcon("img/food.png").getImage().getScaledInstance(GameConfig.ICON_SIZE,GameConfig.ICON_SIZE,GameConfig.ICON_SIZE));
		woodIcon = new ImageIcon(new ImageIcon("img/wood.png").getImage().getScaledInstance(GameConfig.ICON_SIZE,GameConfig.ICON_SIZE,GameConfig.ICON_SIZE));
		hitIcon = new ImageIcon(new ImageIcon("img/hit.png").getImage().getScaledInstance(GameConfig.ICON_SIZE,GameConfig.ICON_SIZE,GameConfig.ICON_SIZE));
	}
}