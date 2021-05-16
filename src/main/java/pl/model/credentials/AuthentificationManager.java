package pl.model.credentials;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.servlet.http.Cookie;

import pl.model.dao.UserDao;
import pl.model.dao.UserDaoImpl;
import pl.model.dao.UserSessionDao;
import pl.model.dao.UserSessionDaoImpl;
import pl.model.entities.User;
import pl.model.entities.UserSession;

@Stateless
@LocalBean
public class AuthentificationManager {
	
	public AuthentificationManager() {
		
	}
	
	/**
	 * Checks a password
	 * @param login - user login
	 * @param pass - user password
	 * @return is password OK or not
	 */
	public boolean isPasswordOk(String login, String pass) {
		UserDao dao = new UserDaoImpl();
		User foundUser = dao.findUserByLogin(login);
		if(foundUser==null)
			return false;
		
		String hash = PasswordProcessor.createPasswordHash(pass, foundUser.getSalt());
		return hash.equals(foundUser.getPassword());
	}
	
	/**
	 * Checks whether a session exists (cookie check).
	 * Session uuid is being kept in a cookies
	 * @param request
	 * @return true is a session exists, otherwise false
	 */
	public boolean hasSession(Cookie[] cookies) {
		String sessionUuid = null;
    	if(cookies==null)
    		return false;
    	
    	for(Cookie cookie: cookies) {
    		if(cookie==null)
    			continue;
    		
    		if(cookie.getName().equals(UserSession.FIELD_SESSION_UUID)) {
    			sessionUuid = cookie.getValue();
    		}
    	}
    	if(sessionUuid==null)
    		return false;
    	
    	UserSessionDao sessionDao = new UserSessionDaoImpl();
    	UserSession session = sessionDao.findSessionByUuid(sessionUuid);
    	return session!=null;
	}
}
