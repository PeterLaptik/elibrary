package pl.elibrary.model.dao.impl;

import java.util.List;

import javax.ejb.Stateless;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import pl.elibrary.model.dao.BookmarkDao;
import pl.elibrary.model.entities.Book;
import pl.elibrary.model.entities.Bookmark;
import pl.elibrary.model.entities.BookmarkId;
import pl.elibrary.model.entities.User;
import pl.elibrary.model.session.HibernateSessionFactory;

@Stateless
public class BookmarkDaoImpl implements BookmarkDao {
	private static final long serialVersionUID = -3755751197696808384L;

	@Override
	public void createBookmark(Bookmark bookmark, int bookId, int userId) {
		if(bookmark==null)
			return;

		bookmark.setId(new BookmarkId(userId, bookId, bookmark.getName()));
		Session session = HibernateSessionFactory.getSession().openSession();
		Transaction transaction = session.beginTransaction();
		// Does a previous stamp exists
		Query<Bookmark> query = session.createQuery("FROM Bookmark WHERE user_id = :user_id AND book_id = :book_id AND bookmark_text=:text", 
				Bookmark.class);
		query.setParameter("user_id", userId);
		query.setParameter("book_id", bookId);
		query.setParameter("text", bookmark.getName());
		
		List<Bookmark> bookmarks = query.list();
		if(bookmarks.size()==0) {
			User user = getUserById(userId, session);
			Book book = getBookById(bookId, session);
			bookmark.setUser(user);
			bookmark.setBook(book);
			session.save(bookmark);
		} else {
			Bookmark existingBookmark = bookmarks.get(0);
			existingBookmark.setPage(bookmark.getPage());
			session.update(existingBookmark);
		}
		transaction.commit();
		session.close();
	}

	@Override
	public void deleteBookmark(Bookmark bookmark) {
		if(bookmark==null)
			return;
		
		int userId = bookmark.getUser().getId();
		int bookId = bookmark.getBook().getId();
		bookmark.setId(new BookmarkId(userId, bookId, bookmark.getName()));
		Session session = HibernateSessionFactory.getSession().openSession();
		Transaction transaction = session.beginTransaction();
		// Does a previous stamp exists
		Query<Bookmark> query = session.createQuery("FROM Bookmark WHERE user_id = :user_id AND book_id = :book_id AND bookmark_text=:text", 
				Bookmark.class);
		query.setParameter("user_id", userId);
		query.setParameter("book_id", bookId);
		query.setParameter("text", bookmark.getName());
		
		List<Bookmark> bookmarks = query.list();
		if(bookmarks.size()!=0) {
			for(Bookmark i: bookmarks)
				session.delete(i);
		}
		transaction.commit();
		session.close();
	}

	@Override
	public List<Bookmark> getBookmarks(int bookId, int userId) {
		Session session = HibernateSessionFactory.getSession().openSession();
		// Does a previous stamp exists
		Query<Bookmark> query = session.createQuery("FROM Bookmark WHERE user_id = :user_id AND book_id = :book_id", 
				Bookmark.class);
		query.setParameter("user_id", userId);
		query.setParameter("book_id", bookId);
		List<Bookmark> bookmarks = query.list();
		session.close();
		return bookmarks;
	}

	@Override
	public void deleteBookmarksForBook(int bookId) {
		Session session = HibernateSessionFactory.getSession().openSession();
		Transaction transaction = session.beginTransaction();
		Query<?> query = session.createNativeQuery(
				"DELETE FROM bookmarks WHERE book_id = :book_id");
		query.setParameter("book_id", bookId);
		query.executeUpdate();
		transaction.commit();
		session.close();
	}

	@Override
	public void deleteBookmarksForUser(int userId) {
		Session session = HibernateSessionFactory.getSession().openSession();
		Transaction transaction = session.beginTransaction();
		Query<?> query = session.createNativeQuery(
				"DELETE FROM bookmarks WHERE user_id = :user_id");
		query.setParameter("user_id", userId);
		query.executeUpdate();
		transaction.commit();
		session.close();
	}
	
	private User getUserById(int userId, Session session) {
		Query<User> query = session.createQuery("FROM User WHERE id = :param", User.class);
		query.setParameter("param", userId);
		List<User> users = query.list();
		return users.size()>0 ? users.get(0) : null;
	}
	
	private Book getBookById(int bookId, Session session) {
		Book book = null;
		Query<Book> query = session.createQuery("FROM Book WHERE id = :param", Book.class);
		query.setParameter("param", bookId);
		try {
			book = query.getSingleResult();
		} catch (Exception e) {
			System.err.println("Error: book not found. book_id=" + bookId);
		}
		return book;
	}
}
