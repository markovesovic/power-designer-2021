package app;

import java.awt.Dimension;

import view.frames.MainFrame;

public class Main {

	public static void main(String[] args) {
		initializeApp();
	}

	private static void initializeApp() {
		AppCore.initializeAppCore();
		AppCore.getInstance().setMainFrame(new MainFrame(new Dimension(70, 70)));
	}

}
