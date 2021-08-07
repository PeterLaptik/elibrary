package pl.model.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table (name="chapter_children")
public class ChapterChildren {
	public final static String FIELD_PARENT = "parent_id";
	public final static String FIELD_CHILD = "child_id";
	
	@Id
	@Column(name=FIELD_PARENT)
	private int parent;

	@Column(name=FIELD_CHILD)
	private int child;

	public ChapterChildren() {
		
	}
	
	public int getParent() {
		return parent;
	}
	
	public void setParent(int parent) {
		this.parent = parent;
	}
	
	public int getChild() {
		return child;
	}
	
	public void setChildren(int child) {
		this.child = child;
	}
}
