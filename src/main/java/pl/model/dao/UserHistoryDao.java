package pl.model.dao;

import java.io.Serializable;
import java.util.List;

import javax.ejb.Local;

import pl.model.entities.Book;
import pl.model.entities.UserHistory;

public interface UserHistoryDao extends Serializable {
	
	void createStamp(int bookId, int userId, Integer maxRecords);
	
	void updateStamp(UserHistory userHistory);
	
	void deleteStamp(UserHistory userHistory);
	
	void deleteStampsForBook(int bookId);
	
	void deleteStampsForUser(int userId);
	
	List<Book> getHistoryBooks(int userId);
	
	List<UserHistory> getHistory(int userId);
	
	int getLastPageFor(int bookId, int userId);
}
