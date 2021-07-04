package pl.model.dao.impl;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import pl.model.dao.SectionDao;
import pl.model.entities.Section;
import pl.model.session.HibernateSessionFactory;

@Stateless
public class SectionDaoImpl implements SectionDao {
	private static final long serialVersionUID = 2624986924936925684L;

	public SectionDaoImpl() {
		
	}
	
	public boolean createSection(Section section) {
		if(section==null)
			return false;
		
		Section rootSection = findSectionByName(Section.ROOT_NAME);
		if(rootSection==null) {
			rootSection = new Section();
			rootSection.setName(Section.ROOT_NAME);
			Session session = HibernateSessionFactory.getSession().openSession();
			Transaction transaction = session.beginTransaction();
			session.save(rootSection);
			transaction.commit();
			session.close();
		}
		
		return addChild(rootSection, section);
	}

	public boolean addChild(Section parent, Section child) {
		if((parent==null)||(child==null))
			return false;
		
		// Create and write child
		Session session = HibernateSessionFactory.getSession().openSession();
		Transaction transaction = session.beginTransaction();
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
		sectionToDelete.setParent(null);
		session.delete(sectionToDelete);
		transaction.commit();
		session.close();
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
}
