package pl.elibrary.credentials;


import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.servlet.http.Cookie;

import pl.elibrary.model.cache.SessionCache;
import pl.elibrary.model.dao.UserDao;
import pl.elibrary.model.dao.UserSessionDao;
import pl.elibrary.model.entities.User;
import pl.elibrary.model.entities.UserSession;

/**
 * Authentification manager imlementation
 * Description: see IAuthentification
 */
@Singleton
@Startup
public class AuthentificationManager implements IAuthentification {
	private static final long serialVersionUID = 1225409457256072688L;

	@EJB
	private UserDao userDao;
	
	@EJB
	private UserSessionDao sessionDao;
	
	@EJB
	private SessionCache sessions;
	
	@EJB
	private PasswordEncoder passwordProcessor;
	
	@Override
	public Cookie login(String login, String pass) {
		Cookie sessionCookie = null;
		User foundUser = userDao.findUserByLogin(login);
		if(foundUser==null)
			return sessionCookie;
		
		String hash = passwordProcessor.createPasswordHash(pass, foundUser.getSalt());
		if(hash.equals(foundUser.getPassword())) {
			sessionDao.purgeSessionsForUser(foundUser.getId());
			UserSession session = new UserSession(foundUser);
			sessionDao.create(session);
			String sessionUuid = sessionDao.findSessionByUserId(foundUser.getId()).getUuid();
			sessionCookie = new Cookie(UserSession.FIELD_SESSION_UUID, sessionUuid);
		}
		
		return sessionCookie;
	}
	
	@Override
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
    	
    	UserSession session = sessions.findSessionByUuid(sessionUuid);
    	return session!=null;
	}
	
	@Override
	public boolean hasSession(String sessionUuid) {
    	if(sessionUuid==null)
    		return false;
    	
    	UserSession session = sessions.findSessionByUuid(sessionUuid);
    	return session!=null;
	}
	
	@Override
	public void logout(Cookie[] cookies) {
		String sessionUuid = null;
    	if(cookies==null)
    		return;
    	
    	for(Cookie cookie: cookies) {
    		if(cookie==null)
    			continue;
    		
    		if(cookie.getName().equals(UserSession.FIELD_SESSION_UUID)) {
    			sessionUuid = cookie.getValue();
    		}
    	}
    	if(sessionUuid==null)
    		return;
    	
    	sessions.deleteSessionByUuid(sessionUuid);
	}
}
