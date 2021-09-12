package pl.model.session;

import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;

import pl.model.entities.Book;
import pl.model.entities.Bookmark;
import pl.model.entities.Section;
import pl.model.entities.User;
import pl.model.entities.UserHistory;
import pl.model.entities.UserSession;

public class HibernateSessionFactory {
	private static SessionFactory session = null;
	
	private HibernateSessionFactory() {
		
	}
	
	public static SessionFactory getSession() {
		if(session==null) {
			try {
				Configuration configuration = new Configuration();
				configuration.configure();
				configuration.addAnnotatedClass(User.class);
				configuration.addAnnotatedClass(UserSession.class);
				configuration.addAnnotatedClass(Section.class);
				configuration.addAnnotatedClass(Book.class);
				configuration.addAnnotatedClass(UserHistory.class);
				configuration.addAnnotatedClass(Bookmark.class);
				StandardServiceRegistryBuilder builder = new StandardServiceRegistryBuilder();
				builder.applySettings(configuration.getProperties());
				session = configuration.buildSessionFactory(builder.build());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return session;
	}
}
