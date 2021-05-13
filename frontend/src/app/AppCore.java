package app;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;

//import actions.ActionManager;
//import controller.ExceptionManager;
//import view.entitySlog.AddEntitySlogFrame;
import view.frames.MainFrame;

public class AppCore {
	public static final String BACKEND_URL = "http://localhost:5000/api/v1/";
	public static final String PROJECT_URL = "fc6c8379-91f3-4d39-aa44-bb1928af9a89";
	public static final String USER_ID = "2e180f00-52c8-4c49-bf25-86aab8d177cd";
	public static String CLASS_MODEL_ID = "";
	public static String USE_CASE_MODEL_ID = "";

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
