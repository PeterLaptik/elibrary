package pl.elibrary.route;

import java.io.Serializable;

/**
 * Keeps and returns url names of the application
 */
public interface IRoutes extends Serializable {
	public String getHome();
	public String getLogin();
	public String getRegister();
	public String getPdfPage();
	public String getDjvuPage();
}
