package pl.route;

import javax.ejb.Singleton;
import javax.ejb.Startup;

/**
 * Keeps and returns url names of the application
 */
@Singleton
@Startup
public class RoutesKeeper implements IRoutes {
	private static final long serialVersionUID = 4648516858600335225L;
	
	private String login = "login";
	private String home = "home";
	private String register = "registration";
	private String pdfPage = "pdf-reader";
	private String djvuPage = "djvu-reader";
	
	@Override
	public String getLogin() {
		return login;
	}
	
	public void setLogin(String login) {
		this.login = login;
	}
	
	@Override
	public String getHome() {
		return home;
	}
	
	public void setHome(String home) {
		this.home = home;
	}
	
	@Override
	public String getRegister() {
		return register;
	}
	
	public void setRegister(String register) {
		this.register = register;
	}
	
	@Override
	public String getPdfPage() {
		return pdfPage;
	}
	
	public void setPdfPage(String pdfPage) {
		this.pdfPage = pdfPage;
	}
	
	@Override
	public String getDjvuPage() {
		return djvuPage;
	}
	
	public void setDjvuPage(String djvuPage) {
		this.djvuPage = djvuPage;
	}
}
