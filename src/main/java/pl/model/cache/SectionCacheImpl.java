package pl.model.cache;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;

import org.hibernate.Session;
import org.hibernate.query.Query;

import pl.model.dao.SectionDao;
import pl.model.entities.Section;
import pl.model.session.HibernateSessionFactory;

@Singleton
public class SectionCacheImpl implements SectionCache {
	private static final long serialVersionUID = -8002568672223868377L;
	private SectionNode rootSection = null;
	
	@EJB
	SectionDao sectionDao;
	
	@PostConstruct
	void init() {
		rootSection = buildSectionTree();
	}
	
	@Override
	public void updateSections() {
		rootSection = buildSectionTree();
	}
	
	@Override
	public SectionNode getSections() {
		return rootSection;
	}
	
	@Override
	public JsonObject getSectionsJson() {
		JsonObject json = Json.createObjectBuilder()
					 .add("name", "root")
				     .add("id", 3).build();
		
		return json;
	}
	
	private SectionNode buildSectionTree() {
    	Section rootSection = sectionDao.findSectionByName(Section.ROOT_NAME);
    	SectionNode root = new SectionNode(rootSection.getId(), rootSection.getName());
    	addSubTreeFor(root);
    	root.toConsole();
    	return root;
    }
    
    private void addSubTreeFor(SectionNode parent) {
    	Session session = HibernateSessionFactory.getSession().openSession();
		try {
			Query<Section> query = session.createQuery("FROM Section WHERE section_name = :param", Section.class);
			query.setParameter("param", parent.name);
			List<Section> list = query.getResultList();
			if (list.size() < 1)
				return;

			Section sectionObject = list.get(0);
			List<Section> children = sectionObject.getChildren();
			Collections.sort(children, new Comparator<Section>() {
				@Override
				public int compare(Section o1, Section o2) {
					return o1.getName().compareTo(o2.getName());
				}
			});

			for (Section i : children) {
				SectionNode subNode = new SectionNode(i.getId(), i.getName());
				parent.children.add(subNode);
				addSubTreeFor(subNode);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		session.close();
    }
    
    private void sectionsToJson() {
    	if(rootSection==null)
    		return;
    	
    	JsonObjectBuilder json = Json.createObjectBuilder();
    	json.add("id", rootSection.id)
    		.add("name", rootSection.name);
    	
    }
    
    private void sectionsToJson(JsonObjectBuilder json) {

    	
    }
}
