package pl.model.dao;

/**
 * DAO implementations factory
 */
public interface IDaoProvider {
	public UserDao getUserDao();
	public UserSessionDao getSessionDao();
}
