package view.frames;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JOptionPane;
import javax.swing.JToolBar;

import view.ToolBar;

@SuppressWarnings("serial")
public class MainFrame extends MyFrame {
	private final JToolBar toolBar = new ToolBar();


	public MainFrame(Dimension dimension) {
		initializeFrame(dimension.height, dimension.width);
		setVisible(true);
	}

	public MainFrame() {
		initializeFrame(0, 0);
		setVisible(true);
	}
	
	protected void initializeFrame(float screenHeightPercent, float screenWidthPercent) {

		add(toolBar, BorderLayout.NORTH);
		setSize(250, 80);
		setLocationRelativeTo(null);
		setTitle("Sums");
		setVisible(true);

		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		setCloseMethod();
	}

	private void setCloseMethod() {
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				int confirmed = JOptionPane.showConfirmDialog(null, "Do you want exit?", "Exit",
						JOptionPane.YES_NO_OPTION);

				if (confirmed == JOptionPane.YES_OPTION) {
					((JFrame)e.getSource()).setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);				
				}
			}
		});
	}
}
