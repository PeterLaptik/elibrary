package pl.model.dao.views;

import java.io.Serializable;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.ejb.EJB;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

import org.hibernate.Session;
import org.hibernate.query.Query;
import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.TreeNode;

import pl.model.dao.SectionDao;
import pl.model.entities.Section;
import pl.model.session.HibernateSessionFactory;

@Named("sectionService")
@ViewScoped
public class SectionService implements Serializable {
	private static final long serialVersionUID = 7694019109747343749L;
	private TreeNode root;
	private TreeNode selectedNode;

	@EJB
	SectionDao sectionDao;
	
	public TreeNode getRoot() {
		root = buildStructure();
		return root;
	}
	
	public TreeNode getSelectedNode() {
		return selectedNode;
	}

	public void setSelectedNode(TreeNode selectedNode) {
		this.selectedNode = selectedNode;
	}
	
	public void deleteNode() {
		if(selectedNode==null)
			return;
		
		SectionNode nodeToDelete = (SectionNode) selectedNode.getData();
		Section sectionToDelete = sectionDao.findSectionByName(nodeToDelete.getName());
		sectionDao.deleteSection(sectionToDelete);
	}
	
	public void appendNode() {
		System.out.println("Append node");
	}
	
	private TreeNode buildStructure() {
		Section root = sectionDao.findSectionByName(Section.ROOT_NAME);
		TreeNode rootNode = new DefaultTreeNode(new SectionNode(root.getName(), root.getId()), null);
		rootNode.setExpanded(true);
		TreeNode rootToShow = new DefaultTreeNode(new SectionNode(root.getName(), root.getId()), rootNode);
		rootToShow.setExpanded(true);
		addSubTreeFor(root, rootToShow);
		return rootNode;
	}
	
	private void addSubTreeFor(Section section, TreeNode parentNode) {
		Session session = HibernateSessionFactory.getSession().openSession();
		Query<Section> query = session.createQuery("FROM Section WHERE section_name = :param", Section.class);
		query.setParameter("param", section.getName());
		List<Section> list = query.getResultList();
		if(list.size()<1)
			return;
		
		Section sectionObject = list.get(0);
		List<Section> children = sectionObject.getChildren();
		Collections.sort(children, new Comparator<Section>() {
			@Override
			public int compare(Section o1, Section o2) {
				return o1.getName().compareTo(o2.getName());
			}
		});
		
		for(Section i: children) {
			TreeNode subNode = new DefaultTreeNode(new SectionNode(i.getName(), i.getId()), parentNode);
			subNode.setExpanded(true);
			addSubTreeFor(i, subNode);
		}
		session.close();
	}
}
