package pl.model.entities;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.PrePersist;
import javax.persistence.Table;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
@Table (name="users_history")
public class UserHistory implements Serializable {
	public static final long serialVersionUID = -8284143090076041152L;
	public static final String MAX_BOOKS_IN_HISTORY = "MAX_BOOKS_IN_HISTORY";
	public static final String FIELD_PAGE = "page";
	public static final String FIELD_DATE = "date_opened";
	
	@EmbeddedId
	private UserHistoryId id = new UserHistoryId();
	
	@ManyToOne
    @JoinColumn(name = User.FIELD_USER_ID, insertable = false, updatable = false)
	private User user;

	@ManyToOne
    @JoinColumn(name = Book.FIELD_ID, insertable = false, updatable = false)
	private Book book;
	
	@Column(name=FIELD_PAGE)
	private int page;

	@Column(name=FIELD_DATE)
	private Date lastOpenDate;
	
	
	public UserHistory() {
		
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
	
	public int getPage() {
		return page;
	}

	public void setPage(int page) {
		this.page = page;
	}
	
	public UserHistoryId getId() {
		return id;
	}

	public void setId(UserHistoryId id) {
		this.id = id;
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
}
