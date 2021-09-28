package pl.route.rest;

import javax.ejb.EJB;
import javax.ws.rs.CookieParam;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Cookie;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

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
	
	@GET
    @Produces(MediaType.APPLICATION_JSON)
	Response getUserInfo(@CookieParam(UserSession.FIELD_SESSION_UUID) Cookie cookie) {
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
}
