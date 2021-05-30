abstract class DomesticAnimal implements Animal {
	public String tmp;

	public DomesticAnimal(String tmp) {
		this.tmp = tmp;
	}

	public abstract String getName();
	public abstract void setName();
	protected abstract String toString();
}