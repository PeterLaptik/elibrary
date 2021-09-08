package pl.model.dao;

import java.util.List;

import pl.model.entities.User;

public interface UserDao {

	public boolean create(User user);
	
	public boolean deleteUser(User user);
	
	public User findUserByName(String name);
	
	public User findUserByLogin(String login);
	
	public User findUserBuId(int id);
	
	public void setAdmin(User user, boolean isAdmin);
	
	public List<User> getAllUsers();
	
	public int getUsersQuantity();
}
