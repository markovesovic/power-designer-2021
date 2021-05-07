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

//import org.json.JSONException;
//import org.json.JSONObject;


@SuppressWarnings("serial")
public class NewAtributeDialog extends JDialog{
//	private JSONObject attr;
	
	private JLabel lblName;
	private JTextField tfName;
	private JLabel lblDatatype;
	private JTextField tfDatatype;
	private JLabel lblLength;
	private JTextField tfLength;
	private JRadioButton rbMandatory;
	private JRadioButton rbPK;
	private JLabel lblDefault;
	private JTextField tfDefault;
	private JButton btnOk;
	
	public NewAtributeDialog() {
		JPanel left = new JPanel();
		left.setLayout(new BoxLayout(left, BoxLayout.Y_AXIS));
		left.add(Box.createVerticalGlue());
		lblName = new JLabel("Name:");
		left.add(lblName);
		left.add(Box.createVerticalGlue());
		tfName = new JTextField();
		left.add(tfName);
		left.add(Box.createVerticalGlue());
		lblDatatype = new JLabel("Datatype:");
		left.add(lblDatatype);
		left.add(Box.createVerticalGlue());
		tfDatatype = new JTextField();
		left.add(tfDatatype);
		left.add(Box.createVerticalGlue());
		lblLength = new JLabel("Lenght:");
		left.add(lblLength);
		tfLength = new JTextField();
		left.add(tfLength);
		left.add(Box.createVerticalGlue());
		left.add(Box.createVerticalGlue());
		JPanel right = new JPanel();
		right.setLayout(new BoxLayout(right, BoxLayout.Y_AXIS));
		right.add(Box.createVerticalGlue());
		rbMandatory = new JRadioButton("Mandatory");
		right.add(rbMandatory);
		right.add(Box.createVerticalGlue());
		rbPK = new JRadioButton("Primary key");
		right.add(rbPK);
		right.add(Box.createVerticalGlue());
		lblDefault = new JLabel("Default value:");
		right.add(lblDefault);
		right.add(Box.createVerticalGlue());
		tfDefault = new JTextField();
		right.add(tfDefault);
		right.add(Box.createVerticalGlue());
		btnOk = new JButton("Ok");
		
		rbPK.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				if(rbPK.isSelected())
				rbMandatory.setSelected(true);
			}
		});
		
		btnOk.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				String name = tfName.getText();
				if(name.isEmpty()) {
					showMessage("Name");
					return;
				}
				String dt = tfDatatype.getText();
				if(dt.isEmpty()) {
					showMessage("Datatype");
					return;
				}
				int length = 0;
				if(!tfLength.getText().isEmpty())
					Integer.parseInt(tfLength.getText());
				boolean mand = rbMandatory.isSelected();
				boolean pk = rbPK.isSelected();
				String def = tfDefault.getText();
				if(def.isEmpty()) def="null";
				
//				try {
//					attr = new JSONObject().put("name", name)
//							.put("datatype", dt)
//							.put("length", length)
//							.put("mandatory", mand)
//							.put("pk", pk)
//							.put("default", def);
//					dispose();
//				} catch (JSONException e1) {
//					e1.printStackTrace();
//				}
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
