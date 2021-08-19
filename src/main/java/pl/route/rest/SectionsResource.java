package pl.route.rest;

import javax.ejb.EJB;
import javax.json.JsonObject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import pl.model.cache.SectionCache;
import pl.model.dao.SectionDao;


@Path("/sections")
public class SectionsResource {
	@EJB
	SectionDao sectionDao;
	
	@EJB
	SectionCache sectionCache;
	
    @GET
    @Produces("application/json")
    public Response getClichedMessage() {
    	sectionCache.updateSections();
        return Response.ok(sectionCache.getSectionsJson())
        			.header("Access-Control-Allow-Origin", "*")
        			.header("Access-Control-Allow-Credentials", "true")
        			.header("Access-Control-Allow-Headers", "origin, content-type, accept, authorization")
        			.header("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS, HEAD")
        			.build();
    }
}
