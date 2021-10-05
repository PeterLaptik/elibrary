package pl.elibrary.route.rest;

import java.util.List;

import javax.ejb.EJB;
import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;

import pl.elibrary.model.cache.SectionCache;
import pl.elibrary.model.dao.BookDao;
import pl.elibrary.model.dao.SectionDao;
import pl.elibrary.model.entities.Book;


@Path("/sections")
public class SectionsResource {
	private static int DEFAULT_WINDOW_SIZE = 5;
	
	@EJB
	SectionCache sectionCache;
	
	@EJB
	SectionDao sectionDao;
	
	@EJB
	BookDao bookDao;
	
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getSections() {
    	sectionCache.updateSections();
        return addCors(Response.ok(sectionCache.getSectionsJson()));
    } 
    
    @GET
    @Path("/books/{sectionId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getBooks(@PathParam("sectionId") int id) {
    	List<Book> bookList = bookDao.getBooksBySectionId(id);
    	
    	// Book list
    	JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();
    	for(Book i: bookList) {
    		arrayBuilder.add(i.toJsonBuilder());
    	}
    	JsonObjectBuilder bookListBuilder = Json.createObjectBuilder();
    	bookListBuilder.add("books", arrayBuilder);
    	// Book number
    	Integer booksQuantity = bookDao.getBookQuantity(id);
    	bookListBuilder.add("booksNumber:", booksQuantity);
    	// Pages number
    	Integer pageNumber = 1;

    	bookListBuilder.add("pagesNumber:", pageNumber);
    	
    	JsonObject result = bookListBuilder.build();
    	
        return addCors(Response.ok(result));
    }
    
    @GET
    @Path("/books/{sectionId}/{pageId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getBooks(@PathParam("sectionId") int sectionId, @PathParam("pageId") int pageId) {
    	List<Book> bookList = bookDao.getBooksBySectionId(sectionId, DEFAULT_WINDOW_SIZE, pageId);
    	
    	// Book list
    	JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();
    	for(Book i: bookList) {
    		arrayBuilder.add(i.toJsonBuilder());
    	}
    	JsonObjectBuilder bookListBuilder = Json.createObjectBuilder();
    	bookListBuilder.add("books", arrayBuilder);
    	// Book number
    	Integer booksQuantity = bookDao.getBookQuantity(sectionId);
    	bookListBuilder.add("booksNumber", booksQuantity);
    	// Pages number
    	Integer pageNumber = booksQuantity>0 ? booksQuantity/DEFAULT_WINDOW_SIZE : 0;
    	if(booksQuantity%DEFAULT_WINDOW_SIZE>0)
    		pageNumber++;
    	
    	bookListBuilder.add("pagesNumber", pageNumber);
    	bookListBuilder.add("currentPage", pageId);
    	
    	JsonObject result = bookListBuilder.build();
    	
        return addCors(Response.ok(result));
    }
    
    private Response addCors(ResponseBuilder rb) {
		return rb.header("Access-Control-Allow-Origin", "*")
    			.header("Access-Control-Allow-Credentials", "true")
    			.header("Access-Control-Allow-Headers", "origin, content-type, accept, authorization")
    			.header("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS, HEAD")
    			.build();
	}
}
