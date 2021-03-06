package pl.elibrary.view.jsf.beans.objects;

/**
 * The class represents single section node
 */
public class SectionNode {
	private String name;
	private int id;

	public SectionNode(String name, int id) {
		this.name = name;
		this.id = id;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
}
