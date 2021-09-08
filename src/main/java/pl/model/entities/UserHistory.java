package pl.model.entities;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.PrePersist;
import javax.persistence.Table;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
@Table (name="users_history")
public class UserHistory implements Serializable{
	private static final long serialVersionUID = -8284143090076041152L;
	private static final String FIELD_PAGE = "page";
	private static final String FIELD_DATE = "date_opened";
	
	@Id
	@OneToOne(optional=true)
	@OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name="user_id", insertable=false, updatable=false)
	private User user;

	@Id
	@OneToOne(optional=true)
	@OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name="book_id", insertable=false, updatable=false)
	private Book book;
	
	@Column(name=FIELD_PAGE)
	private int page;
	
	@Column(name=FIELD_DATE)
	private Date lastOpenDate;
	
	
	public UserHistory() {
		
	}
	
	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Book getBook() {
		return book;
	}

	public void setBook(Book book) {
		this.book = book;
	}

	public Date getLastOpenDate() {
		return lastOpenDate;
	}

	public void setLastOpenDate(Date lastOpenDate) {
		this.lastOpenDate = lastOpenDate;
	}
	
	@PrePersist
	public void registrationDate() {
		this.lastOpenDate = new Date();
	}
}
