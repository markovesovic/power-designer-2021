package classEditor;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

import metaschemaEditor.figure.Attributes;
import metaschemaEditor.figure.Entity;

//import org.json.JSONException;
//import org.json.JSONObject;


@SuppressWarnings("serial")
public class NewAtributeDialog extends JDialog{
//	private JSONObject attr;
	
	private JLabel lblAccess;
	private JTextField tfAccess;
	private JLabel lblName;
	private JTextField tfName;
	private JLabel lblType;
	private JTextField tfType;
	private JButton btnOk;
	
	public NewAtributeDialog(Entity ent) {
		JPanel left = new JPanel();
		left.setLayout(new BoxLayout(left, BoxLayout.Y_AXIS));
		left.add(Box.createVerticalGlue());
		lblAccess = new JLabel("Access Modifier:");
		left.add(lblAccess);
		left.add(Box.createVerticalGlue());
		tfAccess = new JTextField();
		left.add(tfAccess);
		left.add(Box.createVerticalGlue());
		lblName = new JLabel("Name of method:");
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
		JPanel right = new JPanel();
		right.setLayout(new BoxLayout(right, BoxLayout.Y_AXIS));
		right.add(Box.createVerticalGlue());
		btnOk = new JButton("Ok");
		
		
		btnOk.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				String name = tfAccess.getText();
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
				
				ent.getList().add(new Attributes(name, dt, type));
			}
		});		
		
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		JPanel up = new JPanel();
		up.setLayout(new GridLayout(1, 2));
		up.add(left);
		up.add(right);
		panel.add(up);
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
