package pl.route.rest;

import javax.ejb.EJB;
import javax.ws.rs.Consumes;
import javax.ws.rs.CookieParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Cookie;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.primefaces.json.JSONObject;

import pl.credentials.PasswordEncoder;
import pl.model.cache.SessionCache;
import pl.model.dao.UserDao;
import pl.model.entities.User;
import pl.model.entities.UserSession;

@Path("/user")
public class UserResourse {
	@EJB
	UserDao userDao;
	
	@EJB
	private SessionCache sessionCache;
	
	@EJB
	private PasswordEncoder passwordProcessor;
	
	@GET
    @Produces(MediaType.APPLICATION_JSON)
	public Response getUserInfo(@CookieParam(UserSession.FIELD_SESSION_UUID) Cookie cookie) {
		UserSession session = sessionCache.findSessionByUuid(cookie.getValue());
		if(session==null)
			return Response.serverError().build();
		
		User user = userDao.findUserById(session.getUserId());
		if(user==null)
			return Response.serverError().build();
		
		return Response.ok(user.toJson())
    			.header("Access-Control-Allow-Origin", "*")
    			.header("Access-Control-Allow-Credentials", "true")
    			.header("Access-Control-Allow-Headers", "origin, content-type, accept, authorization")
    			.header("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS, HEAD")
    			.build();
	}
	
	@POST
    @Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response changePassword(String data, 
									@CookieParam(UserSession.FIELD_SESSION_UUID) Cookie cookie) {
		UserSession session = sessionCache.findSessionByUuid(cookie.getValue());
		if(session==null)
			return Response.serverError().build();
		
		User user = userDao.findUserById(session.getUserId());
		if(user==null)
			return Response.serverError().build();
		
		try {
			JSONObject jsonObject = new JSONObject(data);
			String newName = jsonObject.getString("newName");
			if(newName!=null) {
				user.setName(newName);
				userDao.update(user);
				return Response.ok("User name has been changed.")
		    			.header("Access-Control-Allow-Origin", "*")
		    			.header("Access-Control-Allow-Credentials", "true")
		    			.header("Access-Control-Allow-Headers", "origin, content-type, accept, authorization")
		    			.header("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS, HEAD")
		    			.build();
			}
			
			String oldPass = jsonObject.getString("oldPass");
			String newPass = jsonObject.getString("newPass");
			
			String hash = passwordProcessor.createPasswordHash(oldPass, user.getSalt());
			if(!hash.equals(user.getPassword())) {
				return Response.ok("Wrong current password input!")
    			.header("Access-Control-Allow-Origin", "*")
    			.header("Access-Control-Allow-Credentials", "true")
    			.header("Access-Control-Allow-Headers", "origin, content-type, accept, authorization")
    			.header("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS, HEAD")
    			.build();
			}
			
			user.setSalt(passwordProcessor.generateStaticSalt());
			user.setPassword(passwordProcessor.createPasswordHash(newPass, user.getSalt()));
			userDao.update(user);
			
		} catch (Exception e) {
			System.err.println("Error on password changhing.");
			e.printStackTrace();
		}
		
		return Response.ok("Password has been changed.")
    			.header("Access-Control-Allow-Origin", "*")
    			.header("Access-Control-Allow-Credentials", "true")
    			.header("Access-Control-Allow-Headers", "origin, content-type, accept, authorization")
    			.header("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS, HEAD")
    			.build();
	}
}
