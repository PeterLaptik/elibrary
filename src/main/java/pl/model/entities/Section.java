package pl.model.entities;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table (name="chapters")
public class Chapter {
	public static final String FIELD_ID = "chapter_id";
	public static final String FIELD_NAME = "chapter_name";
	public static final String FIELD_PARENT = "parent_id";
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	@Column(name=FIELD_ID)
	private int id;

	@Column(name=FIELD_NAME)
	private String name;
	
	@Column(name=FIELD_PARENT)
	private String parentId;
	
	@OneToMany(cascade=CascadeType.ALL)
	private List<Chapter> children;

	
	public Chapter(){
		
	}
	
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getParentId() {
		return parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}
	
	public List<Chapter> getChildren() {
		return children;
	}

	public void setChildren(List<Chapter> children) {
		this.children = children;
	}
}
