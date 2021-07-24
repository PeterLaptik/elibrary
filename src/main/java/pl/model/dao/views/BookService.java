package pl.model.dao.views;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.view.ViewScoped;
import javax.inject.Named;

import pl.model.entities.Book;
import pl.model.entities.Section;

@Named("bookService")
@ViewScoped
public class BookService implements Serializable{
	private static final long serialVersionUID = -6832219154544980243L;
	List<Book> books = new ArrayList<Book>();
	Section selectedSection = null;

	
	public Section getSelectedSection() {
		return selectedSection;
	}

	public void setSelectedSection(Section selectedSection) {
		this.selectedSection = selectedSection;
	}

	public List<Book> getBooks() {
		return books;
	}

	public void setBooks(List<Book> books) {
		this.books = books;
	}
}
