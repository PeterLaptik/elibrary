package pl.model.entities;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
@Table (name="users_history")
public class UserHistory {
	private static final String FIELD_DATE = "date_opened";
	
	@OneToOne(optional=true)
	@OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name="user_id", insertable=false, updatable=false)
	private User user;

	@OneToOne(optional=true)
	@OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name="user_id", insertable=false, updatable=false)
	private Book book;
	
	@Column(name=FIELD_DATE)
	private Date lastOpenDate;
	
	
	UserHistory() {
		
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
}
