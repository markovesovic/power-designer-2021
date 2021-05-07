package actions;


public class ActionManager {
	
	private static ActionManager instance = null;
	OpenClassDiagram openEditor;
	OpenUseCase openUseCase;
	
	
	public ActionManager() {
		openEditor = new OpenClassDiagram();
		openUseCase = new OpenUseCase();
		
	}
	
	
	public OpenClassDiagram getOpenEditor() {
		return openEditor;
	}
	
	public OpenUseCase getOpenUseCase() {
		return openUseCase;
	}
	
	
	
	public static ActionManager getInstance() {
		if (instance == null) instance = new ActionManager();
		return instance;
	}
	
}
