package edu.uci.isr.archstudio4.comp.xarchcs.changesetstatusrelationships;

import edu.uci.isr.xarchflat.ObjRef;


/**
 * Interface for ChangeSetStatusRelationships.
 * Retrieves information about changeset status relationships.
 * 
 * @author Kari
 * 
 */
public interface IChangeSetStatusRelationships {
	
	public ObjRef[] getAllRelationships(ObjRef xArchRef);

}
