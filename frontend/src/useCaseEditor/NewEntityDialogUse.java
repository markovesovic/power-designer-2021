package useCaseEditor;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

@SuppressWarnings("serial")
public class NewEntityDialogUse extends JDialog{
	
	private JLabel labelName;
	private JTextField tfName;
	private JButton btnOk;
	private String name;
	
	public NewEntityDialogUse() {
		
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		
		labelName = new JLabel("Enter entity name:");
		labelName.setAlignmentX(CENTER_ALIGNMENT);
		tfName = new JTextField();
		tfName.setMaximumSize(new Dimension(200,20));
		tfName.setAlignmentX(CENTER_ALIGNMENT);
		btnOk = new JButton("Ok");
		btnOk.setAlignmentX(CENTER_ALIGNMENT);

		panel.add(Box.createVerticalGlue());
		panel.add(labelName);
		panel.add(Box.createVerticalGlue());
		panel.add(tfName);
		panel.add(Box.createVerticalGlue());
		panel.add(btnOk);
		panel.add(Box.createVerticalGlue());
		add(panel);
		
		btnOk.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				if(!tfName.getText().isEmpty()) {
					name = tfName.getText();
					if(EnvUse.getInstance().entityNameExists(name)) {
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

}
