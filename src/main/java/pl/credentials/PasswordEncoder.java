package pl.credentials;

import java.io.Serializable;

/**
 * Password string encoder
 */
public interface PasswordEncoder extends Serializable {
	/**
	 * Creates password hash
	 * @param password
	 * @param salt
	 * @return MD5 hash
	 */
	public String createPasswordHash(String password, String salt);
	
	/**
	 * Generates MD5-hash for string value
	 * @param val - value to hash
	 * @return MD5-hash (string value)
	 */
	public String generateMd5For(String val);
	
	/**
	 * Generates random string
	 * @return salt
	 */
	public String generateStaticSalt();
}
