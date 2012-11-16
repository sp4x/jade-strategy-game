package com.jrts.common;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Line2D;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.UIManager;

import org.jgrapht.graph.DefaultWeightedEdge;

import com.jrts.environment.Position;

public class VisualGraph extends JFrame {
	private JPanel panel;
	int cellSize = 10;
	int size = 0;
	int offset = 10;
	
	static boolean exists = false;
	
	UndirectedWeightedGraph graph;
	
	class DrawLine extends JPanel {
		
		public Dimension getPreferredSize() {
			return new Dimension(size*20, size*20);
		}
		
		protected void paintComponent(Graphics g) {
			Graphics2D g2 = (Graphics2D) g;
			g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

			g2.setPaint(Color.red);
			
			for (DefaultWeightedEdge edge : graph.edgeSet()) {
				Position start = getGraph().getEdgeSource(edge);
				Position end = getGraph().getEdgeTarget(edge);
				g2.draw(
					new Line2D.Double(
						start.getCol()*cellSize + offset,
						start.getRow()*cellSize + offset,
						end.getCol()*cellSize + offset, end.getRow()*cellSize + offset));
//				g2.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 8));
//				g2.drawString("Line2D", start.getRow()*size + offset,start.getCol()*size + offset);
			}
	 
		}
	}

	public VisualGraph(UndirectedWeightedGraph graph, int size) {
		super();
		setGraph(graph);
		setSize(size);
		initializeComponent();
		setVisible(true);
		pack();
	}

	private void initializeComponent() {
		panel = new JPanel();
		
		getContentPane().setLayout(new FlowLayout());
		getContentPane().add(panel);
		panel.add( new DrawLine() );
		
		this.setTitle("VisualGraph");
		this.setSize(new Dimension(size*20, size*20));
		this.setResizable(false);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	public static void main(String[] args) {
		JFrame.setDefaultLookAndFeelDecorated(true);
		JDialog.setDefaultLookAndFeelDecorated(true);
		try {
			UIManager.setLookAndFeel( UIManager.getSystemLookAndFeelClassName() );
		} catch (Exception ex) {  }
		
		UndirectedWeightedGraph graph = new UndirectedWeightedGraph();
		graph.addWeightedEdge(new Position(1, 1), new Position(2, 2),1);
		graph.addWeightedEdge(new Position(1, 2), new Position(2, 2),1);
		graph.addWeightedEdge(new Position(1, 2), new Position(2, 0),1);
		
		new VisualGraph(graph, 10);
	}
	
	public static void show(UndirectedWeightedGraph graph, int size) {
		if(!exists){
			exists = true;
			JFrame.setDefaultLookAndFeelDecorated(true);
			JDialog.setDefaultLookAndFeelDecorated(true);
			try {
				UIManager.setLookAndFeel( UIManager.getSystemLookAndFeelClassName() );
			} catch (Exception ex) {  }
					
			new VisualGraph(graph, size);
		}
	}

	public UndirectedWeightedGraph getGraph() {
		return graph;
	}

	public void setGraph(UndirectedWeightedGraph graph) {
		this.graph = graph;
	}
	
	private void setSize(int size) {
		this.size = size;
	}
}


