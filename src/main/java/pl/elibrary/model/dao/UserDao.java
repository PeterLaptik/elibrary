package pl.elibrary.model.dao;

import java.util.List;

import pl.elibrary.model.entities.User;

public interface UserDao {

	public boolean create(User user);
	
	public boolean update(User user);
	
	public boolean delete(User user);
	
	public User findUserByName(String name);
	
	public User findUserByLogin(String login);
	
	public User findUserById(int id);
	
	public void setAdmin(User user, boolean isAdmin);
	
	public List<User> getAllUsers();
	
	public int getUsersQuantity();
}
