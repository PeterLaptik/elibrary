package pl.route.rest;

import javax.ejb.EJB;
import javax.json.JsonObject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

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
    public JsonObject getClichedMessage() {
    	sectionCache.updateSections();
        return sectionCache.getSectionsJson();
    }
    
}
