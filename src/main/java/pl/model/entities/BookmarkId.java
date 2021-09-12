package pl.model.entities;

import java.io.Serializable;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class BookmarkId implements Serializable {
	private static final long serialVersionUID = -624636710140119450L;
	private static final String FIELD_USER_ID = "user_id";
	private static final String FIELD_BOOK_ID = "book_id";
	private static final String FIELD_BOOKMARK_TEXT = "bookmark_text";
	
	@Column(name=FIELD_USER_ID)
	private int userId;

	@Column(name=FIELD_BOOK_ID)
	private int bookId;
	
	@Column(name=FIELD_BOOKMARK_TEXT)
	private String name;
	
	
	public BookmarkId() {
		
	}
	
	public BookmarkId(int userId, int bookId, String name) {
		this.userId = userId;
		this.bookId = bookId;
		this.name = name;
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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	@Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BookmarkId)) return false;
        BookmarkId other = (BookmarkId) o;
        return Objects.equals(getUserId(), other.getUserId()) &&
                Objects.equals(getBookId(), other.getBookId()) &&
                Objects.equals(getName(), other.getName());
    }
 
    @Override
    public int hashCode() {
        return Objects.hash(getUserId(), getBookId(), getName());
    }
}
