package pl.elibrary.model.dao;

import java.io.Serializable;
import java.util.List;

import pl.elibrary.model.entities.Book;
import pl.elibrary.model.entities.Section;

public interface BookDao extends Serializable {
	
	public boolean createBook(Book book);
	
	public void deleteBook(Book book);
	
	public Book getBookById(int id);
	
	public List<Book> findBooksByName(String name);
	
	public List<Book> getBooksBySection(Section section);
	
	public List<Book> getBooksBySectionId(int id);
	
	/** Paginated list.  windowCapacity - maximum books to show **/
	public List<Book> getBooksBySectionId(int sectionId, int windowCapacity, int pageNumber);
	
	public int getBooksQuantity();
	
	public int getBookQuantity(int sectionId);
	
	public void moveBookToSection(Book book, int sectionId);
	
	/** Search services **/
	public int searchByNameQuantity(String likeValue);
	
	public int searchByAuthorQuantity(String likeValue);
	
	public List<Book> searchByNameBooks(String likeValue, int windowCapacity, int pageNumber);
	
	public List<Book> searchByAuthorBooks(String likeValue, int windowCapacity, int pageNumber);
}
