package pl.model.dao;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import pl.model.entities.Chapter;
import pl.model.entities.ChapterChildren;
import pl.model.entities.User;
import pl.model.session.HibernateSessionFactory;

public class ChapterDaoImpl {
	
	public ChapterDaoImpl() {
		
	}
	
	public boolean createChapter(Chapter chapter) {
		if(chapter==null)
			return false;
		
		Session session = HibernateSessionFactory.getSession().openSession();
		Transaction transaction = session.beginTransaction();
		session.save(chapter);
		transaction.commit();
		session.close();
		return true;
	}

	public boolean addChild(Chapter parent, Chapter child) {
		if((parent==null)||(child==null))
			return false;
		
		// Create child
		createChapter(child);
		Session session = HibernateSessionFactory.getSession().openSession();
		
		Transaction transaction = session.beginTransaction();
		List<Chapter> children = parent.getChildren();
		if(children==null) {
			children = new ArrayList<Chapter>();
			parent.setChildren(children);
		}
		children.add(child);
		session.update(parent);
		transaction.commit();
		session.close();
		
		return true;
	}
	
	public void deleteChapter(Chapter chapter) {
		if(chapter==null)
			return;
		
		Session session = HibernateSessionFactory.getSession().openSession();
		Transaction transaction = session.beginTransaction();
		session.delete(chapter);
		transaction.commit();
		session.close();
	}
	
	public Chapter findChapterByName(String name) {
		Chapter result = null;
		
		Session session = HibernateSessionFactory.getSession().openSession();
		Transaction transaction = session.beginTransaction();
		Query<Chapter> query = session.createQuery("FROM Chapter WHERE chapter_name = :param", Chapter.class);
		query.setParameter("param", name);
		List<Chapter> list = query.getResultList();
		transaction.commit();
		session.close();
		
		if(list.size()>0)
			result = list.get(0);
		
		return result;
	}
	
	public void getChaptersTree() {
		
	}
}
