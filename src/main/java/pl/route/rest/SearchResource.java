package pl.route.rest;

import java.util.List;

import javax.ejb.EJB;
import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
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

import pl.model.cache.SessionCache;
import pl.model.dao.BookDao;
import pl.model.entities.Book;
import pl.model.entities.UserSession;

@Path("/search")
public class SearchResource {
	private static int DEFAULT_WINDOW_SIZE = 5;
	
	@EJB
	private BookDao bookDao;
	
	@EJB
	private SessionCache sessionCache;
	
	@POST
	@Path("/bookname/")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
	public Response findBooksByAuthor(String data,
									@CookieParam(UserSession.FIELD_SESSION_UUID) Cookie cookie) {
		
		UserSession session = sessionCache.findSessionByUuid(cookie.getValue());
		if(session==null)
			return Response.serverError().build();
		
		JsonObject result = null;
		try {
			JSONObject jsonObject = new JSONObject(data);
			int pageId = jsonObject.getInt("page");
			String search = jsonObject.getString("likeVal");
			
			// Search result capacity
			int quantity = bookDao.searchByNameQuantity(search);
			
			// Book list
			List<Book> bookList = bookDao.searchByNameBooks(search, DEFAULT_WINDOW_SIZE, pageId);
	    	JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();
	    	for(Book i: bookList) {
	    		arrayBuilder.add(i.toJsonBuilder());
	    	}
	    	JsonObjectBuilder bookListBuilder = Json.createObjectBuilder();
	    	bookListBuilder.add("books", arrayBuilder);
	    	bookListBuilder.add("booksNumber", quantity);
	    	// Pages number
	    	Integer pageNumber = quantity>0 ? quantity/DEFAULT_WINDOW_SIZE : 0;
	    	if(quantity%DEFAULT_WINDOW_SIZE>0)
	    		pageNumber++;
	    	
	    	bookListBuilder.add("pagesNumber", pageNumber);
	    	bookListBuilder.add("currentPage", pageId);
	    	result = bookListBuilder.build();
	    	
		} catch (Exception e) {
			e.printStackTrace();
			return Response.serverError().build();
		}
		
		if(result==null)
			return Response.serverError().build();
		
		return Response.ok(result)
    			.header("Access-Control-Allow-Origin", "*")
    			.header("Access-Control-Allow-Credentials", "true")
    			.header("Access-Control-Allow-Headers", "origin, content-type, accept, authorization")
    			.header("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS, HEAD")
    			.build();
	}
	
	@POST
	@Path("/author")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
	public Response findBooksByName(@CookieParam(UserSession.FIELD_SESSION_UUID) Cookie cookie,
									String object) {
		UserSession session = sessionCache.findSessionByUuid(cookie.getValue());
		if(session==null)
			return Response.serverError().build();
		
		return null;
	}
}
