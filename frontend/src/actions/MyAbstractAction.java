package actions;

import java.awt.Image;

import javax.swing.AbstractAction;
import javax.swing.Icon;
import javax.swing.ImageIcon;

@SuppressWarnings("serial")
public abstract class MyAbstractAction extends AbstractAction{
	
	public Icon loadIcon(String fileName){
		Icon icon = new ImageIcon(((new ImageIcon(fileName)).getImage().getScaledInstance(24, 24, Image.SCALE_SMOOTH)));
		return icon;
	}
}
