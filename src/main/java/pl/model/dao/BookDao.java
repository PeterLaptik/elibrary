package pl.model.dao;

import java.io.Serializable;
import java.util.List;

import pl.model.entities.Book;
import pl.model.entities.Section;

public interface BookDao extends Serializable{
	
	public boolean createBook(Book book);
	
	public List<Book> findBooksByName(String name);
	
	public List<Book> getBooksBySection(Section section);
}
