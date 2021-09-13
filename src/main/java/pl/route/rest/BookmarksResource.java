package pl.route.rest;

import java.util.List;

import javax.ejb.EJB;
import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.servlet.ServletContext;
import javax.ws.rs.Consumes;
import javax.ws.rs.CookieParam;
import javax.ws.rs.DELETE;
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
import pl.model.dao.BookmarkDao;
import pl.model.dao.UserDao;
import pl.model.entities.Book;
import pl.model.entities.Bookmark;
import pl.model.entities.User;
import pl.model.entities.UserSession;

@Path("/bookmarks")
public class BookmarksResource {
	@Context ServletContext context;
	
	@EJB
	private SessionCache sessionCache;
	
	@EJB
	private BookDao bookDao;
	
	@EJB
	private UserDao userDao;
	
	@EJB
	private BookmarkDao bookmarkDao;
	
	@GET
	@Path("/{bookId}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getBookmarks(@PathParam("bookId") int bookId, 
			@CookieParam(UserSession.FIELD_SESSION_UUID) Cookie cookie) {
		
		JsonObject bookmarkList = null;
		try {
			UserSession session = sessionCache.findSessionByUuid(cookie.getValue());
			if(session==null) {
				System.err.println("No session found!");
				return Response.serverError().build();
			}
			
			List<Bookmark> bookmarks = bookmarkDao.getBookmarks(bookId, session.getUserId());
			
			JsonObjectBuilder builder = Json.createObjectBuilder();
			JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();
			for(Bookmark i: bookmarks)
				arrayBuilder.add(i.toJsonBuilder());
			builder.add("bookmarks", arrayBuilder);
			bookmarkList = builder.build();

		} catch (Exception e) {
			e.printStackTrace();
			System.err.println("Error on getting bookmarks for" + bookId);
			return Response.serverError().build();
		}
		
		return Response.ok(bookmarkList)
    			.header("Access-Control-Allow-Origin", "*")
    			.header("Access-Control-Allow-Credentials", "true")
    			.header("Access-Control-Allow-Headers", "origin, content-type, accept, authorization")
    			.header("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS, HEAD")
    			.build();
	}
	
	@POST
	@Path("/post/{bookId}")
    @Produces(MediaType.TEXT_HTML)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response writeBookmark(String data, @PathParam("bookId") int bookId,
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
			String text = jsonObject.getString("text");
			int page = jsonObject.getInt("page");
			if(text==null || text.equals(""))
				throw new Exception("No bookmark info recieved");
			
			Bookmark bookmark = new Bookmark();
			bookmark.setUser(user);
			bookmark.setBook(book);
			bookmark.setPage(page);
			bookmark.setName(text);
			bookmarkDao.createBookmark(bookmark);
			
		} catch (Exception e) {
			e.printStackTrace();
			System.err.println("Error on writing bookmark:" + data);
			return Response.serverError().build();
		}
		return Response.ok().build();
    }
	
	@DELETE
	@Path("/delete/{bookId}")
    @Produces(MediaType.TEXT_HTML)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response deleteBookmark(String data, @PathParam("bookId") int bookId,
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
			String text = jsonObject.getString("bookmark_text");
			int page = jsonObject.getInt("page_number");
			if(text==null || text.equals(""))
				throw new Exception("No bookmark info recieved");
			
			Bookmark bookmark = new Bookmark();
			bookmark.setUser(user);
			bookmark.setBook(book);
			bookmark.setPage(page);
			bookmark.setName(text);
			bookmarkDao.deleteBookmark(bookmark);
			
		} catch (Exception e) {
			e.printStackTrace();
			System.err.println("Error on deleting bookmark:" + data);
			return Response.serverError().build();
		}
		return Response.ok().build();
    }
}
