package pl.model.cache;

import java.io.Serializable;
import javax.json.JsonObject;

public interface SectionCache extends Serializable {

	public void updateSections();
	
	public SectionNode getSections();
	
	public JsonObject getSectionsJson();
	
}
