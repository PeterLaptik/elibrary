package pl.model.dao.impl;

import java.util.List;

import javax.ejb.Stateless;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import pl.model.dao.UserSessionDao;
import pl.model.entities.User;
import pl.model.entities.UserSession;
import pl.model.session.HibernateSessionFactory;

@Stateless
public class UserSessionDaoImpl implements UserSessionDao {
	
	public UserSessionDaoImpl(){
		
	}

	public void create(UserSession userSession) {
		Session session = HibernateSessionFactory.getSession().openSession();
		Transaction transaction = session.beginTransaction();
		session.save(userSession);
		transaction.commit();
		session.close();
	}
	
	public UserSession findSessionByUuid(String uuid) {
		Session session = HibernateSessionFactory.getSession().openSession();
		Query<UserSession> query = session.createQuery("FROM UserSession WHERE session_uuid = :param", UserSession.class);
		query.setParameter("param", uuid);
		List<UserSession> sessions = query.list();
		session.close();
		return sessions.size()>0 ? sessions.get(0) : null;
	}
	
	public UserSession findSessionByUserId(int userId) {
		Session session = HibernateSessionFactory.getSession().openSession();
		Query<UserSession> query = session.createQuery("FROM UserSession WHERE user_id = :param", UserSession.class);
		query.setParameter("param", userId);
		List<UserSession> sessions = query.list();
		session.close();
		return sessions.size()>0 ? sessions.get(0) : null;
	}
	
	public void deleteSessionByUuid(String uuid) {
		Session session = HibernateSessionFactory.getSession().openSession();
		Query<UserSession> query = session.createQuery("FROM UserSession WHERE session_uuid = :param", UserSession.class);
		query.setParameter("param", uuid);
		List<UserSession> sessions = query.list();
		if(sessions.size()<1)
			return;
		
		int userId = sessions.get(0).getUserId();
		Query<UserSession> sessionsToDelete = session.createQuery("FROM UserSession WHERE user_id = :param", UserSession.class);
		sessionsToDelete.setParameter("param", userId);
		List<UserSession> removeList = sessionsToDelete.list();
		Transaction t = session.beginTransaction();
		for(UserSession i: removeList) {
			session.delete(i);
		}
		t.commit();
		session.close();
	}
}
