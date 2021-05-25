package pl.model.credentials;

import javax.ejb.LocalBean;
import javax.ejb.Stateful;
import javax.inject.Inject;

import pl.model.dao.IDaoProvider;
import pl.model.dao.UserDao;
import pl.model.dao.UserDaoImpl;
import pl.model.entities.User;
import pl.model.entities.UserRegBean;

@LocalBean
@Stateful
public class RegistrationManager {
	// Error messages
	private static final String MSG_ENTER_NAME = "Enter user name";
	private static final String MSG_ENTER_LOGIN = "Enter user login";
	private static final String MSG_ENTER_PASSWORD = "Enter user password";
	private static final String MSG_ENTER_PASSWORD_R = "Repeat user password";
	private static final String MSG_ENTER_PASS_DOES_NOT_MATCH = "Entered passwords do not match";
	private static final String MSG_LOGIN_EXISTS = "Entered login exists. Choose another one";
	
	private String errorString = "";
	
	@Inject
	IDaoProvider provider;
	
	public RegistrationManager() {
		
	}
	
	/**
	 * Checks are the credentials OK
	 * @param user - user to register
	 * @return true if all OK, otherwise false
	 */
	public boolean isCredentialsOk(UserRegBean user) {
		if(user.getName().equals("")) {
			errorString = MSG_ENTER_NAME;
			return false;
		}
		
		if(user.getLogin().equals("")) {
			errorString = MSG_ENTER_LOGIN;
			return false;
		}
		
		if(user.getPass().equals("")) {
			errorString = MSG_ENTER_PASSWORD;
			return false;
		}
		
		if(user.getPassRepeat().equals("")) {
			errorString = MSG_ENTER_PASSWORD_R;
			return false;
		}
		
		if(!user.getPass().equals(user.getPassRepeat())) {
			errorString = MSG_ENTER_PASS_DOES_NOT_MATCH;
			return false;
		}
		
		UserDao userDao = provider.getUserDao();
		if(userDao.findUserByLogin(user.getLogin())!=null) {
			errorString = MSG_LOGIN_EXISTS;
			return false;
		}
		
		errorString = "";
		return true;
	}

	/**
	 * Returns text of a last error
	 * @return
	 */
	public String getErrorString() {
		return errorString;
	}

	/**
	 * Registers user
	 * @param user
	 */
	public void register(UserRegBean user) {
		UserDao dao = provider.getUserDao();
		User userToCreate = new User();
		userToCreate.setLogin(user.getLogin());
		userToCreate.setPassword(user.getPass());
		userToCreate.setName(user.getName());
		dao.create(userToCreate);
	}
}
