package pl.route.rest;

import javax.ejb.EJB;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import pl.model.cache.SectionCache;
import pl.model.dao.SectionDao;


@Path("/helloworld")
public class SectionsResource {
	@EJB
	SectionDao sectionDao;
	
	@EJB
	SectionCache sectionCache;
	
    @GET
    @Produces("text/plain")
    public String getClichedMessage() {
    	sectionCache.updateSections();
        return sectionCache.getSectionsJson().toString();
    }
    
}
