package classEditor;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

//import org.json.JSONObject;

import metaschemaEditor.figure.Entity;

@SuppressWarnings("serial")
public class EditEntityDialog extends JDialog {

	private JLabel lblName;
	private JTextField tfName;
	private JButton btnAddAttr;
	private JButton btnDone;
	
	public EditEntityDialog(Entity ent) {
		lblName = new JLabel("Entity name:");
		tfName = new JTextField(ent.getName());
		btnAddAttr = new JButton("Add attribute...");
		btnDone = new JButton("Done");
		
//		btnAddAttr.addActionListener(new ActionListener() {
//
//			@Override
//			public void actionPerformed(ActionEvent e) {
//				JSONObject attr = new NewAtributeDialog().getAttr();
//				if(attr!=null) ent.addAttribute(attr);
//			}
//		});
		
		btnDone.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				String name = tfName.getText();
				if(name.equals(ent.getName())) {
					dispose();
					return;
				}
				if(name.isEmpty()) {
					showMsgEmpty();
					return;
				}
				if(Env.getInstance().entityNameExists(name)) {
					showMsgNameExists();
					return;
				}
				ent.setName(name);
				dispose();
			}
		});
		
		JPanel btns = new JPanel();
		btns.setLayout(new BoxLayout(btns, BoxLayout.X_AXIS));
		btns.add(Box.createHorizontalGlue());
		btns.add(btnAddAttr);
		btns.add(Box.createHorizontalGlue());
		btns.add(btnDone);
		btns.add(Box.createHorizontalGlue());
		
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		panel.add(Box.createVerticalGlue());
		lblName.setAlignmentX(CENTER_ALIGNMENT);		
		panel.add(lblName);
		panel.add(Box.createVerticalGlue());
		tfName.setMaximumSize(new Dimension(200,20));
		tfName.setAlignmentX(CENTER_ALIGNMENT);
		panel.add(tfName);
		panel.add(Box.createVerticalGlue());
		panel.add(btns);
		panel.add(Box.createVerticalGlue());
		add(panel);
		
		setSize(300,200);
		setModal(true);
		setLocationRelativeTo(null);
		setVisible(true);
	}
	
	private void showMsgEmpty() {
		JOptionPane.showMessageDialog(this, "Entity name can't be empty");		
	}
	
	private void showMsgNameExists() {
		JOptionPane.showMessageDialog(this, "Entity with this name already exists");		
	}
}
