package pl.route.rest;

import java.io.File;

import javax.ejb.EJB;
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

import pl.model.dao.BookDao;
import pl.model.dao.UserDao;
import pl.model.dao.UserHistoryDao;
import pl.model.dao.UserSessionDao;
import pl.model.entities.Book;
import pl.model.entities.User;
import pl.model.entities.UserHistory;
import pl.model.entities.UserSession;

@Path("/books")
public class BookResource {
	@Context ServletContext context;
	
	@EJB
	BookDao bookDao;
	
	@EJB
	UserDao userDao;
	
	@EJB
	UserSessionDao sessionDao;
	
	@EJB
	UserHistoryDao userHistoryDao;

	@GET
    @Path("/{bookId}")
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    public Response getBooks(@PathParam("bookId") int id) {
		Book book = bookDao.getBookById(id);
		String directory = context.getInitParameter(Book.BOOKS_VOLUME);
		if (directory.charAt(directory.length()-1)!='\\' && directory.charAt(directory.length()-1)!='/')
			directory += File.separator;
		
		File file = book!=null ? new File(directory + book.getFileName()) : null;
		if(file.exists()==false) {
			// TODO redirect to error page
			file = null;
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
    public Response openedBook(String data, @CookieParam(UserSession.FIELD_SESSION_UUID) Cookie cookie) {
		try {
			JSONObject jsonObject = new JSONObject(data);
			
			UserSession session = sessionDao.findSessionByUuid(cookie.getValue());
			if(session==null) {
				System.out.println("No session found!");
				return Response.serverError().build();
			}
			
			int userId = session.getUserId();
			User user = userDao.findUserBuId(userId);
			Book book = bookDao.getBookById(jsonObject.getInt("id"));
			
			System.out.println(jsonObject.toString() + "bookId:" + book.getId() + " - userId: " + userId);
			UserHistory userHistory = new UserHistory();
			userHistory.setUser(user);
			userHistory.setBook(book);
			userHistoryDao.createStamp(userHistory);
			
		} catch (Exception e) {
			System.err.println("Wrong opened book data:" + data);
			return Response.serverError().build();
		}
		return Response.ok().build();
    }
}
