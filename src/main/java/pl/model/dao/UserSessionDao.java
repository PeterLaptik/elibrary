package pl.model.dao;

import pl.model.entities.UserSession;

public interface UserSessionDao {
	
	public void create(UserSession userSession);
	
	public UserSession findSessionByUuid(String uuid);
	
	public UserSession findSessionByUserId(int userId);
	
	public void deleteSessionByUuid(String uuid);
}
