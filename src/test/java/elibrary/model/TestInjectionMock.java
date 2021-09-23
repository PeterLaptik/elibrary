package elibrary.model;

import java.lang.reflect.Field;

import pl.credentials.AuthentificationManager;
import pl.model.cache.SectionCache;
import pl.model.cache.SectionCacheImpl;
import pl.model.cache.SessionCache;
import pl.model.cache.SessionCacheImpl;
import pl.model.dao.BookDao;
import pl.model.dao.SectionDao;
import pl.model.dao.UserDao;
import pl.model.dao.impl.BookDaoImpl;
import pl.model.dao.impl.BookmarkDaoImpl;
import pl.model.dao.impl.SectionDaoImpl;
import pl.model.dao.impl.UserDaoImpl;
import pl.model.dao.impl.UserHistoryDaoImpl;
import pl.model.dao.impl.UserSessionDaoImpl;

public class TestInjectionMock {

	public static AuthentificationManager getAuthManager() {
		AuthentificationManager mgr = new AuthentificationManager();
		try {
			// User DAO
			Field field = mgr.getClass().getDeclaredField("userDao");
			field.setAccessible(true);
			field.set(mgr, new UserDaoImpl());
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
