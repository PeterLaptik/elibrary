package pl.model.dao;

import java.io.Serializable;
import java.util.List;

import pl.model.entities.User;

public interface UserService extends Serializable{
	
	public List<User> getUsers();
	
	//public setAdmin(boolean isAdmin);
}
