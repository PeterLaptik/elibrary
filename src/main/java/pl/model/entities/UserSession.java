package pl.model.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;


@Entity
@Table (name="user_sessions")
public class UserSession {
	public static final String FIELD_USER_ID = "user_id";
	public static final String FIELD_SESSION_UUID = "session_uuid";
	
	@Column(name=FIELD_USER_ID)
	private int userId;
	
	@Id
	@GeneratedValue(generator = "UUID")
	@GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
	@Column(name=FIELD_SESSION_UUID)
	private String uuid;
	
	@OneToOne(optional=true)
	@OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name="user_id", insertable=false, updatable=false,
    			foreignKey=@ForeignKey(name="fk_users_sessions"))
    private User user;
	
	public UserSession() {
		
	}
	
	public UserSession(User user) {
		userId = user.getId();
	}
	
	
	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}
}
