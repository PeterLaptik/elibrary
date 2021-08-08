package pl.model.cache;

import java.util.ArrayList;
import java.util.List;

public class SectionNode {
	public String name;
	public int id;
	public List<SectionNode> children = new ArrayList<SectionNode>();
	
	public SectionNode(int id, String name){
		this.id = id;
		this.name = name;
	}
	
	public void toConsole() {
		System.out.println(this.id + " - " + this.name);
		for(SectionNode i: children)
			i.toConsole(1);
	}
	
	public void toConsole(int level) {
		for(int i=0; i<=level; i++)
			System.out.print("-");
		
		System.out.println(this.id + " - " + this.name);
		for(SectionNode i: children)
			i.toConsole(level+1);
	}
}
