package elibrary.model;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import pl.model.dao.UserDao;
import pl.model.dao.UserDaoImpl;
import pl.model.dao.UserSessionDao;
import pl.model.dao.UserSessionDaoImpl;
import pl.model.entities.User;
import pl.model.entities.UserSession;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class UserTest {
	final static String testLogin = "test_user";
	final static String testPass = "qwerty";
	private static User user = null;
	
	static {
		UserTest.user = new User();
		UserTest.user.setName(testLogin);
		UserTest.user.setPassword(testPass);
		UserTest.user.setLogin(testLogin);
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
	}
	
	@Test
	@Ignore
	public void test3_userPasswordCheck() {
		
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
