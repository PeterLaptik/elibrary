package pl.model.dao.views;

import java.io.Serializable;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateful;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

import pl.model.dao.UserDao;
import pl.model.entities.User;

@Named("userService")
@Stateful
@ViewScoped
public class UserService implements Serializable {
	private static final long serialVersionUID = 4211888101576176371L;
	
	private List<User> users;
	private User selectedUser;

	enum SortType{
		BY_NAME, BY_ID, BY_LOGIN, BY_IS_ADMIN
	};
	
	SortType sorting = SortType.BY_ID;
	
	@EJB
	UserDao userDao;
	
	//@Override
	public List<User> getUsers() {
		users = userDao.getAllUsers();
		Collections.sort(users, new Comparator<User>() {
			@Override
			public int compare(User o1, User o2) {
				switch(sorting) {
					case BY_NAME:
						return o1.getName().toLowerCase().compareTo(o2.getName().toLowerCase());
					case BY_ID:
						return o1.getId() - o2.getId();
					case BY_LOGIN:
						return o1.getLogin().toLowerCase().compareTo(o2.getLogin().toLowerCase());
					case BY_IS_ADMIN:
						return Boolean.compare(o2.isAdmin(), o1.isAdmin());
				}
				return o1.getId() - o2.getId();
			}
		});
		return users;
	}
	
	public void sortByName() {
		sorting = SortType.BY_NAME;
	}
	
	public void sortByLogin() {
		sorting = SortType.BY_LOGIN;
	}
	
	public void sortById() {
		sorting = SortType.BY_ID;
	}
	
	public void sortByIsAdmin() {
		sorting = SortType.BY_IS_ADMIN;
	}
	
	public void setUsers(List<User> users) {
		this.users = users;
	}
	
	public User getSelectedUser() {
		return selectedUser;
	}

	public void setSelectedUser(User selectedUser) {
		this.selectedUser = selectedUser;
	}
	
	public void grantAdminRights() {
		userDao.setAdmin(selectedUser, true);
	}
	
	public void removeAdminRights() {
		userDao.setAdmin(selectedUser, false);
	}

	public void deleteSelectedUser() {
		if(selectedUser==null)
			return;
		
		userDao.deleteUser(selectedUser);
		selectedUser = null;
	}
}
