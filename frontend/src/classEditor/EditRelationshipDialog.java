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

//import org.json.JSONObject;

import metaschemaEditor.figure.Relationship;

@SuppressWarnings("serial")
public class EditRelationshipDialog extends JDialog {

	private JLabel lblName;
	private JTextField tfName;
	private JButton btnOK;
	
	private boolean ok;
	
	public EditRelationshipDialog(Relationship rel) {
		lblName = new JLabel("Type of Relationship:");
		tfName = new JTextField();
		btnOK = new JButton("OK");
		ok = false;
		
		btnOK.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				String val = tfName.getText();
				
				rel.setName(val);
				dispose();
			}
		});
		
//		JPanel from = new JPanel();
//		from.setLayout(new GridLayout(2,1));
//		lblFrom.setAlignmentX(CENTER_ALIGNMENT);
//		cmbFrom.setAlignmentX(CENTER_ALIGNMENT);
//		from.add(lblFrom);
//		from.add(cmbFrom);
//		JPanel to = new JPanel();
//		to.setLayout(new GridLayout(2,1));
//		lblTo.setAlignmentX(CENTER_ALIGNMENT);
//		lblTo.setSize(new Dimension(100,10));
//		cmbTo.setAlignmentX(CENTER_ALIGNMENT);
//		to.add(lblTo);
//		to.add(cmbTo);
//		JPanel up = new JPanel();
//		up.setLayout(new GridLayout(1,2));
//		up.add(from);
//		up.add(to);
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		
//		panel.add(up);
		lblName.setAlignmentX(CENTER_ALIGNMENT);
		tfName.setAlignmentX(CENTER_ALIGNMENT);
//		panel.add(Box.createVerticalGlue());
		btnOK.setAlignmentX(CENTER_ALIGNMENT);
		
		panel.add(Box.createVerticalGlue());
		panel.add(lblName);
		panel.add(Box.createVerticalGlue());
		panel.add(tfName);
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
	
//	private Vector<String> getAttributeNames(ArrayList<JSONObject> attrs){
//		try {
//			Vector<String> names = new Vector<String>();
//			names.add("");
//			for (JSONObject attr : attrs)
//				names.add(attr.getString("name"));
//			return names;
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		return null;
//	}

	private void showMsgEmpty() {
		JOptionPane.showMessageDialog(this, "'From' field can't be empty");		
	}
    
}

