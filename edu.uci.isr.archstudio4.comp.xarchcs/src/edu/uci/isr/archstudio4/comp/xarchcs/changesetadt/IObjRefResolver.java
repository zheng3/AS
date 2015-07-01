package edu.uci.isr.archstudio4.comp.xarchcs.changesetadt;

import edu.uci.isr.xarchflat.ObjRef;
import edu.uci.isr.xarchflat.XArchFlatQueryInterface;

/*
 * Simplifying Assumptions:
 * 
 * - Resolvers may only use data from Attributes and Elements, not ElementMany.
 * 
 * - Resvolers may only use data in the resolving element or its children. 
 *   A resolver may not use data in parent elements.
 *   
 * - Reference values are not changed after their initial calculation. 
 *   If they are, then the entity of that new reference value is 
 *   a completely separate entity from, and unrelated to, the original entity; 
 */

public interface IObjRefResolver {

	public boolean canResolve(String reference);

	public String getReference(XArchFlatQueryInterface xarch, ObjRef objRef);

	public boolean canResolve(XArchFlatQueryInterface xarch, ObjRef objRef);

	public ObjRef resolveObjRef(XArchFlatQueryInterface xarch, String reference, ObjRef parentRef, ObjRef[] childRefs);
}
