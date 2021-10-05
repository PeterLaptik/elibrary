package pl.elibrary.model.dao.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import pl.elibrary.model.dao.BookDao;
import pl.elibrary.model.dao.UserDao;
import pl.elibrary.model.dao.UserHistoryDao;
import pl.elibrary.model.entities.Book;
import pl.elibrary.model.entities.User;
import pl.elibrary.model.entities.UserHistory;
import pl.elibrary.model.entities.UserHistoryId;
import pl.elibrary.model.session.HibernateSessionFactory;

@Stateless
public class UserHistoryDaoImpl implements UserHistoryDao {
	private static final long serialVersionUID = 1697888100344639121L;
	
	@EJB
	UserDao userDao;
	
	@EJB
	BookDao bookDao;
	
	@Override
	public void createStamp(int bookId, int userId,  Integer maxRecords) {
		Session session = HibernateSessionFactory.getSession().openSession();
		Transaction transaction = session.beginTransaction();
		// Does a previous stamp exists
		Query<UserHistory> query = session.createQuery("FROM UserHistory WHERE user_id = :user_id AND book_id = :book_id", 
									UserHistory.class);
		query.setParameter("user_id", userId);
		query.setParameter("book_id", bookId);
		List<UserHistory> stamps = query.list();
		
		UserHistory userHistory = null;
		if(stamps.size()==0) {
			userHistory = new UserHistory();
			UserHistoryId uhid = new UserHistoryId(userId, bookId);
			userHistory.setId(uhid);
			User user = getUserById(userId, session);
			Book book = getBookById(bookId, session);
			userHistory.setUser(user);
			userHistory.setBook(book);
			session.save(userHistory);
			// Purge history
			if (maxRecords != null && maxRecords > 1)
				purgeHistory(userHistory, session, maxRecords - 1);
		} else {
			UserHistory existingStamp = stamps.get(0);
			existingStamp.setLastOpenDate(new Date());
			session.update(existingStamp);
		}
		transaction.commit();
		session.close();
	}

	@Override
	public void updateStamp(UserHistory userHistory) {
		if(userHistory==null)
			return;
		
		int userId = userHistory.getUser().getId();
		int bookId = userHistory.getBook().getId();
		Session session = HibernateSessionFactory.getSession().openSession();
		Transaction transaction = session.beginTransaction();
		// Does a previous stamp exists
		Query<UserHistory> query = session.createQuery("FROM UserHistory WHERE user_id = :user_id AND book_id = :book_id", 
									UserHistory.class);
		query.setParameter("user_id", userId);
		query.setParameter("book_id", bookId);
		List<UserHistory> stamps = query.list();
		if(stamps.size()==0) {
			transaction.commit();
			session.close();
			return;
		}
		
		UserHistory existingStamp = stamps.get(0);
		existingStamp.setLastOpenDate(new Date());
		existingStamp.setPage(userHistory.getPage());
		session.update(existingStamp);
		transaction.commit();
		session.close();
	}
	
	@Override
	public int getLastPageFor(int bookId, int userId) {
		Session session = HibernateSessionFactory.getSession().openSession();
		Query<UserHistory> query = session.createQuery("FROM UserHistory WHERE user_id = :user_id AND book_id = :book_id", 
									UserHistory.class);
		query.setParameter("user_id", userId);
		query.setParameter("book_id", bookId);
		List<UserHistory> stamps = query.list();
		session.close();
		
		if(stamps.size()==0)
			return 0;

		return stamps.get(0).getPage();
	}
	
	private void purgeHistory(UserHistory userHistory, Session session, int maxRecords) {
		Query<?> query = session.createNativeQuery(
				"DELETE FROM users_history\r\n"
				+ "WHERE ctid NOT IN\r\n"
				+ "(SELECT\r\n"
				+ "	ctid\r\n"
				+ "FROM users_history\r\n"
				+ "WHERE user_id = :user_id\r\n"
				+ "ORDER BY date_opened DESC\r\n"
				+ "LIMIT :limit);");
		query.setParameter("user_id", userHistory.getUser().getId());
		query.setParameter("limit", maxRecords);
		query.executeUpdate();
	}

	@Override
	public List<Book> getHistoryBooks(int userId) {
		List<Book> books = new ArrayList<Book>();
		Session session = HibernateSessionFactory.getSession().openSession();
		Query<UserHistory> query = session.createQuery("FROM UserHistory WHERE user_id = :user_id ORDER BY date_opened DESC", 
									UserHistory.class);
		query.setParameter("user_id", userId);
		List<UserHistory> historyLines = query.list();
		for(UserHistory i: historyLines)
			books.add(i.getBook());
		
		session.close();
		return books;
	}

	@Override
	public List<UserHistory> getHistory(int userId) {
		Session session = HibernateSessionFactory.getSession().openSession();
		Query<UserHistory> query = session.createQuery("FROM UserHistory WHERE user_id = :user_id ORDER BY date_opened DESC", 
									UserHistory.class);
		query.setParameter("user_id", userId);
		List<UserHistory> historyLines = query.list();
		session.close();
		return historyLines;
	}
	
	@Override
	public void deleteStamp(UserHistory userHistory) {
		if(userHistory==null)
			return;
		
		Session session = HibernateSessionFactory.getSession().openSession();
		Transaction transaction = session.beginTransaction();
		
		try {
			session.delete(userHistory);
		} catch (Exception e) {
			e.printStackTrace();
		}

		transaction.commit();
		session.close();
		
	}

	@Override
	public void deleteStampsForBook(int bookId) {
		Session session = HibernateSessionFactory.getSession().openSession();
		Transaction transaction = session.beginTransaction();
		Query<?> query = session.createNativeQuery(
				"DELETE FROM users_history WHERE book_id = :book_id");
		query.setParameter("book_id", bookId);
		query.executeUpdate();
		transaction.commit();
		session.close();
	}

	@Override
	public void deleteStampsForUser(int userId) {
		Session session = HibernateSessionFactory.getSession().openSession();
		Transaction transaction = session.beginTransaction();
		Query<?> query = session.createNativeQuery(
				"DELETE FROM users_history WHERE user_id = :user_id");
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
