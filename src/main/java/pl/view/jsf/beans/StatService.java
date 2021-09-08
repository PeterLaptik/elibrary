package pl.view.jsf.beans;

import java.io.Serializable;

import javax.ejb.EJB;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

import pl.model.dao.BookDao;
import pl.model.dao.UserDao;
import pl.model.entities.Book;

@Named("statService")
@ViewScoped
public class StatService implements Serializable {
	private static final long serialVersionUID = -8369429709221109642L;
	private int bookQuantity;
	private int userQuantity;
	private String volumePath = "";

	@EJB
	BookDao bookDao;
	
	@EJB
	UserDao userDao;
	
	public int getBookQuantity() {
		setBookQuantity(bookDao.getBooksQuantity());
		return bookQuantity;
	}

	public void setBookQuantity(int bookQuantity) {
		this.bookQuantity = bookQuantity;
	}
	
	public int getUserQuantity() {
		setUserQuantity(userDao.getUsersQuantity());
		return userQuantity;
	}

	public void setUserQuantity(int userQuantity) {
		this.userQuantity = userQuantity;
	}
	
	public String getVolumePath() {
		ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
		String directory = externalContext.getInitParameter(Book.BOOKS_VOLUME);
		setVolumePath(directory);
		return volumePath;
	}

	public void setVolumePath(String getVolumePath) {
		this.volumePath = getVolumePath;
	}
}
