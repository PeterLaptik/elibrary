package pl.model.entities;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObjectBuilder;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import pl.model.cache.objects.SectionNode;

@Entity
@Table (name="bookmarks")
public class Bookmark {
	public static final String FIELD_COLUMN_NAME = "bookmark_text";
	public static final String FIELD_COLUMN_PAGE = "page_number";
	
	@EmbeddedId
	private BookmarkId id;
	
	@ManyToOne
    @JoinColumn(name = User.FIELD_USER_ID, insertable = false, updatable = false,
    			foreignKey=@ForeignKey(name="fk_bm_outer_user_id"))
	private User user;

	@ManyToOne
    @JoinColumn(name = Book.FIELD_ID, insertable = false, updatable = false,
    			foreignKey=@ForeignKey(name="fk_bm_outer_book_id"))
	private Book book;

	@Column(name = Bookmark.FIELD_COLUMN_NAME, insertable = false, updatable = false)
	private String name;
	
	@Column(name = Bookmark.FIELD_COLUMN_PAGE)
	private int page;


	public Bookmark() {
		
	}
	
	
	public BookmarkId getId() {
		return id;
	}

	public void setId(BookmarkId id) {
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
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public int getPage() {
		return page;
	}

	public void setPage(int page) {
		this.page = page;
	}
	
	public JsonObjectBuilder toJsonBuilder() {
		JsonObjectBuilder builder = Json.createObjectBuilder();
    	builder.add(Bookmark.FIELD_COLUMN_NAME, name)
				.add(Bookmark.FIELD_COLUMN_PAGE, page);
		return builder;
	}
}
