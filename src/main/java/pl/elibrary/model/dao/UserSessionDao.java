package pl.elibrary.model.dao;

import pl.elibrary.model.entities.UserSession;

public interface UserSessionDao {
	
	public void create(UserSession userSession);
	
	public UserSession findSessionByUuid(String uuid);
	
	public UserSession findSessionByUserId(int userId);
	
	public void deleteSessionByUuid(String uuid);
	
	public void purgeSessionsForUser(int userId);
}
