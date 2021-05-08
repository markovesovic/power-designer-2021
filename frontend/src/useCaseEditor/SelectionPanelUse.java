package useCaseEditor;

import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.*;

import modelEditor.figure.FigureGraphic;

@SuppressWarnings("serial")
public class SelectionPanelUse extends JScrollPane {
	protected WindowUse window;
	protected EnvUse env;
	protected JPanel panel;
	protected List<FigureObj> objs;
	
	public SelectionPanelUse(WindowUse window, EnvUse env) {
		this.window = window;
		this.env = env;
		setViewportView(panel = new JPanel());
		panel.setLayout(new GridBagLayout());
		onSelectionChanged();
	}
	
	public void onSelectionChanged() {
		panel.setVisible(false);
		panel.removeAll();
		GridBagConstraints constraints = new GridBagConstraints();
		constraints.fill = GridBagConstraints.HORIZONTAL;
		constraints.anchor = GridBagConstraints.NORTH;
		constraints.gridx = 0;
		constraints.gridy = 0;
		constraints.weightx = 1;
		constraints.weighty = 1;
		JLabel title = new JLabel("Selected figures");
		Font font = title.getFont();
		title.setFont(font.deriveFont(font.getStyle() ^ Font.BOLD));
		panel.add(title, constraints);
		int y = 1;
		objs = new ArrayList<SelectionPanelUse.FigureObj>();
		for(FigureGraphic figure : env.getSelected()) {
			constraints.gridy = y;
			FigureObj obj = new FigureObj(this, figure);
			panel.add(obj, constraints);
			++ y;
			objs.add(obj);
		}
		if(objs.size()>0) {
			FigureObj obj = objs.get(0);
			obj.setOpened(true);
		}
		panel.setVisible(true);
	}
	
	protected void closeAll() {
		for(FigureObj obj : objs) {
			obj.setOpened(false);
			obj.button.setEnabled(true);
		}
	}
	
	public class FigureObj extends JPanel {
		public SelectionPanelUse parent;
		public FigureGraphic figure;
		public JButton button;
		public JPanel options;

		public void setOpened(boolean opened) {
			options.setVisible(opened);
			button.setVisible(!opened);
		}
		public void open() {
			parent.closeAll();
			setOpened(true);
		}
		public FigureObj(final SelectionPanelUse parent, final FigureGraphic figure) {
			this.parent = parent;
			this.figure = figure;
			setLayout(new BorderLayout());
			button = new JButton(figure.getName());
			button.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					open();
				}
			});
			add(button, BorderLayout.NORTH);
			options = new JPanel();
			options.setLayout(new GridBagLayout());
			GridBagConstraints constraints = new GridBagConstraints();
			constraints.fill = GridBagConstraints.HORIZONTAL;
			constraints.anchor = GridBagConstraints.NORTH;
			constraints.gridwidth = 1;
			constraints.gridx = 0;
			constraints.gridy = 0;
			
			final JTextField nameField = new JTextField(figure.getName());
			
			options.add(new JLabel("Name: "), constraints);
			++ constraints.gridx;
			options.add(nameField, constraints);
			++constraints.gridy;
			constraints.gridx = 0;
			
			options.add(new JLabel("Form: "), constraints);
			++ constraints.gridx;
			options.add(new JLabel(figure.getShapeName()), constraints);
			++constraints.gridy;
			constraints.gridx = 0;
			
			JLabel bgLabel = new JLabel("Color: ");
			final Canvas bgColor = new Canvas();
			bgColor.setSize(20, 20);
			bgColor.setBackground(figure.getBackgroundColor());
			options.add(bgLabel, constraints);
			++ constraints.gridx;
			options.add(bgColor, constraints);
			++constraints.gridy;
			constraints.gridx = 0;

			
			add(options, BorderLayout.CENTER);
			
			
			nameField.addKeyListener(new KeyAdapter() {
				public void keyReleased(KeyEvent e) {
					String name = nameField.getText();
					if(name.length()==0) return;
					figure.setName(name);
					button.setText(name);
					env.getCanvas().repaint();
				}
			});
			
			setOpened(false);
		}
	}
}
