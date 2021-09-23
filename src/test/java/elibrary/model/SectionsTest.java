package elibrary.model;

import java.util.List;

import org.junit.Assert;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import pl.model.cache.SectionCache;
import pl.model.cache.SectionCacheImpl;
import pl.model.dao.BookDao;
import pl.model.dao.SectionDao;
import pl.model.entities.Book;
import pl.model.entities.Section;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class SectionsTest {
	static {
		BookDao bookDao = TestInjectionMock.getBookDao();
		List<Book> books = bookDao.findBooksByName("Test book");
		for(Book book: books)
			bookDao.deleteBook(book);
		
		SectionDao dao = TestInjectionMock.getSectionDao();
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
		SectionDao dao = TestInjectionMock.getSectionDao();
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
		SectionDao dao = TestInjectionMock.getSectionDao();
		Section bookSection = dao.findSectionByName("test_section");
		BookDao bookDao = TestInjectionMock.getBookDao();
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
		BookDao bookDao = TestInjectionMock.getBookDao();
		List<Book> books = bookDao.findBooksByName("Test book");
		for(Book book: books)
			bookDao.deleteBook(book);
		
		SectionDao dao = TestInjectionMock.getSectionDao();
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
}
