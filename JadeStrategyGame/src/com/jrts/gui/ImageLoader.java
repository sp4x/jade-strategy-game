package com.jrts.gui;

import javax.swing.ImageIcon;

class ImageLoader {
	public static ImageIcon freeIcon, workerIcon1, workerIcon2, workerIcon3, workerIcon4, treeIcon, woodIcon, foodIcon, workerFactoryIcon1, workerFactoryIcon2, workerFactoryIcon3, workerFactoryIcon4;
	public static ImageIcon hitIcon;
	public static int iconSize = 15;
	
	public static ImageIcon getWorkerFactoryImageIcon(String id){
		if(id.equals("team1")) return workerFactoryIcon1;
		if(id.equals("team2")) return workerFactoryIcon2;
		if(id.equals("team3")) return workerFactoryIcon3;
		if(id.equals("team4")) return workerFactoryIcon4;
		
		return null;
	}
	
	public static ImageIcon getWorkerImageIcon(String id){
		if(id == null){
			System.out.println("Requested id null!");
			return null;
		}
		if(id.equals("team1")) return workerIcon1;
		if(id.equals("team2")) return workerIcon2;
		if(id.equals("team3")) return workerIcon3;
		if(id.equals("team4")) return workerIcon4;
		
		return null;
	}
	
	static {
		freeIcon = new ImageIcon(new ImageIcon("img/tile.gif").getImage().getScaledInstance(iconSize,iconSize,iconSize));
		workerIcon1 = new ImageIcon(new ImageIcon("img/worker1.gif").getImage().getScaledInstance(iconSize,iconSize,iconSize));
		workerIcon2 = new ImageIcon(new ImageIcon("img/worker2.gif").getImage().getScaledInstance(iconSize,iconSize,iconSize));
		workerIcon3 = new ImageIcon(new ImageIcon("img/worker3.gif").getImage().getScaledInstance(iconSize,iconSize,iconSize));
		workerIcon4 = new ImageIcon(new ImageIcon("img/worker4.gif").getImage().getScaledInstance(iconSize,iconSize,iconSize));
		treeIcon = new ImageIcon(new ImageIcon("img/tree.png").getImage().getScaledInstance(iconSize,iconSize,iconSize));
		foodIcon = new ImageIcon(new ImageIcon("img/food.gif").getImage().getScaledInstance(iconSize,iconSize,iconSize));
		workerFactoryIcon1 = new ImageIcon(new ImageIcon("img/workerFactory1.png").getImage().getScaledInstance(iconSize,iconSize,iconSize));
		workerFactoryIcon2 = new ImageIcon(new ImageIcon("img/workerFactory2.png").getImage().getScaledInstance(iconSize,iconSize,iconSize));
		workerFactoryIcon3 = new ImageIcon(new ImageIcon("img/workerFactory3.png").getImage().getScaledInstance(iconSize,iconSize,iconSize));
		workerFactoryIcon4 = new ImageIcon(new ImageIcon("img/workerFactory4.png").getImage().getScaledInstance(iconSize,iconSize,iconSize));
		woodIcon = new ImageIcon(new ImageIcon("img/wood.png").getImage().getScaledInstance(iconSize,iconSize,iconSize));
		hitIcon = new ImageIcon(new ImageIcon("img/hit.gif").getImage().getScaledInstance(iconSize,iconSize,iconSize));		
	}
}