package pl.elibrary.model.cache;

import java.util.HashMap;
import java.util.Map;

import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.ejb.Startup;

import pl.elibrary.model.dao.UserSessionDao;
import pl.elibrary.model.entities.UserSession;

/**
 * Active sessions holder.
 * SessionCache implementation
 */
@Startup
@Singleton
public class SessionCacheImpl implements SessionCache {
	private static final long serialVersionUID = 1753968269497514267L;
	
	/** Local sessions cache **/
	private Map<String,UserSession> sessions = new HashMap<String,UserSession>();
	
	@EJB
	private UserSessionDao sessionDao;

	/**
	 * Checks cache for a session of the defined uuid.
	 * If a session does not found then searches session stamp in a database.
	 * If a session found in a database then the session is added to the local sessions cache
	 * @param sessionUuid - session uuid
	 * @return session object if a session found, otherwise returns null
	 */
	@Override
	public UserSession findSessionByUuid(String sessionUuid) {
		UserSession session = sessions.get(sessionUuid);
		if(session==null) {
			session = sessionDao.findSessionByUuid(sessionUuid);
			if(session!=null)
				sessions.put(sessionUuid, session);
		}
		return session;
	}

	/**
	 * Deletes session from the local session cache and a database (logout).
	 */
	@Override
	public void deleteSessionByUuid(String uuid) {
		sessions.remove(uuid);
		sessionDao.deleteSessionByUuid(uuid);
	}
}
