package pl.model.dao;

import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateful;
import javax.inject.Named;

import pl.model.entities.User;

@Named("userServiceImpl")
@Stateful
public class UserServiceImpl implements UserService {
	private static final long serialVersionUID = 4211888101576176371L;
	
	private List<User> users;

	@EJB
	UserDao userDao;
	
	@Override
	public List<User> getUsers() {
		users = userDao.getAllUsers();
		return users;
	}
	
	public void setUsers(List<User> users) {
		this.users = users;
	}
}
