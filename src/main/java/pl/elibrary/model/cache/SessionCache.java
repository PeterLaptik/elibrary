package pl.elibrary.model.cache;

import java.io.Serializable;

import pl.elibrary.model.entities.UserSession;

/**
 * Bean keeping active sessions.
 * Method descriptions - see implementation
 * @author Peter Laptik
 */
public interface SessionCache extends Serializable {
	public UserSession findSessionByUuid(String sessionUuid);
	public void deleteSessionByUuid(String uuid);
}
