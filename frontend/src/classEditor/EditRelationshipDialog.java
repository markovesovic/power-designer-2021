package classEditor;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Vector;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import modelEditor.figure.Relationship;

@SuppressWarnings("serial")
public class EditRelationshipDialog extends JDialog {

	private JLabel lblName;
	private JComboBox cmbName;
	private JButton btnOK;
	
	private boolean ok;
	
	public EditRelationshipDialog(Relationship rel) {
		lblName = new JLabel("Type of Relationship:");
		String types[]={"generalization", "association", "realization"};
		cmbName = new JComboBox(types);
		cmbName.setSelectedItem(cmbName.getItemAt(0).toString());
		btnOK = new JButton("OK");
		ok = false;
		
		btnOK.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				String val = cmbName.getSelectedItem().toString();
				
				rel.setName(val);
				dispose();
			}
		});
		
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		
		lblName.setAlignmentX(CENTER_ALIGNMENT);
		cmbName.setAlignmentX(CENTER_ALIGNMENT);
		btnOK.setAlignmentX(CENTER_ALIGNMENT);
		
		panel.add(Box.createVerticalGlue());
		panel.add(lblName);
		panel.add(Box.createVerticalGlue());
		panel.add(cmbName);
		panel.add(Box.createVerticalGlue());
		panel.add(btnOK);
		panel.add(Box.createVerticalGlue());
		add(panel);

		setSize(200,100);
		setModal(true);
		setLocationRelativeTo(null);	
	}
    
	public boolean showDialog() {
		setVisible(true);	
		return ok;
	}
	

	private void showMsgEmpty() {
		JOptionPane.showMessageDialog(this, "'From' field can't be empty");		
	}
    
}

