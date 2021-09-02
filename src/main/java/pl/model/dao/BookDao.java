package pl.model.dao;

import java.io.Serializable;
import java.util.List;

import pl.model.entities.Book;
import pl.model.entities.Section;

public interface BookDao extends Serializable{
	
	public boolean createBook(Book book);
	
	public void deleteBook(Book book);
	
	public Book getBookById(int id);
	
	public List<Book> findBooksByName(String name);
	
	public List<Book> getBooksBySection(Section section);
	
	public List<Book> getBooksBySectionId(int id);
	
	/** Paginated list **/
	public List<Book> getBooksBySectionId(int sectionId, int windowCapacity, int pageNumber);
	
	public int getBooksQuantity();
	
	public int getBookQuantity(int sectionId);
	
	public void moveBookToSection(Book book, int sectionId);
}
