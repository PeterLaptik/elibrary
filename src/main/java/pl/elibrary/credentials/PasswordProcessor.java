package pl.elibrary.credentials;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import javax.ejb.Singleton;
import javax.ejb.Startup;

/**
 * Password encoder implementation
 */
@Singleton
@Startup
public class PasswordProcessor implements PasswordEncoder {
	private static final long serialVersionUID = -6207835897404050306L;
	private static final int SALT_LENGTH = 10;
	private static final int SALT_DYNAMIC_POSITION = 3;
	
	public PasswordProcessor() {
		
	}
	
	/**
	 * Creates password hash
	 * @param password
	 * @param salt
	 * @return MD5 hash
	 */
	public String createPasswordHash(String password, String salt) {
		String result = generateMd5For(password + salt);
		String dynamicSalt = getDynamicSalt(result);
		return generateMd5For(dynamicSalt);
	}
	
	/**
	 * Generates MD5-hash for string value
	 * @param val - value to hash
	 * @return MD5-hash (string value)
	 */
	public String generateMd5For(String val) {
		StringBuilder sb = new StringBuilder();
		try {
			MessageDigest digest = MessageDigest.getInstance("MD5");
			byte[] bytes = digest.digest(val.getBytes());
			for(byte b: bytes) {
				sb.append(String.format("%02x", b));
			}
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return sb.toString();
	}
	
	/**
	 * Generates random string (length = 'SALT_LENGTH')
	 * @return salt
	 */
	public String generateStaticSalt() {
		SecureRandom random = new SecureRandom();
		StringBuilder sb = new StringBuilder();
		int counter = 0;
		while(counter<SALT_LENGTH) {
			int val = random.nextInt(123);
			if(((val<123)&&(val>96))
					||((val<91)&&(val>64))
					||((val<58)&&(val>47))) {
				counter++;
				sb.append(((char)val));
			}	
		}
		return sb.toString();
	}
	
	/**
	 * Generates salt by hash-code: from 'SALT_DYNAMIC_POSITION'-character to the end of string
	 * @param val - hash value
	 * @return salt
	 */
	private String getDynamicSalt(String val) {
		return val.length()>SALT_DYNAMIC_POSITION ? val.substring(SALT_DYNAMIC_POSITION) : ""; 
	}
}
