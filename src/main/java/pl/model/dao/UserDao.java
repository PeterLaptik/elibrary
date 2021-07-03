package pl.model.dao;

import java.util.List;

import pl.model.entities.User;

public interface UserDao {
	/**
	 * Creates user in db
	 * @param user - user to create
	 * @return true if user has been created / false if the user exist
	 */
	public boolean create(User user);
	
	public boolean deleteUser(User user);
	
	public User findUserByName(String name);
	
	public User findUserByLogin(String login);
	
	public void setAdmin(User user, boolean isAdmin);
	
	public List<User> getAllUsers();
}
