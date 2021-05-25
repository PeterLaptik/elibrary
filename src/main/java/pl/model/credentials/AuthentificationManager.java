package pl.model.credentials;


import javax.ejb.Singleton;
import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import javax.servlet.http.Cookie;

import pl.model.dao.IDaoProvider;
import pl.model.dao.UserDao;
import pl.model.dao.UserSessionDao;
import pl.model.dao.UserSessionDaoImpl;
import pl.model.entities.User;
import pl.model.entities.UserSession;


@Singleton
@Dependent
public class AuthentificationManager {
	
	@Inject
	private IDaoProvider provider;
	
	/**
	 * Checks a password
	 * @param login - user login
	 * @param pass - user password
	 * @return session UUID if logged in, otherwise null
	 */
	public String login(String login, String pass) {
		String sessionUuid = null;
		
		UserDao dao = provider.getUserDao();
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
    	
    	UserSessionDao sessionDao = provider.getSessionDao();
    	UserSession session = sessionDao.findSessionByUuid(sessionUuid);
    	return session!=null;
	}
}
