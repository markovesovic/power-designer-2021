package actions;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.KeyStroke;

import classEditor.Window;

public class OpenUseCase extends MyAbstractAction{
	
	public OpenUseCase() {
		putValue(ACCELERATOR_KEY,KeyStroke.getKeyStroke(
		        KeyEvent.VK_R, ActionEvent.CTRL_MASK));
		putValue(SMALL_ICON, loadIcon("images/man.png"));
		putValue(NAME, "Use case diagram");
		putValue(SHORT_DESCRIPTION, "Use case diagram");
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		//MyTreeNode sel = (MyTreeNode) AppCore.getInstance().getTree().getLastSelectedPathComponent();
		//if(sel==null) return;
		Window window = new Window("abc"/*sel.getFile().getPath()*/);
		window.setVisible(true);
		
	}

}
