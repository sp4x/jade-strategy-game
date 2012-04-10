package gui;

import javax.swing.ImageIcon;

class ImageLoader {
	public static ImageIcon freeIcon, workerIcon, treeIcon;
	public static int iconSize = 13;
	
	static {
		freeIcon = new ImageIcon(new ImageIcon("img/tile.jpeg").getImage().getScaledInstance(iconSize,iconSize,iconSize));
		workerIcon = new ImageIcon(new ImageIcon("img/worker.gif").getImage().getScaledInstance(iconSize,iconSize,iconSize));
		treeIcon = new ImageIcon(new ImageIcon("img/tree.png").getImage().getScaledInstance(iconSize,iconSize,iconSize));
	}
}
