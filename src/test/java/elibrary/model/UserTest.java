package elibrary.model;

import java.lang.reflect.Field;

import javax.ejb.EJB;
import javax.servlet.http.Cookie;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import pl.model.credentials.AuthentificationManager;
import pl.model.dao.UserDao;
import pl.model.dao.UserDaoImpl;
import pl.model.dao.UserSessionDao;
import pl.model.dao.UserSessionDaoImpl;
import pl.model.entities.User;
import pl.model.entities.UserSession;


@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class UserTest {
	private final static String testLogin = "test_user";
	private final static String testPass = "qwerty";
	private static User user = null;
	private static AuthentificationManager mgr = new AuthentificationManager();
	
	static {
		UserTest.user = new User();
		UserTest.user.setName(testLogin);
		UserTest.user.setPassword(testPass);
		UserTest.user.setLogin(testLogin);
		
		// Init authentification manager manager via reflection
		try {
			// User DAO
			Field field = mgr.getClass().getDeclaredField("userDao");
			field.setAccessible(true);
			field.set(mgr, new UserDaoImpl());
			// Session DAO
			field = mgr.getClass().getDeclaredField("sessionDao");
			field.setAccessible(true);
			field.set(mgr, new UserSessionDaoImpl());
		} catch (NoSuchFieldException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@BeforeClass
	public static void removeTestUsersIfExist() {
		UserDao dao = new UserDaoImpl();
		User user = dao.findUserByLogin(testLogin);
		if(user!=null)
			dao.deleteUser(user);
	}
	
	@Test
	public void test1_userCreatingTestDefDao() {
		UserDao dao = new UserDaoImpl();
		Assert.assertTrue(dao.create(UserTest.user));
		// Trying to add a user with duplicating login
		Assert.assertFalse(dao.create(UserTest.user));
	}
	
	@Test
	public void test2_userSessionCreatingDefDao() {
		UserDao userDao = new UserDaoImpl();
		User dbUser = userDao.findUserByLogin(UserTest.user.getLogin());
		// Whether user found
		Assert.assertNotNull(dbUser);

		UserSession session = new UserSession(dbUser);
		UserSessionDao dao = new UserSessionDaoImpl();
		dao.create(session);
		UserSession foundSession = dao.findSessionByUserId(UserTest.user.getId());
		// Has the session been appended
		Assert.assertNotNull(foundSession);
		
		// Check session by uuid
		String sessionId = foundSession.getUuid();
		UserSession sessionByUuid = dao.findSessionByUuid(sessionId);
		Assert.assertNotNull(sessionByUuid);
		
		// Not existing session uuid
		UserSession noSession = dao.findSessionByUuid(sessionId + "xyz");
		Assert.assertNull(noSession);
	}
	
	@Test
	public void test3_cookieCheck() {
		UserSessionDao dao = new UserSessionDaoImpl();
		UserSession foundSession = dao.findSessionByUserId(UserTest.user.getId());
		// Does a session exists
		Assert.assertNotNull(foundSession);
		
		// Cookie check
		Cookie cookie = new Cookie(UserSession.FIELD_SESSION_UUID, foundSession.getUuid());
		Cookie[] cookies = new Cookie[1];
		cookies[0] = cookie;
		Assert.assertTrue(mgr.hasSession(cookies));
	}
	
	@Test
	public void test3_userPasswordCheck() {
		Assert.assertNotNull(mgr.login(testLogin, testPass));
		Assert.assertNull(mgr.login(testLogin, testPass + "a"));
		Assert.assertNull(mgr.login(testLogin + "b", testPass));
		Assert.assertNull(mgr.login(testLogin + "x", testPass + "y"));
	}
	
	@Test
	public void test4_userDeletingCascadeCheck() {
		UserDao uDao = new UserDaoImpl();
		User foundUser = uDao.findUserByLogin(UserTest.user.getLogin());
		Assert.assertNotNull(foundUser);
		
		UserSessionDao sDao = new UserSessionDaoImpl();
		UserSession session = sDao.findSessionByUserId(foundUser.getId());
		Assert.assertNotNull(session);
		// Delete user
		Assert.assertTrue(uDao.deleteUser(foundUser));
		// Check if the session has been deleted after user
		session = sDao.findSessionByUserId(foundUser.getId());
		Assert.assertNull(session);
	}
}
