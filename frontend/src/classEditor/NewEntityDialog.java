package classEditor;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;

import javax.swing.*;

@SuppressWarnings("serial")
public class NewEntityDialog extends JDialog{
	
	private JLabel labelName;
	private JTextField tfName;
	private JButton btnOk;
	private JCheckBox cbIsAbstract;
	private JLabel labelIsAbstract;
	private String name;
	private JRadioButton rbClass, rbInterface;
	
	public NewEntityDialog() {
		
		JPanel panel = new JPanel();
		JPanel panelAbstract = new JPanel();
		JPanel panelType = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		panelAbstract.setLayout(new BoxLayout(panelAbstract, BoxLayout.X_AXIS));
		panelType.setLayout(new BoxLayout(panelType, BoxLayout.X_AXIS));
		
		labelName = new JLabel("Enter entity name:");
		labelName.setAlignmentX(CENTER_ALIGNMENT);
		tfName = new JTextField();
		tfName.setMaximumSize(new Dimension(200,20));
		tfName.setAlignmentX(CENTER_ALIGNMENT);
		cbIsAbstract = new JCheckBox();
		labelIsAbstract = new JLabel("Is abstract:");
		btnOk = new JButton("Ok");
		btnOk.setAlignmentX(CENTER_ALIGNMENT);
		rbClass = new JRadioButton("Class");
		rbInterface = new JRadioButton("Interface");
		rbClass.setSelected(true);

		panelType.add(rbClass);
		panelType.add(rbInterface);

		panelAbstract.add(labelIsAbstract);
		panelAbstract.add(cbIsAbstract);

		panel.add(Box.createVerticalGlue());
		panel.add(labelName);
		panel.add(Box.createVerticalGlue());
		panel.add(tfName);
		panel.add(Box.createVerticalGlue());
		panel.add(panelAbstract);
		panel.add(Box.createVerticalGlue());
		panel.add(panelType);
		panel.add(Box.createVerticalGlue());
		panel.add(btnOk);
		panel.add(Box.createVerticalGlue());
		add(panel);

		rbClass.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(rbInterface.isSelected())
					rbInterface.setSelected(false);
			}
		});

		rbInterface.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(rbClass.isSelected())
					rbClass.setSelected(false);
			}
		});
		
		btnOk.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				if(!tfName.getText().isEmpty()) {
					name = tfName.getText();
					if(Env.getInstance().entityNameExists(name)) {
						showMsgNameExists();
						name = null;
					}
					else
						dispose();
				}
				else
					showMsgEmpty();
			}
		});
		
		KeyListener kl = new KeyListener() {
			
			@Override
			public void keyTyped(KeyEvent e) {
			}
			
			@Override
			public void keyReleased(KeyEvent e) {
				if(e.getKeyCode()==KeyEvent.VK_ENTER)
					btnOk.doClick();
			}
			
			@Override
			public void keyPressed(KeyEvent e) {}
		};
		
		tfName.addKeyListener(kl);
		btnOk.addKeyListener(kl);
		
		setSize(250,200);
		setLocationRelativeTo(null);
		setModal(true);
		
	}
	
	private void showMsgEmpty() {
		JOptionPane.showMessageDialog(this, "Entity name can't be empty");		
	}
	
	private void showMsgNameExists() {
		JOptionPane.showMessageDialog(this, "Entity with this name already exists");		
	}
	
	public String getName() {
		setVisible(true);
		return name;
	}

	public boolean isAbstract(){
		return cbIsAbstract.isSelected();
	}

	public boolean isClass(){
		return rbClass.isSelected();
	}

}
