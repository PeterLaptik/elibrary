package pl.model.dao.impl;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.NativeQuery;
import org.hibernate.query.Query;

import pl.model.dao.BookDao;
import pl.model.dao.BookmarkDao;
import pl.model.dao.UserHistoryDao;
import pl.model.entities.Book;
import pl.model.entities.Section;
import pl.model.session.HibernateSessionFactory;

@Stateless
public class BookDaoImpl implements BookDao {
	private static final long serialVersionUID = 8219653706236670746L;
	
	@EJB
	private UserHistoryDao historyDao;
	
	@EJB
	private BookmarkDao bookmarkDao;

	@Override
	public boolean createBook(Book book) {
		if((book==null)||(book.getSection()==null))
			return false;

		Session session = HibernateSessionFactory.getSession().openSession();
		Transaction transaction = session.beginTransaction();
		session.save(book);
		transaction.commit();
		session.close();
		return true;
	}

	@Override
	public void deleteBook(Book book) {
		historyDao.deleteStampsForBook(book.getId());
		bookmarkDao.deleteBookmarksForBook(book.getId());
		Session session = HibernateSessionFactory.getSession().openSession();
		Transaction transaction = session.beginTransaction();
		Query<Book> query = session.createQuery("FROM Book WHERE id = :param", Book.class);
		query.setParameter("param", book.getId());
		List<Book> books = query.list();
		if(books.size()>0)
			session.delete(books.get(0));
		transaction.commit();
		session.close();
	}

	@Override
	public void moveBookToSection(Book book, int sectionId) {
		int id = book.getId();
		
		book = null;
		Section section = null;
		Session session = HibernateSessionFactory.getSession().openSession();
		Transaction transaction = session.beginTransaction();
		
		Query<Book> bQuery = session.createQuery("FROM Book WHERE id = :param", Book.class);
		bQuery.setParameter("param", id);
		List<Book> books = bQuery.list();
		if(books.size()>0)
			book = books.get(0);
		
		Query<Section> sQuery = session.createQuery("FROM Section WHERE id = :param", Section.class);
		sQuery.setParameter("param", sectionId);
		List<Section> sections = sQuery.list();
		if(sections.size()>0)
			section = sections.get(0);
			
		if(book!=null && section!=null)
			book.setSection(section);
		
		transaction.commit();
		session.close();
	}
	
	@Override
	public Book getBookById(int id) {
		Book book = null;
		Session session = HibernateSessionFactory.getSession().openSession();
		Query<Book> query = session.createQuery("FROM Book WHERE id = :param", Book.class);
		query.setParameter("param", id);
		try {
			book = query.getSingleResult();
		} catch (Exception e) {
			System.err.println("Error: book not found. book_id=" + id);
		}
		session.close();
		return book;
	}
	
	@Override
	public List<Book> findBooksByName(String name) {
		if(name==null)
			return new ArrayList<Book>();
		
		Session session = HibernateSessionFactory.getSession().openSession();
		Query<Book> query = session.createQuery("FROM Book WHERE book_name = :param", Book.class);
		query.setParameter("param", name);
		List<Book> books = query.list();
		session.close();
		return books;
	}
	
	@Override
	public List<Book> getBooksBySection(Section section) {
		if(section==null)
			return new ArrayList<Book>();

		return getBooksBySectionId(section.getId());
	}

	@Override
	public List<Book> getBooksBySectionId(int id) {
		Session session = HibernateSessionFactory.getSession().openSession();
		Query<Book> query = session.createQuery("FROM Book WHERE section_section_id = :param ORDER BY book_name", Book.class);
		query.setParameter("param", id);
		List<Book> books = query.list();
		session.close();
		return books;
	}
	
	@Override
	public List<Book> getBooksBySectionId(int sectionId, int windowCapacity, int pageNumber) {
		Session session = HibernateSessionFactory.getSession().openSession();
		Query<Book> query = session.createQuery("FROM Book WHERE section_section_id = :id ORDER BY book_name", Book.class)
				.setFirstResult(windowCapacity*pageNumber)
				.setMaxResults(windowCapacity);
		query.setParameter("id", sectionId);
		List<Book> books = query.list();
		session.close();
		return books;
	}
	
	@Override
	public int getBooksQuantity() {
		int result = 0;
		Session session = HibernateSessionFactory.getSession().openSession();
		
		@SuppressWarnings("unchecked")
		List<BigInteger> counter = session.createSQLQuery("SELECT count(*) FROM books").list();
		if(counter.size()>0) {
			try {
				result = counter.get(0).intValue();
			} catch (Exception e) {
				// Could not count
				// do nothing (zero-value returns)
			}
		}
		session.close();
		return result;
	}

	@Override
	public int getBookQuantity(int sectionId) {
		int result = 0;
		Session session = HibernateSessionFactory.getSession().openSession();
		
		NativeQuery<?> nquery = session.createSQLQuery("SELECT count(*) FROM books WHERE section_section_id=:param");
		nquery.setParameter("param", sectionId);
		@SuppressWarnings("unchecked")
		List<BigInteger> counter = (List<BigInteger>) nquery.list();
		
		if(counter.size()>0) {
			try {
				result = counter.get(0).intValue();
			} catch (Exception e) {
				// Could not count
				// do nothing (zero-value returns)
			}
		}
		session.close();
		return result;
	}

	@Override
	public int searchByNameQuantity(String likeValue) {
		int result = 0;
		Session session = HibernateSessionFactory.getSession().openSession();
		
		NativeQuery<?> nquery = session.createSQLQuery("SELECT count(*) FROM books WHERE book_name like :param");
		nquery.setParameter("param", '%' + likeValue + '%');
		
		@SuppressWarnings("unchecked")
		List<BigInteger> counter = (List<BigInteger>) nquery.list();
		if(counter.size()>0) {
			try {
				result = counter.get(0).intValue();
			} catch (Exception e) {
				// Could not count
				// do nothing (zero-value returns)
			}
		}
		session.close();
		return result;
	}

	@Override
	public int searchByAuthorQuantity(String likeValue) {
		int result = 0;
		Session session = HibernateSessionFactory.getSession().openSession();
		
		NativeQuery<?> nquery = session.createSQLQuery("SELECT count(*) FROM books WHERE authors like :param");
		nquery.setParameter("param", '%' + likeValue + '%');
		
		@SuppressWarnings("unchecked")
		List<BigInteger> counter = (List<BigInteger>) nquery.list();
		if(counter.size()>0) {
			try {
				result = counter.get(0).intValue();
			} catch (Exception e) {
				// Could not count
				// do nothing (zero-value returns)
			}
		}
		session.close();
		return result;
	}

	@Override
	public List<Book> searchByNameBooks(String likeValue, int windowCapacity, int pageNumber) {
		Session session = HibernateSessionFactory.getSession().openSession();
		Query<Book> query = session.createQuery("FROM Book WHERE book_name like :param ORDER BY book_name", Book.class)
				.setFirstResult(windowCapacity*pageNumber)
				.setMaxResults(windowCapacity);
		query.setParameter("param", '%' + likeValue + '%');
		List<Book> books = query.list();
		session.close();
		return books;
	}

	@Override
	public List<Book> searchByAuthorBooks(String likeValue, int windowCapacity, int pageNumber) {
		Session session = HibernateSessionFactory.getSession().openSession();
		Query<Book> query = session.createQuery("FROM Book WHERE authors like :param ORDER BY authors", Book.class)
				.setFirstResult(windowCapacity*pageNumber)
				.setMaxResults(windowCapacity);
		query.setParameter("param", '%' + likeValue + '%');
		List<Book> books = query.list();
		session.close();
		return books;
	}
}
