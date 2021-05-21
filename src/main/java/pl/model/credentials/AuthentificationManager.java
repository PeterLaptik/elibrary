package pl.model.credentials;


import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.enterprise.context.Dependent;
import javax.servlet.http.Cookie;

import pl.model.dao.UserDao;
import pl.model.dao.UserDaoImpl;
import pl.model.dao.UserSessionDao;
import pl.model.dao.UserSessionDaoImpl;
import pl.model.entities.User;
import pl.model.entities.UserSession;


@LocalBean
@Stateless
public class AuthentificationManager {
	
	public AuthentificationManager() {
		
	}
	
	/**
	 * Checks a password
	 * @param login - user login
	 * @param pass - user password
	 * @return session UUID if logged in, otherwise null
	 */
	public String login(String login, String pass) {
		String sessionUuid = null;
		
		UserDao dao = new UserDaoImpl();
		User foundUser = dao.findUserByLogin(login);
		if(foundUser==null)
			return sessionUuid;
		
		String hash = PasswordProcessor.createPasswordHash(pass, foundUser.getSalt());
		if(hash.equals(foundUser.getPassword())) {
			UserSession session = new UserSession(foundUser);
			UserSessionDao sessionDao = new UserSessionDaoImpl();
			sessionDao.create(session);
			session = sessionDao.findSessionByUserId(foundUser.getId());
			sessionUuid = session.getUuid();
		}
		
		return sessionUuid;
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
