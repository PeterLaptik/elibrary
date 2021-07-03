package elibrary.model;

import org.junit.Test;

import pl.model.dao.ChapterDaoImpl;
import pl.model.entities.Chapter;

public class ChaptersTest {
	
	static {
		ChapterDaoImpl dao = new ChapterDaoImpl();
		Chapter chapter = dao.findChapterByName("test_chapter");
		dao.deleteChapter(chapter);
		
		chapter = dao.findChapterByName("test_child");
		dao.deleteChapter(chapter);
	}
	
	@Test
	public void test1_userCreatingTestDefDao() {
		ChapterDaoImpl dao = new ChapterDaoImpl();
		Chapter chapter = new Chapter();
		chapter.setName("test_chapter");
		dao.createChapter(chapter);
		
		Chapter child = new Chapter();
		child.setName("test_child_1");
		dao.addChild(chapter, child);
		
		Chapter subChild = new Chapter();
		subChild.setName("sub_child_11");
		dao.addChild(child, subChild);
		
		subChild = new Chapter();
		subChild.setName("sub_child_12");
		dao.addChild(child, subChild);
		
		child = new Chapter();
		child.setName("test_child_2");
		dao.addChild(chapter, child);
		
		subChild = new Chapter();
		subChild.setName("sub_child_1");
		dao.addChild(child, subChild);
		
		child = new Chapter();
		child.setName("test_child_2");
		dao.addChild(chapter, child);
		
		subChild = new Chapter();
		subChild.setName("sub_child_3");
		dao.addChild(child, subChild);
	}
}
