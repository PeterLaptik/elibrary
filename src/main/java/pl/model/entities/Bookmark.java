package pl.model.entities;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table (name="bookmarks")
public class Bookmark {
	@EmbeddedId
	private BookmarkId id;

	public Bookmark() {
		
	}
	
	public BookmarkId getId() {
		return id;
	}

	public void setId(BookmarkId id) {
		this.id = id;
	}
}
