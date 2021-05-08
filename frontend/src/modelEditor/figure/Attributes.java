package modelEditor.figure;

public class Attributes {
	
	private String accessModifiers;
	private String name;
	private String type;
	private boolean function;
	
	public Attributes(String accessModifiers, String name, String type, boolean function) {
		super();
		this.accessModifiers = accessModifiers;
		this.name = name;
		this.type = type;
		this.function = function;
	}
	public String getAccessModifiers() {
		return accessModifiers;
	}

	public boolean isFunction() {
		return function;
	}

	public void setAccessModifiers(String accessModifiers) {
		this.accessModifiers = accessModifiers;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	
	

}
