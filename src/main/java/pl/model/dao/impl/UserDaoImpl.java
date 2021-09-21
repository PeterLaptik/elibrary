package pl.model.dao.impl;

import java.math.BigInteger;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import pl.credentials.PasswordProcessor;
import pl.model.dao.BookmarkDao;
import pl.model.dao.UserDao;
import pl.model.dao.UserHistoryDao;
import pl.model.entities.User;
import pl.model.session.HibernateSessionFactory;

@Stateless
public class UserDaoImpl implements UserDao {
	@EJB
	private UserHistoryDao historyDao;
	
	@EJB
	private BookmarkDao bookmarkDao;
	
	@Override
	public boolean create(User user) {
		if((user==null)||(findUserByLogin(user.getLogin())!=null))
			return false;
		
		createHashes(user);
		Session session = HibernateSessionFactory.getSession().openSession();
		Transaction transaction = session.beginTransaction();
		session.save(user);
		transaction.commit();
		session.close();
		return true;
	}
	
	@Override
	public User findUserByName(String name) {
		Session session = HibernateSessionFactory.getSession().openSession();
		Query<User> query = session.createQuery("FROM User WHERE name = :param", User.class);
		query.setParameter("param", name);
		List<User> users = query.list();
		session.close();
		return users.size()>0 ? users.get(0) : null;
	}
	
	@Override
	public User findUserByLogin(String login) {
		Session session = HibernateSessionFactory.getSession().openSession();
		Query<User> query = session.createQuery("FROM User WHERE login = :param", User.class);
		query.setParameter("param", login);
		List<User> users = query.list();
		session.close();
		return users.size()>0 ? users.get(0) : null;
	}
	
	@Override
	public User findUserById(int id) {
		Session session = HibernateSessionFactory.getSession().openSession();
		Query<User> query = session.createQuery("FROM User WHERE id = :param", User.class);
		query.setParameter("param", id);
		List<User> users = query.list();
		session.close();
		return users.size()>0 ? users.get(0) : null;
	}
	
	@Override
	public void setAdmin(User user, boolean isAdmin) {
		if(user==null)
			return;
		
		user.setAdmin(isAdmin);
		Session session = HibernateSessionFactory.getSession().openSession();
		Transaction transaction = session.beginTransaction();
		session.update(user);
		transaction.commit();
		session.close();
	}
	
	@Override
	public boolean deleteUser(User user) {
		if(user==null)
			return false;
		
		historyDao.deleteStampsForUser(user.getId());
		bookmarkDao.deleteBookmarksForUser(user.getId());
		Session session = HibernateSessionFactory.getSession().openSession();
		Transaction transaction = session.beginTransaction();
		Query<User> query = session.createQuery("FROM User WHERE user_id = :param", User.class);
		query.setParameter("param", user.getId());
		List<User> users = query.list();
		if(users.size()>0)
			session.delete(users.get(0));
		transaction.commit();
		session.close();
		return true;
	}
	
	@Override
	public List<User> getAllUsers() {
		Session session = HibernateSessionFactory.getSession().openSession();
		Query<User> query = session.createQuery("FROM User", User.class);
		return query.list();
	}
	
	private void createHashes(User user) {
		user.setSalt(PasswordProcessor.generateStaticSalt());
		user.setPassword(PasswordProcessor.createPasswordHash(user.getPassword(), user.getSalt()));
	}

	@Override
	public int getUsersQuantity() {
		int result = 0;
		Session session = HibernateSessionFactory.getSession().openSession();
		
		@SuppressWarnings("unchecked")
		List<BigInteger> counter = session.createSQLQuery("SELECT count(*) FROM users").list();
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
