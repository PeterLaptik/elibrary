package pl.route.rest;

import java.util.Collections;
import java.util.Comparator;
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

import pl.model.cache.SectionCache;
import pl.model.dao.BookDao;
import pl.model.dao.SectionDao;
import pl.model.entities.Book;


@Path("/sections")
public class SectionsResource {
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
        return Response.ok(sectionCache.getSectionsJson())
        			.header("Access-Control-Allow-Origin", "*")
        			.header("Access-Control-Allow-Credentials", "true")
        			.header("Access-Control-Allow-Headers", "origin, content-type, accept, authorization")
        			.header("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS, HEAD")
        			.build();
    } 
    
    @GET
    @Path("/books/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getBooks(@PathParam("id") int id) {
    	List<Book> bookList = bookDao.getBooksBySectionId(id);
    	Collections.sort(bookList, new Comparator<Book>() {
			@Override
			public int compare(Book o1, Book o2) {
				return o1.getName().compareTo(o2.getName());
			}
		});
    	
    	JsonObjectBuilder listBuilder = Json.createObjectBuilder();
    	JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();
    	for(Book i: bookList) {
    		JsonObjectBuilder builder = Json.createObjectBuilder();
    		builder.add("name", i.getName());
    		builder.add("description", i.getDescription());
    		builder.add("author", i.getAuthors());
    		builder.add("code", i.getCode());
    		arrayBuilder.add(builder);
    	}
    	listBuilder.add("books", arrayBuilder);
    	JsonObject result = listBuilder.build();
    	
        return Response.ok(result)
        			.header("Access-Control-Allow-Origin", "*")
        			.header("Access-Control-Allow-Credentials", "true")
        			.header("Access-Control-Allow-Headers", "origin, content-type, accept, authorization")
        			.header("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS, HEAD")
        			.build();
    } 
}
