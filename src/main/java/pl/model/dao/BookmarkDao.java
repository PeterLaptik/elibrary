package pl.model.dao;

import java.io.Serializable;
import java.util.List;

import pl.model.entities.Bookmark;

public interface BookmarkDao extends Serializable {
	
	void createBookmark(Bookmark bookmark, int bookId, int userId);
	
	void deleteBookmark(Bookmark bookmark);
	
	List<Bookmark> getBookmarks(int bookId, int userId);
	
	void deleteBookmarksForBook(int bookId);
	
	void deleteBookmarksForUser(int userId);
}
