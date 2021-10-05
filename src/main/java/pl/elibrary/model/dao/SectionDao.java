package pl.elibrary.model.dao;

import java.io.Serializable;

import pl.elibrary.model.entities.Section;

public interface SectionDao extends Serializable {
	
	public boolean createSection(Section section);
	
	public boolean addChild(Section parent, Section child);
	
	public void deleteSection(Section section);
	
	public Section findSectionByName(String name);
	
	public Section findSectionById(int id);
}
