package pl.model.dao.impl;

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
		Session session = HibernateSessionFactory.getSession().openSession();
		Query<Book> query = session.createQuery("FROM Book WHERE book_name = :param", Book.class);
		query.setParameter("param", name);
		List<Book> books = query.list();
		session.close();
		return books;
	}
	
	@Override
	public List<Book> getBooksBySection(Section section) {
		Session session = HibernateSessionFactory.getSession().openSession();
		Query<Book> query = session.createQuery("FROM Book WHERE section_section_id = :param", Book.class);
		query.setParameter("param", section.getId());
		List<Book> books = query.list();
		session.close();
		return books;
	}
}
