package pl.elibrary.route.rest;

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
import javax.ws.rs.core.Response.ResponseBuilder;

import org.primefaces.json.JSONObject;

import pl.elibrary.credentials.PasswordEncoder;
import pl.elibrary.model.cache.SessionCache;
import pl.elibrary.model.dao.UserDao;
import pl.elibrary.model.entities.User;
import pl.elibrary.model.entities.UserSession;

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
		
		return addCors(Response.ok(user.toJson()));
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
			String newName = getName(jsonObject);
			if(newName!=null) {
				user.setName(newName);
				userDao.update(user);
				return addCors(Response.ok("User name has been changed."));
			}
			
			String oldPass = jsonObject.getString("oldPass");
			String newPass = jsonObject.getString("newPass");
			if(newPass.equals("")) {
				return Response.serverError().build();
			}
			
			String hash = passwordProcessor.createPasswordHash(oldPass, user.getSalt());
			if(!hash.equals(user.getPassword())) {
				return addCors(Response.ok("Wrong current password input!"));
			}
			
			user.setSalt(passwordProcessor.generateStaticSalt());
			user.setPassword(passwordProcessor.createPasswordHash(newPass, user.getSalt()));
			userDao.update(user);
			
		} catch (Exception e) {
			System.err.println("Error on password changhing.");
			e.printStackTrace();
			return Response.serverError().build();
		}
		
		return addCors(Response.ok("Password has been changed."));
	}
	
	private String getName(JSONObject jsonObject) {
		String name = null;
		try {
			name = jsonObject.getString("newName");
		} catch (Exception e) {
			// name = null
		}
		return name;
	}
	
	private Response addCors(ResponseBuilder rb) {
		return rb.header("Access-Control-Allow-Origin", "*")
    			.header("Access-Control-Allow-Credentials", "true")
    			.header("Access-Control-Allow-Headers", "origin, content-type, accept, authorization")
    			.header("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS, HEAD")
    			.build();
	}
}
