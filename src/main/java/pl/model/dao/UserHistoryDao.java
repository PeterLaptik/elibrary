package pl.model.dao;

import pl.model.entities.UserHistory;

public interface UserHistoryDao {
	void createStamp(UserHistory userHistory);
	void updateStamp(UserHistory userHistory);
	int getLastPageFor(int bookId, int userId);
}
