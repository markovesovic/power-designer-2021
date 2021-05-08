package useCaseEditor;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import javax.swing.*;


import app.AppCore;
import classEditor.Mode;
import useCaseEditor.ModeUse;
import modelEditor.figure.Entity;
import modelEditor.figure.Relationship;

@SuppressWarnings("serial")
public class ToolBoxUse extends JPanel {
	
	public JButton select = new JButton(new ImageIcon("images/select.png"));
	public JButton move = new JButton(new ImageIcon("images/move.png"));
	public JButton newRectangle = new JButton(new ImageIcon("images/rectangle.png"));
	public JButton deleteFigure = new JButton(new ImageIcon("images/deleteFigure.png"));
	public JButton newLine = new JButton(new ImageIcon("images/line.png"));
	private JButton save = new JButton(new ImageIcon("images/save.png"));
	private JButton newActor = new JButton(new ImageIcon("images/man.png"));
	private JButton undo = new JButton(new ImageIcon("images/undo.png"));
	private JButton redo = new JButton(new ImageIcon("images/redo.png"));
	
	protected List<JButton> buttons = new ArrayList<JButton>();

	protected Canvas bgColor = new Canvas();
	protected Canvas strokeColor = new Canvas();
	
	protected EnvUse env;
	protected WindowUse window;
	protected CanvasAreaUse canvas;
	
	private JButton addImageButton(JButton b) {
		Dimension d = new Dimension(36, 36);
		b.setPreferredSize(d);
		b.setMinimumSize(d);
		b.setMaximumSize(d);
		buttons.add(b);
		return b;
	}
	private void select(JButton button) {
		for(JButton b : buttons) {
			b.setSelected(false);
			b.setBackground(null);
		}
		button.setSelected(true);
		button.setBackground(Color.white);
	}
	
	public ToolBoxUse(WindowUse window, EnvUse env, CanvasAreaUse canvas) {
		this.env = env;
		this.window = window;
		this.canvas = canvas;
		setBorder(BorderFactory.createEtchedBorder());
		GridBagLayout l = new GridBagLayout();
		GridBagConstraints c = new GridBagConstraints();
		setLayout(l);
		
		c.gridx = 0;
		c.gridy = 0;
		c.anchor = GridBagConstraints.WEST;
		
		JPanel panel = new JPanel();
		
		panel.add(addImageButton(select));
		panel.add(addImageButton(move));
		panel.add(addImageButton(deleteFigure));
		panel.add(addImageButton(undo));
		panel.add(addImageButton(redo));
		
		add(panel, c);
		c.gridy++;
		panel = new JPanel();
		panel.add(addImageButton(newRectangle));
		panel.add(addImageButton(newLine));
		panel.add(addImageButton(save));
		panel.add(addImageButton(newActor));
		add(panel, c);
		
		c.gridy++;
		c.anchor = GridBagConstraints.CENTER;
		
		panel = new JPanel();
		add(panel, c);		
		
		select.addActionListener(new SelectListener());
		move.addActionListener(new MoveListener());
		deleteFigure.addActionListener(new DeleteListener());
		newRectangle.addActionListener(new NewRectangleListener());
		newLine.addActionListener(new NewLineListener());
		newActor.addActionListener(new NewActorListener());
		save.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					File file = window.getFile();
				} catch (Exception e2) {
					e2.printStackTrace();
				}
			}
		});
		select(move);
	}

	class SelectListener extends ButtonListener {
		public SelectListener() {
			super(ModeUse.SELECT);
		}
		public void actionPerformed(ActionEvent e) {
			super.actionPerformed(e);
		}
	}
	
	
	class DeleteListener extends ButtonListener {
		
		public DeleteListener() {
			super(ModeUse.DELETE);
		}

		public void actionPerformed(ActionEvent e) {
			env.removeSelected();
		}
	}
	
	class MoveListener extends ButtonListener {
		public MoveListener() {
			super(ModeUse.MOVE);
		}
		public void actionPerformed(ActionEvent e) {
			super.actionPerformed(e);
		}
	}
	class NewRectangleListener extends ButtonListener {
		public NewRectangleListener() {
			super(ModeUse.DRAW_RECTANGLE);
		}
		public void actionPerformed(ActionEvent e) {
			super.actionPerformed(e);
		}
	}
	
	class NewActorListener extends ButtonListener {
		public NewActorListener() {
			super(ModeUse.DRAW_ACTOR);
		}
		public void actionPerformed(ActionEvent e) {
			super.actionPerformed(e);
		}
	}
	
	class NewLineListener extends ButtonListener{
		
		public NewLineListener() {
			super(ModeUse.DRAW_LINE);
		}		
		@Override
		public void actionPerformed(ActionEvent e) {
			super.actionPerformed(e);
		}
		
	}
	
	class ButtonListener implements ActionListener {
		ModeUse mode;
		public ButtonListener(ModeUse mode) {
			this.mode = mode;
		}
		public void actionPerformed(ActionEvent e) {
			if(e.getSource() instanceof JButton) 
				select((JButton)e.getSource());
			env.getCanvas().setMode(mode);
			env.getCanvasMouseListener().onToolChanged(mode);
		}
	}
}
