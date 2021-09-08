package pl.model.dao;

import pl.model.entities.UserHistory;

public interface UserHistoryDao {
	
	void createStamp(UserHistory userHistory);
	
}
