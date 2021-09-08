package pl.credentials;

import java.io.Serializable;

import javax.servlet.http.Cookie;

/**
 * Bean for checking sessions / login / logout
 * @author Peter Laptik
 */
public interface IAuthentification extends Serializable {
	/**
	 * Checks a password for the user login.
	 * Creates session for the user if password check is passed
	 * @param login - user login
	 * @param pass - user password
	 * @return session UUID if logged in, otherwise null
	 */
	public Cookie login(String login, String pass);
	
	/**
	 * Checks whether a session exists (cookies check).
	 * Session uuid is being kept in a cookies
	 * @param cookies array
	 * @return true is a session exists, otherwise false
	 */
	public boolean hasSession(Cookie[] cookies);
	
	/**
	 * Checks whether a session with defined uuid exists.
	 * @param sessionUuid
	 * @return true is a session exists, otherwise false
	 */
	boolean hasSession(String sessionUuid);
	
	/**
	 * Removes active sessions from a database
	 * @param cookies for the session for logging out
	 */
	public void logout(Cookie[] cookies);
}
