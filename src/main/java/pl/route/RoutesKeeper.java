package pl.route;

import javax.ejb.Singleton;
import javax.ejb.Startup;

@Singleton
@Startup
public class RoutesKeeper implements IRoutes {
	private static final long serialVersionUID = 4648516858600335225L;
	
	private String login = "login";
	private String home = "home";
	private String register = "register";
	private String applicationPath = null;
	
	public String getLogin() {
		return login;
	}
	public void setLogin(String login) {
		this.login = login;
	}
	public String getHome() {
		return home;
	}
	public void setHome(String home) {
		this.home = home;
	}
	public String getRegister() {
		return register;
	}
	public void setRegister(String register) {
		this.register = register;
	}
	
	public String getApplicationPath() {
		return applicationPath;
	}
}
