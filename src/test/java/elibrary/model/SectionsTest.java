package elibrary.model;

import java.lang.reflect.Field;
import java.util.List;

import org.junit.Assert;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import pl.model.cache.SectionCache;
import pl.model.cache.SectionCacheImpl;
import pl.model.dao.impl.BookDaoImpl;
import pl.model.dao.impl.SectionDaoImpl;
import pl.model.entities.Book;
import pl.model.entities.Section;
import pl.view.jsf.beans.SectionService;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class SectionsTest {
	private static SectionCache commonCache = new SectionCacheImpl();
	
	static {
		BookDaoImpl bookDao = new BookDaoImpl();
		List<Book> books = bookDao.findBooksByName("Test book");
		for(Book book: books)
			bookDao.deleteBook(book);
		
		SectionDaoImpl dao = new SectionDaoImpl();
		assignSectionCache(dao);
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
		
		try {
			// SectionsCache
			Field field = commonCache.getClass().getDeclaredField("sectionDao");
			field.setAccessible(true);
			field.set(commonCache, new SectionDaoImpl());
		} catch (NoSuchFieldException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void test1_userCreatingTestDefDao() {
		SectionDaoImpl dao = new SectionDaoImpl();
		SectionsTest.assignSectionCache(dao);
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
	public void test2_createBooks() {
		SectionDaoImpl dao = new SectionDaoImpl();
		SectionsTest.assignSectionCache(dao);
		Section bookSection = dao.findSectionByName("test_section");
		BookDaoImpl bookDao = new BookDaoImpl();
		Book book = new Book();
		book.setName("Test book");
		book.setDescription("desc");
		book.setFormat("pdf");
		book.setFileName("001.pdf");
		book.setSection(bookSection);
		Assert.assertTrue(bookDao.createBook(book));
		
		List<Book> createdBook = bookDao.findBooksByName(book.getName());
		Assert.assertTrue(createdBook.size()>0);
	}
	
	@Test
	public void test3_deleteTestData() {
		BookDaoImpl bookDao = new BookDaoImpl();
		List<Book> books = bookDao.findBooksByName("Test book");
		for(Book book: books)
			bookDao.deleteBook(book);
		
		SectionDaoImpl dao = new SectionDaoImpl();
		SectionsTest.assignSectionCache(dao);
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
	
	
	private static void assignSectionCache(SectionDaoImpl dao) {
		try {
			// SectionsCache
			Field field = dao.getClass().getDeclaredField("cache");
			field.setAccessible(true);
			field.set(dao, commonCache);
		} catch (NoSuchFieldException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
