package pl.elibrary.model.cache.objects;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents one section node of a section tree.
 * Root node is being kept in SectionCache object
 */
public class SectionNode {
	public String name;
	public int id;
	
	/** Children nodes **/
	public List<SectionNode> children = new ArrayList<SectionNode>();
	
	public SectionNode(int id, String name){
		this.id = id;
		this.name = name;
	}
	
	/**
	 * Debug output of full tree (sub-tree)
	 */
	public void toConsole() {
		System.out.println(this.id + " - " + this.name);
		for(SectionNode i: children)
			i.toConsole(1);
	}
	
	/**
	 * Debug output
	 */
	public void toConsole(int level) {
		for(int i=0; i<=level; i++)
			System.out.print("-");
		
		System.out.println(this.id + " - " + this.name);
		for(SectionNode i: children)
			i.toConsole(level+1);
	}
}
