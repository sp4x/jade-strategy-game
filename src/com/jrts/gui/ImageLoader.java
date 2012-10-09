package com.jrts.gui;

import javax.swing.ImageIcon;

class ImageLoader {
	public static ImageIcon freeIcon, workerIcon, treeIcon, woodIcon, foodIcon, workerFactoryIcon;
	public static ImageIcon hitIcon;
	public static int iconSize = 15;
	
	static {
		freeIcon = new ImageIcon(new ImageIcon("img/tile.gif").getImage().getScaledInstance(iconSize,iconSize,iconSize));
		workerIcon = new ImageIcon(new ImageIcon("img/worker.gif").getImage().getScaledInstance(iconSize,iconSize,iconSize));
		treeIcon = new ImageIcon(new ImageIcon("img/tree.png").getImage().getScaledInstance(iconSize,iconSize,iconSize));
		foodIcon = new ImageIcon(new ImageIcon("img/food.gif").getImage().getScaledInstance(iconSize,iconSize,iconSize));
		workerFactoryIcon = new ImageIcon(new ImageIcon("img/workerFactory.png").getImage().getScaledInstance(iconSize,iconSize,iconSize));
		woodIcon = new ImageIcon(new ImageIcon("img/wood.png").getImage().getScaledInstance(iconSize,iconSize,iconSize));
		hitIcon = new ImageIcon(new ImageIcon("img/hit.gif").getImage().getScaledInstance(iconSize,iconSize,iconSize));		
	}
}
