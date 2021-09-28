package pl.model.entities;

import java.io.Serializable;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

@Entity
@Table 
(name="sections",
uniqueConstraints = @UniqueConstraint(columnNames={"section_name"}, name="uk_section_name"))
public class Section implements Serializable{
	private static final long serialVersionUID = -6957062912046512337L;
	public static final String FIELD_ID = "section_id";
	public static final String FIELD_NAME = "section_name";
	public static final String FIELD_PARENT = "parent_id";
	public static final String ROOT_NAME = "root";
	
	@Id
	@SequenceGenerator(name = "section_seq",
				sequenceName = "SEQ_SECTION",
				allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE,
					generator = "section_seq")
	@Column(name=FIELD_ID)
	private int id;

	@Column(name=FIELD_NAME)
	private String name;
	
	@OneToOne(cascade=CascadeType.DETACH)
	@JoinColumn(foreignKey=@ForeignKey(name="fk_parent_node_id"))
	private Section parent;

	@OneToMany(cascade=CascadeType.ALL, mappedBy="parent")
	private List<Section> children;

	
	public Section(){
		
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
	
	public List<Section> getChildren() {
		return children;
	}

	public void setChildren(List<Section> children) {
		this.children = children;
	}
	
	public Section getParent() {
		return parent;
	}

	public void setParent(Section parent) {
		this.parent = parent;
	}
}
