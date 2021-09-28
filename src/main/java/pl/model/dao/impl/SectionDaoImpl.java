package pl.model.dao.impl;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.internal.SessionCreationOptions;
import org.hibernate.query.Query;

import pl.model.cache.SectionCache;
import pl.model.dao.SectionDao;
import pl.model.entities.Section;
import pl.model.session.HibernateSessionFactory;

@Stateless
public class SectionDaoImpl implements SectionDao {
	private static final long serialVersionUID = 2624986924936925684L;

	@EJB
	SectionCache cache;
	
	static {
		Session session = HibernateSessionFactory.getSession().openSession();
		Transaction transaction = session.beginTransaction();
		Query<Section> query = session.createQuery("FROM Section WHERE section_name = :param", Section.class);
		query.setParameter("param", Section.ROOT_NAME);
		List<Section> list = query.getResultList();
		if(list.size()<1) {
			Section rootSection = new Section();
			rootSection.setName(Section.ROOT_NAME);
			session.save(rootSection);
		}
		transaction.commit();
		session.close();
	}
	
	
	public SectionDaoImpl() {
		
	}
	
	public boolean createSection(Section section) {
		if(section==null)
			return false;
		
		Section rootSection = findSectionByName(Section.ROOT_NAME);
		return addChild(rootSection, section);
	}

	public boolean addChild(Section proxyParent, Section child) {
		if((proxyParent==null)||(child==null))
			return false;
		
		// Create and write child
		Session session = HibernateSessionFactory.getSession().openSession();
		Transaction transaction = session.beginTransaction();
		// Get parent db-object
		Query<Section> query = session.createQuery("FROM Section WHERE section_id = :param", Section.class);
		query.setParameter("param", proxyParent.getId());
		List<Section> parentResult = query.getResultList();
		if(parentResult.size()<1)
			return false;
				
		Section parent = parentResult.get(0);
		List<Section> children = parent.getChildren();
		if(children==null) {
			children = new ArrayList<Section>();
			parent.setChildren(children);
		}
		session.update(parent);
		child.setParent(parent);
		children.add(child);
		session.save(child);
		transaction.commit();
		session.close();
		cache.updateSections();
		return true;
	}
	
	public void deleteSection(Section section) {
		if((section==null)||(section.getName().equals(Section.ROOT_NAME)))
			return;
		
		Session session = HibernateSessionFactory.getSession().openSession();
		Transaction transaction = session.beginTransaction();
		
		Query<Section> query = session.createQuery("FROM Section WHERE section_name = :param", Section.class);
		query.setParameter("param", section.getName());
		List<Section> list = query.getResultList();
		if(list.size()<1)
			return;
		
		Section sectionToDelete = list.get(0);
		Section parent = sectionToDelete.getParent();
		List<Section> siblings = parent.getChildren();
		siblings.remove(sectionToDelete);
		//sectionToDelete.setParent(null);
		session.delete(sectionToDelete);
		transaction.commit();
		session.close();
		cache.updateSections();
	}
	
	public Section findSectionByName(String name) {
		Section result = null;
		Session session = HibernateSessionFactory.getSession().openSession();
		//Transaction transaction = session.beginTransaction();
		Query<Section> query = session.createQuery("FROM Section WHERE section_name = :param", Section.class);
		query.setParameter("param", name);
		List<Section> list = query.getResultList();
		//transaction.commit();
		session.close();
		if(list.size()>0)
			result = list.get(0);
		
		return result;
	}
	
	public Section findSectionById(int id) {
		Section result = null;
		Session session = HibernateSessionFactory.getSession().openSession();
		Query<Section> query = session.createQuery("FROM Section WHERE section_id = :param", Section.class);
		query.setParameter("param", id);
		List<Section> list = query.getResultList();
		session.close();
		if(list.size()>0)
			result = list.get(0);
		
		return result;
	}
}
