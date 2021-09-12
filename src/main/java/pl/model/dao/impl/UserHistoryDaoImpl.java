package pl.model.dao.impl;

import java.util.Date;
import java.util.List;

import javax.ejb.Stateless;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import pl.model.dao.UserHistoryDao;
import pl.model.entities.UserHistory;
import pl.model.entities.UserHistoryId;
import pl.model.session.HibernateSessionFactory;

@Stateless
public class UserHistoryDaoImpl implements UserHistoryDao {
	
	@Override
	public void createStamp(UserHistory userHistory,  Integer maxRecords) {
		if(userHistory==null)
			return;
		
		int userId = userHistory.getUser().getId();
		int bookId = userHistory.getBook().getId();
		userHistory.setId(new UserHistoryId(userId, bookId));
		Session session = HibernateSessionFactory.getSession().openSession();
		Transaction transaction = session.beginTransaction();
		// Does a previous stamp exists
		Query<UserHistory> query = session.createQuery("FROM UserHistory WHERE user_id = :user_id AND book_id = :book_id", 
									UserHistory.class);
		query.setParameter("user_id", userId);
		query.setParameter("book_id", bookId);
		List<UserHistory> stamps = query.list();
		if(stamps.size()==0) {
			session.save(userHistory);
			if(maxRecords!=null && maxRecords>1)
				cleanHistory(userHistory, maxRecords-1, session);
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
	
	private void cleanHistory(UserHistory userHistory, int maxRecords, Session session) {
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
}
