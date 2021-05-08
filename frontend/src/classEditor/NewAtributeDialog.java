package classEditor;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

import modelEditor.figure.Attributes;
import modelEditor.figure.Entity;



@SuppressWarnings("serial")
public class NewAtributeDialog extends JDialog{
	
	private JLabel lblAccess;
	private JTextField tfAccess;
	private JLabel lblName;
	private JTextField tfName;
	private JLabel lblType;
	private JTextField tfType;
	private JButton btnOk;
	private JLabel lblFunc;
	private JCheckBox cbFunc;
	private JComboBox cmbAccess;
	
	public NewAtributeDialog(Entity ent) {
		JPanel left = new JPanel();
		left.setLayout(new BoxLayout(left, BoxLayout.Y_AXIS));
		left.add(Box.createVerticalGlue());
		lblAccess = new JLabel("Access Modifier:");
		left.add(lblAccess);
		left.add(Box.createVerticalGlue());
		//tfAccess = new JTextField();
		String accessModifiers[]={"private","public","protected","default"};
		cmbAccess = new JComboBox(accessModifiers);
		cmbAccess.setSelectedItem(cmbAccess.getItemAt(0));
		left.add(cmbAccess);
		left.add(Box.createVerticalGlue());
		lblName = new JLabel("Name:");
		left.add(lblName);
		left.add(Box.createVerticalGlue());
		tfName = new JTextField();
		left.add(tfName);
		left.add(Box.createVerticalGlue());
		lblType = new JLabel("Type:");
		left.add(lblType);
		tfType = new JTextField();
		left.add(tfType);
		left.add(Box.createVerticalGlue());
		left.add(Box.createVerticalGlue());
		lblFunc = new JLabel("Function:");
		cbFunc = new JCheckBox();

		btnOk = new JButton("Ok");
		
		
		btnOk.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				String name = cmbAccess.getSelectedItem().toString();
				if(name.isEmpty()) {
					showMessage("Access Modifier");
					return;
				}
				String dt = tfName.getText();
				if(dt.isEmpty()) {
					showMessage("Name of method");
					return;
				}
				String type = tfType.getText();
				if(type.isEmpty()) {
					showMessage("Type");
					return;
				}
				
				ent.getList().add(new Attributes(name, dt, type, cbFunc.isSelected()));
				dispose();
			}
		});		
		
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		JPanel up = new JPanel();
		up.setLayout(new GridLayout(1, 2));
		JPanel panelFunc = new JPanel();
		panelFunc.setLayout(new BoxLayout(panelFunc, BoxLayout.X_AXIS));
		panelFunc.add(lblFunc);
		panelFunc.add(cbFunc);
		up.add(left);
		panel.add(up);
		panel.add(Box.createVerticalGlue());
		panel.add(panelFunc);
		panel.add(Box.createVerticalGlue());
		btnOk.setAlignmentX(CENTER_ALIGNMENT);
		panel.add(btnOk);
		panel.add(Box.createVerticalGlue());
		
		add(panel);
		
		setSize(400,200);
		setLocationRelativeTo(null);
		setModal(true);
	}
	
	private void showMessage(String fieldName) {
		JOptionPane.showMessageDialog(this, "Field "+fieldName+" can't be empty");		
	}
	
//	public JSONObject getAttr() {
//		setVisible(true);
//		return attr;
//	}
}
