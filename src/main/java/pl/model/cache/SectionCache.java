package pl.model.cache;

import java.io.Serializable;
import javax.json.JsonObject;

import pl.model.cache.objects.SectionNode;

/**
 * Keeps built section tree
 */
public interface SectionCache extends Serializable {
	/**
	 * Updates section tree data
	 * (for example, after new section is added)
	 */
	public void updateSections();
	
	/**
	 * Returns section tree.
	 * See SectionNode class for more details about tree-object
	 */
	public SectionNode getSections();
	
	/**
	 * Returns section tree as a JSON-object
	 */
	public JsonObject getSectionsJson();
}
