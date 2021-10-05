package pl.elibrary.model.session;

import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;

import pl.elibrary.model.entities.Book;
import pl.elibrary.model.entities.Bookmark;
import pl.elibrary.model.entities.Section;
import pl.elibrary.model.entities.User;
import pl.elibrary.model.entities.UserHistory;
import pl.elibrary.model.entities.UserSession;

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
