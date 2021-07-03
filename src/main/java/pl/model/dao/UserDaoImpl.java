package pl.model.dao;

import java.util.List;

import javax.ejb.Stateless;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import pl.model.credentials.PasswordProcessor;
import pl.model.entities.User;
import pl.model.session.HibernateSessionFactory;

@Stateless
public class UserDaoImpl implements UserDao {
	
	public UserDaoImpl(){
		
	}
	
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
	
	public User findUserByName(String name) {
		Session session = HibernateSessionFactory.getSession().openSession();
		Query<User> query = session.createQuery("FROM User WHERE name = :param", User.class);
		query.setParameter("param", name);
		List<User> users = query.list();
		session.close();
		return users.size()>0 ? users.get(0) : null;
	}
	
	public User findUserByLogin(String login) {
		Session session = HibernateSessionFactory.getSession().openSession();
		Query<User> query = session.createQuery("FROM User WHERE login = :param", User.class);
		query.setParameter("param", login);
		List<User> users = query.list();
		session.close();
		return users.size()>0 ? users.get(0) : null;
	}
	
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
	
	public boolean deleteUser(User user) {
		if(user==null)
			return false;
		
		Session session = HibernateSessionFactory.getSession().openSession();
		Transaction transaction = session.beginTransaction();
		session.delete(user);
		transaction.commit();
		session.close();
		return true;
	}
	
	public List<User> getAllUsers() {
		Session session = HibernateSessionFactory.getSession().openSession();
		Query<User> query = session.createQuery("FROM User", User.class);
		return query.list();
	}
	
	private void createHashes(User user) {
		user.setSalt(PasswordProcessor.generateStaticSalt());
		user.setPassword(PasswordProcessor.createPasswordHash(user.getPassword(), user.getSalt()));
	}
}
