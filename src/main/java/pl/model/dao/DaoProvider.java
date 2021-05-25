package pl.model.dao;

import javax.ejb.Singleton;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;

@Named
@Singleton
@ApplicationScoped
public class DaoProvider implements IDaoProvider{
	
	public UserDao getUserDao() {
		return new UserDaoImpl();
	}
	
	public UserSessionDao getSessionDao() {
		return new UserSessionDaoImpl();
	}
}
