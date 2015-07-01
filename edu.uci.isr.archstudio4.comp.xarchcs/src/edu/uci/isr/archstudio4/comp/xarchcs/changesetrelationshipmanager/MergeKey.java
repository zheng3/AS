package edu.uci.isr.archstudio4.comp.xarchcs.changesetrelationshipmanager;

import java.util.Set;

public class MergeKey {
	
	public enum RelationshipType {		
		OR_TYPE, AND_TYPE 
	}
	
	Set<RelationshipElement> signature;
	RelationshipType relType;


	public MergeKey(Set<RelationshipElement> signature,
			        RelationshipType relType) {
		super();
		this.signature = signature;
		this.relType = relType;
	}

	public int hashCode() {
		return relType.hashCode() + (31 * signature.hashCode());
	}
	
	
	public boolean equals (Object o) {
		if (o == null) return false;
		if (! (o instanceof MergeKey)) return false;
		MergeKey mk = (MergeKey) o;
		return ((signature.equals(mk.signature)) &&
				(relType.equals(mk.relType)));
	}
	
}
