package elibrary.model;

import java.lang.reflect.Field;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import pl.model.dao.impl.SectionDaoImpl;
import pl.model.dao.views.SectionService;
import pl.model.entities.Section;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class SectionsTest {
	
	static {
		SectionDaoImpl dao = new SectionDaoImpl();
		Section section = dao.findSectionByName("sub_child_3");
		dao.deleteSection(section);
		section = dao.findSectionByName("sub_child_1");
		dao.deleteSection(section);
		section = dao.findSectionByName("sub_child_11");
		dao.deleteSection(section);
		section = dao.findSectionByName("sub_child_12");
		dao.deleteSection(section);
		section = dao.findSectionByName("test_child_1");
		dao.deleteSection(section);
		section = dao.findSectionByName("test_child_2");
		dao.deleteSection(section);
		section = dao.findSectionByName("test_child_3");
		dao.deleteSection(section);
		section = dao.findSectionByName("test_child");
		dao.deleteSection(section);
		section = dao.findSectionByName("test_section");
		dao.deleteSection(section);
		section = dao.findSectionByName("test_section_5");
		dao.deleteSection(section);
	}
	
	@Test
	public void test1_userCreatingTestDefDao() {
		SectionDaoImpl dao = new SectionDaoImpl();
		Section section = new Section();
		section.setName("test_section");
		dao.createSection(section);
		
		section = new Section();
		section.setName("test_section_5");
		dao.createSection(section);
		
		Section child = new Section();
		child.setName("test_child_1");
		dao.addChild(section, child);
		
		Section subChild = new Section();
		subChild.setName("sub_child_11");
		dao.addChild(child, subChild);
		
		subChild = new Section();
		subChild.setName("sub_child_12");
		dao.addChild(child, subChild);
		
		child = new Section();
		child.setName("test_child_2");
		dao.addChild(section, child);
		
		subChild = new Section();
		subChild.setName("sub_child_1");
		dao.addChild(child, subChild);
		
		child = new Section();
		child.setName("test_child_3");
		dao.addChild(section, child);
		
		subChild = new Section();
		subChild.setName("sub_child_3");
		dao.addChild(child, subChild);
	}
	
	@Test
	public void tests_showSectionsTree() {
		SectionService service = new SectionService();
		Field field;
		try {
			field = service.getClass().getDeclaredField("sectionDao");
			field.setAccessible(true);
			field.set(service, new SectionDaoImpl());
		} catch (NoSuchFieldException e1) {
			e1.printStackTrace();
		} catch (SecurityException e1) {
			e1.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		service.getRoot();
	}
}
