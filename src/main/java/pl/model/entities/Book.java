package pl.model.entities;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.OneToOne;

public class Book implements Serializable {
	private int id;
	private String name;
	private String description;
	private String format;
	private String fileName;

	@OneToOne(cascade=CascadeType.ALL)
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
}
