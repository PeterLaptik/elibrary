package pl.model.cache;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;

import org.hibernate.Session;
import org.hibernate.query.Query;

import pl.model.cache.objects.SectionNode;
import pl.model.dao.SectionDao;
import pl.model.entities.Section;
import pl.model.session.HibernateSessionFactory;

@Singleton
public class SectionCacheImpl implements SectionCache {
	private static final long serialVersionUID = -8002568672223868377L;
	private static final String ATTR_NAME = "name";
	private static final String ATTR_ID = "id";
	private static final String ATTR_CHILDREN = "children";
	
	private SectionNode rootSection = null;
	private JsonObject sectionTree;
	
	@EJB
	SectionDao sectionDao;
	
	@PostConstruct
	void init() {
		rootSection = buildSectionTree();
		sectionTree = sectionsToJson();
	}
	
	@Override
	public void updateSections() {
		rootSection = buildSectionTree();
		sectionTree = sectionsToJson();
	}
	
	@Override
	public SectionNode getSections() {
		return rootSection;
	}
	
	@Override
	public JsonObject getSectionsJson() {
		if(sectionTree==null) {
			rootSection = buildSectionTree();
			sectionTree = sectionsToJson();
		}
		return sectionTree;
	}
	
	private SectionNode buildSectionTree() {
    	Section rootSection = sectionDao.findSectionByName(Section.ROOT_NAME);
    	SectionNode root = new SectionNode(rootSection.getId(), rootSection.getName());
    	addSubTreeFor(root);
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
    
    private JsonObject sectionsToJson() {
    	if(rootSection==null)
    		return null;
    	
    	JsonObjectBuilder rootBuilder = Json.createObjectBuilder();
    	rootBuilder.add(ATTR_ID, rootSection.id)
    				.add(ATTR_NAME, rootSection.name);
    	

    	JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();
		for (SectionNode child : rootSection.children)
			arrayBuilder.add(sectionsToJson(child));
		
		JsonArray childrenArray  = arrayBuilder.build();
    	if(childrenArray.size()>0)
    		rootBuilder.add(ATTR_CHILDREN, childrenArray);
    	
    	JsonObject root =  rootBuilder.build();
    	return root;
    }
    
    private JsonObjectBuilder sectionsToJson(SectionNode node) {
    	JsonObjectBuilder builder = Json.createObjectBuilder();
    	builder.add(ATTR_ID, node.id)
				.add(ATTR_NAME, node.name);
    	
    	JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();
    	for(SectionNode child: node.children)
    		arrayBuilder.add(sectionsToJson(child));
    	
    	JsonArray childrenArray  = arrayBuilder.build();
    	if(childrenArray.size()>0)
    		builder.add(ATTR_CHILDREN, childrenArray);
    	
    	return builder;
    }
}
