package pl.model.dao.views;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.view.ViewScoped;
import javax.inject.Named;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.ManyToOne;

import pl.model.entities.Book;
import pl.model.entities.Section;

@Named("bookService")
@ViewScoped
public class BookService implements Serializable{
	private static final long serialVersionUID = -6832219154544980243L;
	List<Book> books = new ArrayList<Book>();
	Section selectedSection = null;
	
	private String name;
	private String description;
	private String format;
	private String fileName;
	private Section section;

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
	
	public String getFileName() {
		return fileName;
	}
	
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	
	public Section getSection() {
		return section;
	}
	
	public void setSection(Section section) {
		this.section = section;
	}
}
