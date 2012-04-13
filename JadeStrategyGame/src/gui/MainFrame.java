package gui;
import java.awt.BorderLayout;

import javax.swing.WindowConstants;

import com.jrts.environment.Floor;
import com.jrts.environment.World;


/**
 * Graphic interface, allow to create the environment configuration 
 * and display the sequence of actions performed by the agent
 *
 */
public class MainFrame extends javax.swing.JFrame {

	private static final long serialVersionUID = 1L;

	public GridPanel gridPanel;
	//	public SettingsPanel settingsPanel;

	public MainFrame(Floor floor) {
		super();
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		getContentPane().setLayout(new BorderLayout());
		//		setResizable(false);

		//		settingsPanel = new SettingsPanel(this);
		//		getContentPane().add(settingsPanel, BorderLayout.WEST);
		//		
		gridPanel = new GridPanel(floor);
		getContentPane().add(gridPanel, BorderLayout.EAST);

		pack();
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
		gridPanel.update();
	}
}
