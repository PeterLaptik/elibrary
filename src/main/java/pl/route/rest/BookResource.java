package pl.route.rest;

import java.io.File;
import java.util.List;

import javax.ejb.EJB;
import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObjectBuilder;
import javax.servlet.ServletContext;
import javax.ws.rs.Consumes;
import javax.ws.rs.CookieParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Cookie;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.primefaces.json.JSONObject;

import pl.model.cache.SessionCache;
import pl.model.dao.BookDao;
import pl.model.dao.UserDao;
import pl.model.dao.UserHistoryDao;
import pl.model.entities.Book;
import pl.model.entities.User;
import pl.model.entities.UserHistory;
import pl.model.entities.UserSession;

@Path("/books")
public class BookResource {
	@Context ServletContext context;
	
	@EJB
	private BookDao bookDao;
	
	@EJB
	private UserDao userDao;
	
	@EJB
	private SessionCache sessionCache;
	
	@EJB
	private UserHistoryDao userHistoryDao;
	
	
	@GET
    @Path("/{bookId}")
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    public Response getBooks(@PathParam("bookId") int id, 
    							@CookieParam(UserSession.FIELD_SESSION_UUID) Cookie cookie) {
		UserSession session = sessionCache.findSessionByUuid(cookie.getValue());
		if(session==null)
			return Response.serverError().build();
		
		Book book = bookDao.getBookById(id);
		String directory = context.getInitParameter(Book.BOOKS_VOLUME);
		if (directory.charAt(directory.length()-1)!='\\' && directory.charAt(directory.length()-1)!='/')
			directory += File.separator;
		
		File file = book!=null ? new File(directory + book.getFileName()) : null;
		if(file.exists()==false) {
			file = null;
			System.err.println("Book does not exist in the database");
			return Response.serverError().build();
		}
		
		return Response.ok(file, MediaType.APPLICATION_OCTET_STREAM)
				.header("Content-Disposition", "attachment; filename=\"" + (file!=null ? file.getName() : "unknown") + "\"")
				.header("Access-Control-Allow-Origin", "*").header("Access-Control-Allow-Credentials", "true")
				.header("Access-Control-Allow-Headers", "origin, content-type, accept, authorization")
				.header("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS, HEAD")
				.build();
    }
	
	
	@POST
	@Path("/bookhistory")
    @Produces(MediaType.TEXT_HTML)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response writeOpenedBookHistory(String data, 
    										@CookieParam(UserSession.FIELD_SESSION_UUID) Cookie cookie) {
		try {
			UserSession session = sessionCache.findSessionByUuid(cookie.getValue());
			if(session==null) {
				System.err.println("No session found!");
				return Response.serverError().build();
			}
			
			JSONObject jsonObject = new JSONObject(data);
			int userId = session.getUserId();
			int bookId = jsonObject.getInt("id");
			
			String maxVal = context.getInitParameter(UserHistory.MAX_BOOKS_IN_HISTORY);
			Integer maxBooks = null;
			try {
				maxBooks = Integer.parseInt(maxVal);
			} catch (Exception e) {
				System.err.println("Wrong value of MAX_BOOKS_IN_HISTORY in web.xml: " + maxVal);
			}
			userHistoryDao.createStamp(bookId, userId, maxBooks);
			
		} catch (Exception e) {
			System.err.println("Wrong opened book history data:" + data);
			return Response.serverError().build();
		}
		return Response.ok().build();
    }
	
	
	@POST
	@Path("/bookhistory/{bookId}")
    @Produces(MediaType.TEXT_HTML)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response writeOpenedBookPageHistory(String data, @PathParam("bookId") int bookId,
    											@CookieParam(UserSession.FIELD_SESSION_UUID) Cookie cookie) {
		try {
			UserSession session = sessionCache.findSessionByUuid(cookie.getValue());
			if(session==null) {
				System.err.println("No session found!");
				return Response.serverError().build();
			}
			
			JSONObject jsonObject = new JSONObject(data);
			int userId = session.getUserId();
			User user = userDao.findUserById(userId);
			Book book = bookDao.getBookById(bookId);
			
			UserHistory userHistory = new UserHistory();
			userHistory.setUser(user);
			userHistory.setBook(book);
			userHistory.setPage(jsonObject.getInt("page"));
			userHistoryDao.updateStamp(userHistory);
		} catch (Exception e) {
			System.err.println("Wrong page opened data:" + data);
			return Response.serverError().build();
		}
		return Response.ok().build();
    }
	
	
	@GET
    @Path("/pagehistory/{bookId}")
	@Produces(MediaType.TEXT_PLAIN)
    public int getLastPageForBook(@PathParam("bookId") int bookId, 
    						@CookieParam(UserSession.FIELD_SESSION_UUID) Cookie cookie) {
		
		UserSession session = sessionCache.findSessionByUuid(cookie.getValue());
		if(session==null)
			return 0;
		
		int userId = session.getUserId();
		int page = userHistoryDao.getLastPageFor(bookId, userId);
		return page;
    }
	
	@GET
	@Path("/bookhistory")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.TEXT_HTML)
	public Response getBookHistory(@CookieParam(UserSession.FIELD_SESSION_UUID) Cookie cookie) {
		UserSession session = sessionCache.findSessionByUuid(cookie.getValue());
		if(session==null)
			return Response.serverError().build();
		
		JsonObjectBuilder bookListBuilder = Json.createObjectBuilder();
		try {
			int userId = session.getUserId();
			List<Book> books = userHistoryDao.getHistoryBooks(userId);
			JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();
	    	for(Book i: books) {
	    		arrayBuilder.add(i.toJsonBuilder());
	    	}
	    	bookListBuilder.add("books", arrayBuilder);
	    	
		} catch (Exception e) {
			e.printStackTrace();
			return Response.serverError().build();
		}
		return Response.ok(bookListBuilder.build())
    			.header("Access-Control-Allow-Origin", "*")
    			.header("Access-Control-Allow-Credentials", "true")
    			.header("Access-Control-Allow-Headers", "origin, content-type, accept, authorization")
    			.header("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS, HEAD")
    			.build();
	}
}
