package view;

import javax.swing.Box;
import javax.swing.JToolBar;

import actions.ActionManager;

@SuppressWarnings("serial")
public class ToolBar extends JToolBar {

	public ToolBar() {
		dodaj();
	}

	private void dodaj() {
		add(Box.createHorizontalGlue());
		add(ActionManager.getInstance().getOpenEditor());
		add(ActionManager.getInstance().getOpenUseCase());
		add(Box.createHorizontalGlue());
	}

}
