package classEditor;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;

import javax.swing.*;

@SuppressWarnings("serial")
public class Window extends JFrame {
	
	private CanvasArea canvas;
	public ToolBox toolbox;
	public SelectionPanel selectionPanel;
	
	private Env env;
	private File file;
	
	public Window(String filePath) {
		Env.CreateInstance(this); 
		env = Env.getInstance();
		file = new File(filePath);
		
		setBounds(0, 0, 800, 600);
		setMinimumSize(new Dimension(400, 300));
		setLocationRelativeTo(null);
		setTitle("Shape Editor");
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				if(confirm("Do you want close graphic editor?"))
					dispose();
			}
		});
		
		Container pane = getContentPane();
		pane.setLayout(new GridBagLayout());
		GridBagConstraints constraints = new GridBagConstraints();

		canvas = new CanvasArea(env);
		toolbox = new ToolBox(this, env, canvas);
		selectionPanel = new SelectionPanel(this, env);
		CanvasMouseListener cml = new CanvasMouseListener(canvas, env);
		env.setToolbox(toolbox);
		env.setSelectionPanel(selectionPanel);
		env.setCanvas(canvas);
		env.setCanvasMouseListener(cml);
		//env.readMetaScheme(new File("json_seme/salata_bar.json"));
		
		canvas.addMouseListener(cml);
		canvas.addMouseMotionListener(cml);
		
		constraints.insets = new Insets(2, 2, 2, 2);
		constraints.fill = GridBagConstraints.BOTH;
		constraints.gridx = 0;
		constraints.gridy = 0;
		constraints.weightx = 0;
		constraints.weighty = 0;
		pane.add(toolbox, constraints);

		constraints.gridx = 0;
		constraints.gridy = 1;
		pane.add(selectionPanel, constraints);

		constraints.gridx = 1;
		constraints.gridy = 0;
		constraints.gridheight = 2;
		constraints.weightx = 1.0;
		constraints.weighty = 1.0;
		pane.add(canvas, constraints);
		
		setVisible(true);
	}
	
	public File getFile() {
		return file;
	}
	
	public boolean confirm(String message, String title) {
		return JOptionPane.OK_OPTION == JOptionPane.showConfirmDialog(this, message, title, 
										JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);
	}
	
	public boolean confirm(String message) {
		return confirm(message, null);
	}
	
	public void error(String message) {
		JOptionPane.showConfirmDialog(this, message, null, JOptionPane.CLOSED_OPTION, JOptionPane.ERROR_MESSAGE);
	}
	
}
