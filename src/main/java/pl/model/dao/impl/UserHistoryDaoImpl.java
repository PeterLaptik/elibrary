package pl.model.dao.impl;

import java.util.Date;
import java.util.List;

import javax.ejb.Stateless;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import pl.model.dao.UserHistoryDao;
import pl.model.entities.UserHistory;
import pl.model.session.HibernateSessionFactory;

@Stateless
public class UserHistoryDaoImpl implements UserHistoryDao {
	@Override
	public void createStamp(UserHistory userHistory) {
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
			session.save(userHistory);
		} else {
			UserHistory existingStamp = stamps.get(0);
			existingStamp.setLastOpenDate(new Date());
			session.update(existingStamp);
		}
		transaction.commit();
		session.close();
	}
}
