package pl.route.rest;

import java.io.File;

import javax.ejb.EJB;
import javax.servlet.ServletContext;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.primefaces.json.JSONObject;

import pl.model.dao.BookDao;
import pl.model.entities.Book;

@Path("/books")
public class BookResource {
	@Context ServletContext context;
	
	@EJB
	BookDao bookDao;

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
    public void openedBook(String data) {
		try {
			JSONObject jsonObject = new JSONObject(data);
		} catch (Exception e) {
			System.err.println("Wrong opened book data:" + data);
		}
		
    }
}
