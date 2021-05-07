package actions;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.KeyStroke;

import app.AppCore;
import classEditor.Window;

@SuppressWarnings("serial")
public class OpenClassDiagram extends MyAbstractAction{
	
	public OpenClassDiagram() {
		putValue(ACCELERATOR_KEY,KeyStroke.getKeyStroke(
		        KeyEvent.VK_R, ActionEvent.CTRL_MASK));
		putValue(SMALL_ICON, loadIcon("images/editor.png"));
		putValue(NAME, "Class diagram");
		putValue(SHORT_DESCRIPTION, "Class diagram");
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		//MyTreeNode sel = (MyTreeNode) AppCore.getInstance().getTree().getLastSelectedPathComponent();
		//if(sel==null) return;
		Window window = new Window("abc"/*sel.getFile().getPath()*/);
		window.setVisible(true);
		
	}

}
