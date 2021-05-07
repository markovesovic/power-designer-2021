package app;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;

//import actions.ActionManager;
//import controller.ExceptionManager;
//import view.entitySlog.AddEntitySlogFrame;
import view.frames.MainFrame;

public class AppCore {
	private static AppCore instance;
	
	private final File metaMetaSchemaFile = new File("meta_meta_sema.json");
	
	private MainFrame mainFrame;
//	private AddEntitySlogFrame addEntitySlogFrame;
//	private ActionManager actionManager;
//	private ExceptionManager exceptionManager;
	
	private Connection connection;
	
	private AppCore() {
//		actionManager = new ActionManager();
//		exceptionManager = new ExceptionManager();
	}
	
	public static boolean initializeAppCore() {
		if (instance == null) {
			instance = new AppCore();
			return true;
		}
		else return false;
	}
	
	public void connectToDatabase(String serverName, String databaseName, String userName, String password) {
		try {
			Class.forName("net.sourceforge.jtds.jdbc.Driver");
			connection = DriverManager.getConnection(
					 "jdbc:jtds:sqlserver://"+serverName+"/"+databaseName,userName,password);
		}
		catch (Exception e) {
			System.out.println("Povezivanje sa bazom nije uspelo");
		}
	}
	
	public Connection getConnection() {
		return connection;
	}
	
//	public Tree getTree() {
//		return mainFrame.getCenterFrame().getTreePanel().getTree();
//	}
	
	public MainFrame getMainFrame() {
		return mainFrame;
	}
	
//	public TabView getTabView() {
//		return mainFrame.getCenterFrame().getRightPanel().getTabView();
//	}
	
	public void setMainFrame(MainFrame mainFrame) {
		this.mainFrame = mainFrame;
	}
	
	public File getMetametaschemafile() {
		return metaMetaSchemaFile;
	}
	
	public static AppCore getInstance() {
		return instance;
	}

//	public ActionManager getActionManager() {
//		return actionManager;
//	}
//
//	public ExceptionManager getExceptionManager() {
//		return exceptionManager;
//	}
//
//	public void setAddEntitySlogFrame(AddEntitySlogFrame addEntitySlogFrame) {
//		this.addEntitySlogFrame = addEntitySlogFrame;
//	}
//
//	public AddEntitySlogFrame getAddEntitySlogFrame() {
//		return addEntitySlogFrame;
//	}
}
