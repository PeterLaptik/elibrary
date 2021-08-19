package pl.model.dao.impl;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import pl.model.dao.BookDao;
import pl.model.entities.Book;
import pl.model.entities.Section;
import pl.model.session.HibernateSessionFactory;

@Stateless
public class BookDaoImpl implements BookDao {
	private static final long serialVersionUID = 8219653706236670746L;

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
		
		Session session = HibernateSessionFactory.getSession().openSession();
		Query<Book> query = session.createQuery("FROM Book WHERE section_section_id = :param", Book.class);
		query.setParameter("param", section.getId());
		List<Book> books = query.list();
		session.close();
		return books;
	}

	@Override
	public List<Book> getBooksBySectionId(int id) {
		Session session = HibernateSessionFactory.getSession().openSession();
		Query<Book> query = session.createQuery("FROM Book WHERE section_section_id = :param", Book.class);
		query.setParameter("param", id);
		List<Book> books = query.list();
		session.close();
		return books;
	}
	
	@Override
	public void deleteBook(Book book) {
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
}
