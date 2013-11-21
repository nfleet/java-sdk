package fi.cosky.sdk;

public class EntityLink extends BaseData {
	private int Id;
	private String Name;
		
	public int getId() {
		return Id;
	}
	public void setId(int id) {
		Id = id;
	}
	public String getName() {
		return Name;
	}
	public void setName(String name) {
		Name = name;
	}
}
