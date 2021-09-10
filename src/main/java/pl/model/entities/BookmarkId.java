package pl.model.entities;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class BookmarkId {
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
}
