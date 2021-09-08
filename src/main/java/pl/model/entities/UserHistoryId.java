package pl.model.entities;

import java.io.Serializable;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Embeddable;


@Embeddable
public class UserHistoryId implements Serializable {
	private static final long serialVersionUID = -6833104549511786702L;
	private static final String FIELD_USER_ID = "user_id";
	private static final String FIELD_BOOK_ID = "book_id";
	
	@Column(name=FIELD_USER_ID)
	private int userId;

	@Column(name=FIELD_BOOK_ID)
	private int bookId;
	
	public UserHistoryId() {
		
	}
	
	public UserHistoryId(int userId, int bookId) {
		this.userId = userId;
		this.bookId = bookId;
	}
	
	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public int getBookId() {
		return bookId;
	}

	public void setBookId(int bookId) {
		this.bookId = bookId;
	}
	
	@Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UserHistoryId)) return false;
        UserHistoryId other = (UserHistoryId) o;
        return Objects.equals(getUserId(), other.getUserId()) &&
                Objects.equals(getBookId(), other.getBookId());
    }
 
    @Override
    public int hashCode() {
        return Objects.hash(getUserId(), getBookId());
    }
}
