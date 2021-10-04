package pl.credentials.beans;

import java.io.Serializable;
import javax.ejb.Local;
import javax.ejb.Stateful;

/**
 * User registration data holder
 */
@Local
@Stateful
public class UserRegBean implements Serializable {
	private static final long serialVersionUID = -6717766886237606588L;
	private String name;
	private String login;
	private String pass;
	private String passRepeat;
	
	public UserRegBean() {

	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public String getPass() {
		return pass;
	}

	public void setPass(String pass) {
		this.pass = pass;
	}

	public String getPassRepeat() {
		return passRepeat;
	}

	public void setPassRepeat(String passRepeat) {
		this.passRepeat = passRepeat;
	}
}
