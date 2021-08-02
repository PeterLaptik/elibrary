package pl.model.entities;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table (name="books")
public class Book implements Serializable {
	private static final long serialVersionUID = -2333712729066071996L;
	public static final String FIELD_ID = "id";
	public static final String FIELD_NAME = "book_name";
	public static final String FIELD_AUTHORS = "authors";
	public static final String FIELD_CODE = "book_code";
	public static final String FIELD_MAGAZINE = "magazine";
	public static final String FIELD_DESCR = "book_description";
	public static final String FIELD_FILE_FORMAT = "file_format";
	public static final String FIELD_FILE_NAME = "file_name";
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	@Column(name=FIELD_ID)
	private int id;
	
	@Column(name=FIELD_NAME)
	private String name = "";
	
	@Column(name=FIELD_DESCR)
	private String description = "";
	
	@Column(name=FIELD_FILE_FORMAT)
	private String format = "";
	
	@Column(name=FIELD_FILE_NAME)
	private String fileName = "";
	
	/** Denormalized **/
	@Column(name=FIELD_AUTHORS)
	private String authors = "";
	
	@Column(name=FIELD_CODE)
	private String code = "";
	
	/** Attribute is only for articles: 
	 * name - article name, code - issue **/
	@Column(name=FIELD_MAGAZINE)
	private String magazine = "";

	@ManyToOne(cascade=CascadeType.DETACH)
	private Section section;

	public Book() {
		
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

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getFormat() {
		return format;
	}

	public void setFormat(String format) {
		this.format = format;
	}
	
	public Section getSection() {
		return section;
	}

	public void setSection(Section section) {
		this.section = section;
	}
	
	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	
	public String getAuthors() {
		return authors;
	}

	public void setAuthors(String authors) {
		this.authors = authors;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getMagazine() {
		return magazine;
	}

	public void setMagazine(String magazine) {
		this.magazine = magazine;
	}
}
