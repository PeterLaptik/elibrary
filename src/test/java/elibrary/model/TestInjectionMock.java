package elibrary.model;

import java.lang.reflect.Field;

import pl.elibrary.credentials.AuthentificationManager;
import pl.elibrary.credentials.PasswordEncoder;
import pl.elibrary.credentials.PasswordProcessor;
import pl.elibrary.model.cache.SectionCache;
import pl.elibrary.model.cache.SectionCacheImpl;
import pl.elibrary.model.cache.SessionCache;
import pl.elibrary.model.cache.SessionCacheImpl;
import pl.elibrary.model.dao.BookDao;
import pl.elibrary.model.dao.SectionDao;
import pl.elibrary.model.dao.UserDao;
import pl.elibrary.model.dao.impl.BookDaoImpl;
import pl.elibrary.model.dao.impl.BookmarkDaoImpl;
import pl.elibrary.model.dao.impl.SectionDaoImpl;
import pl.elibrary.model.dao.impl.UserDaoImpl;
import pl.elibrary.model.dao.impl.UserHistoryDaoImpl;
import pl.elibrary.model.dao.impl.UserSessionDaoImpl;

public class TestInjectionMock {
	private static PasswordEncoder passwordProcessor = new PasswordProcessor();
	
	public static AuthentificationManager getAuthManager() {
		AuthentificationManager mgr = new AuthentificationManager();
		try {
			// User DAO
			Field field = mgr.getClass().getDeclaredField("userDao");
			field.setAccessible(true);
			field.set(mgr, getUserDao());
			// Session DAO
			field = mgr.getClass().getDeclaredField("sessionDao");
			field.setAccessible(true);
			field.set(mgr, new UserSessionDaoImpl());
			// Sessions cache
			SessionCache sessionsCache = new SessionCacheImpl();
			field = sessionsCache.getClass().getDeclaredField("sessionDao");
			field.setAccessible(true);
			field.set(sessionsCache, new UserSessionDaoImpl());
			field = mgr.getClass().getDeclaredField("sessions");
			field.setAccessible(true);
			field.set(mgr, sessionsCache);
			// Password processor
			field = mgr.getClass().getDeclaredField("passwordProcessor");
			field.setAccessible(true);
			field.set(mgr, passwordProcessor);
		} catch (NoSuchFieldException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return mgr;
	}
	
	public static BookDao getBookDao() {
		BookDao bookDao = new BookDaoImpl();
		try {
			// User DAO
			Field field = bookDao.getClass().getDeclaredField("bookmarkDao");
			field.setAccessible(true);
			field.set(bookDao, new BookmarkDaoImpl());
			// Session DAO
			field = bookDao.getClass().getDeclaredField("historyDao");
			field.setAccessible(true);
			field.set(bookDao, new UserHistoryDaoImpl());
		} catch (NoSuchFieldException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return bookDao;
	}
	
	public static UserDao getUserDao() {
		UserDao userDao = new UserDaoImpl();
		try {
			// User DAO
			Field field = userDao.getClass().getDeclaredField("historyDao");
			field.setAccessible(true);
			field.set(userDao, new UserHistoryDaoImpl());
			// Session DAO
			field = userDao.getClass().getDeclaredField("bookmarkDao");
			field.setAccessible(true);
			field.set(userDao, new BookmarkDaoImpl());
			// Password processor
			field = userDao.getClass().getDeclaredField("passwordProcessor");
			field.setAccessible(true);
			field.set(userDao, passwordProcessor);
		} catch (NoSuchFieldException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return userDao;
	}
	
	public static SectionCache getSectionCache() {
		SectionCache cache = new SectionCacheImpl();
		try {
			Field field = cache.getClass().getDeclaredField("sectionDao");
			field.setAccessible(true);
			field.set(cache, new SectionDaoImpl());
		} catch (NoSuchFieldException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return cache;
	}
	
	public static SectionDao getSectionDao() {
		SectionDao dao = new SectionDaoImpl();
		try {
			Field field = dao.getClass().getDeclaredField("cache");
			field.setAccessible(true);
			field.set(dao, getSectionCache());
		} catch (NoSuchFieldException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return dao;
	}
}
