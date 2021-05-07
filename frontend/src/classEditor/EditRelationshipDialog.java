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

//import org.json.JSONObject;

import metaschemaEditor.figure.Relationship;

@SuppressWarnings("serial")
public class EditRelationshipDialog extends JDialog {

	private JLabel lblFrom;
	private JLabel lblTo;
	private JComboBox<String> cmbFrom;
	private JComboBox<String> cmbTo;
	private JButton btnOK;
	
	private boolean ok;
	
	public EditRelationshipDialog(Relationship rel) {
		lblFrom = new JLabel("From");
		lblTo = new JLabel("To");
//		cmbFrom = new JComboBox<String>(getAttributeNames(rel.getEntity1().getAttributes()));
//		cmbTo = new JComboBox<String>(getAttributeNames(rel.getEntity2().getAttributes()));
		btnOK = new JButton("OK");
		ok = false;
		
		btnOK.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				String val = (String) cmbFrom.getSelectedItem();
				if(val==null || val.isEmpty()) {
					showMsgEmpty();
					return;
				}
				rel.setFrom(val);
				val = (String) cmbTo.getSelectedItem();
				if(val!=null && !val.isEmpty())
					rel.setTo(val);
				ok=true;
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
//		panel.add(Box.createVerticalGlue());
		btnOK.setAlignmentX(CENTER_ALIGNMENT);
		panel.add(btnOK);
		panel.add(Box.createVerticalGlue());
		add(panel);

		setSize(300,200);
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

