package pl.model.entities;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;


@Entity
@Table (name="users",
	uniqueConstraints = @UniqueConstraint(name="uk_user_login", columnNames = {"login"}))
public class User {
	public static final String FIELD_USER_ID = "user_id";
	public static final String FIELD_USER_NAME = "user_name";
	public static final String FIELD_USER_LOGIN = "login";
	public static final String FIELD_USER_PASS = "user_password";
	public static final String FIELD_SALT = "salt";
	public static final String FIELD_REGISTRATION_STAMP = "registration_date";
	public static final String FIELD_IS_ADMIN = "is_admin";
	
	@Id
	@SequenceGenerator(name = "user_seq",
						sequenceName = "SEQ_USER",
						allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE,
					generator = "user_seq")
	@Column(name=FIELD_USER_ID)
	private int id;

	@Column(name=FIELD_USER_NAME)
	private String name;
	
	@Column(name=FIELD_USER_LOGIN)
	private String login;
	
	@Column(name=FIELD_USER_PASS)
	private String password;
	
	@Column(name=FIELD_SALT)
	private String salt;
	
	@Column(name=FIELD_REGISTRATION_STAMP)
	private Date registrationDate;
	
	@Column(name=FIELD_IS_ADMIN)
	private boolean admin = false;
	
	
	public User() {
		
	}
	
	public User(String name, String login, String password) {
		this.name = name;
		this.login = login;
		this.password = password;
	}
	
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setLogin(String login) {
		this.login = login;
	}
	
	public String getLogin() {
		return login;
	}
	
	public String getPassword() {
		return password;
	}
	
	public void setPassword(String password) {
		this.password = password;
	}
	
	public void setSalt(String salt) {
		this.salt = salt;
	}
	
	public String getSalt() {
		return salt;
	}
	
	public Date  getRegistrationDate() {
		return registrationDate;
	}

	public void setRegistrationDate(Date registrationDate) {
		this.registrationDate = registrationDate;
	}
	
	@PrePersist
	public void registrationDate() {
		this.registrationDate = new Date();
	}
	
	public boolean isAdmin() {
		return admin;
	}

	public void setAdmin(boolean isAdmin) {
		this.admin = isAdmin;
	}
	
	@Override
	public String toString() {
		return id + ": " + name;
	}
}
