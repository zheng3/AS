package edu.uci.isr.archstudio4.comp.xarchcs.changesetidrelationships;

import edu.uci.isr.xarchflat.ObjRef;


/**
 * Interface for ChangeRelationships.
 * Retrieves information about changeset id relationships.
 * 
 * @author Kari
 * 
 */
public interface IChangeSetIdRelationships {
	
	public ObjRef[] getAllRelationships(ObjRef xArchRef);

}
