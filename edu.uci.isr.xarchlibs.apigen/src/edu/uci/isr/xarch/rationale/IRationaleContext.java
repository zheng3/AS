/*
 * Copyright (c) 2000-2005 Regents of the University of California.
 * All rights reserved.
 *
 * This software was developed at the University of California, Irvine.
 *
 * Redistribution and use in source and binary forms are permitted
 * provided that the above copyright notice and this paragraph are
 * duplicated in all such forms and that any documentation,
 * advertising materials, and other materials related to such
 * distribution and use acknowledge that the software was developed
 * by the University of California, Irvine.  The name of the
 * University may not be used to endorse or promote products derived
 * from this software without specific prior written permission.
 * THIS SOFTWARE IS PROVIDED ``AS IS'' AND WITHOUT ANY EXPRESS OR
 * IMPLIED WARRANTIES, INCLUDING, WITHOUT LIMITATION, THE IMPLIED
 * WARRANTIES OF MERCHANTIBILITY AND FITNESS FOR A PARTICULAR PURPOSE.
 */
package edu.uci.isr.xarch.rationale;

import java.util.*;

import edu.uci.isr.xarch.*;

import org.w3c.dom.*;

import edu.uci.isr.xarch.IXArch;
import edu.uci.isr.xarch.IXArchContext;

/**
 * The context interface for the rationale package.
 * This interface is used to create objects that are used
 * in the rationale namespace.
 *
 * @author Automatically Generated by xArch apigen
 */
public interface IRationaleContext extends IXArchContext{

	/**
	 * Create an IRationale object in this namespace.
	 * @return New IRationale object.
	 */
	public IRationale createRationale();

	/**
	 * Brings an IRationale object created in another
	 * context into this context.
	 * @param value Object to recontextualize.
	 * @return <code>value</code> object in this namespace.
	 */
	public IRationale recontextualizeRationale(IRationale value);

	/**
	 * Create an edu.uci.isr.xarch.instance.IDescription object in this namespace.
	 * @return New edu.uci.isr.xarch.instance.IDescription object.
	 */
	public edu.uci.isr.xarch.instance.IDescription createDescription();

	/**
	 * Brings an edu.uci.isr.xarch.instance.IDescription object created in another
	 * context into this context.
	 * @param value Object to recontextualize.
	 * @return <code>value</code> object in this namespace.
	 */
	public edu.uci.isr.xarch.instance.IDescription recontextualizeDescription(edu.uci.isr.xarch.instance.IDescription value);

	/**
	 * Create an edu.uci.isr.xarch.instance.IXMLLink object in this namespace.
	 * @return New edu.uci.isr.xarch.instance.IXMLLink object.
	 */
	public edu.uci.isr.xarch.instance.IXMLLink createXMLLink();

	/**
	 * Brings an edu.uci.isr.xarch.instance.IXMLLink object created in another
	 * context into this context.
	 * @param value Object to recontextualize.
	 * @return <code>value</code> object in this namespace.
	 */
	public edu.uci.isr.xarch.instance.IXMLLink recontextualizeXMLLink(edu.uci.isr.xarch.instance.IXMLLink value);

	/**
	 * Create an IArchRationale object in this namespace.
	 * @return New IArchRationale object.
	 */
	public IArchRationale createArchRationale();

	/**
	 * Brings an IArchRationale object created in another
	 * context into this context.
	 * @param value Object to recontextualize.
	 * @return <code>value</code> object in this namespace.
	 */
	public IArchRationale recontextualizeArchRationale(IArchRationale value);

	/**
	 * Create an IArchChangeSetsRationale object in this namespace.
	 * @return New IArchChangeSetsRationale object.
	 */
	public IArchChangeSetsRationale createArchChangeSetsRationale();

	/**
	 * Brings an IArchChangeSetsRationale object created in another
	 * context into this context.
	 * @param value Object to recontextualize.
	 * @return <code>value</code> object in this namespace.
	 */
	public IArchChangeSetsRationale recontextualizeArchChangeSetsRationale(IArchChangeSetsRationale value);

	/**
	 * Promote an object of type <code>edu.uci.isr.xarch.changesets.IArchChangeSets</code>
	 * to one of type <code>IArchChangeSetsRationale</code>.  xArch APIs
	 * are structured in such a way that a regular cast is not possible
	 * to change interface types, so casting must be done through these
	 * promotion functions.  If the <code>edu.uci.isr.xarch.changesets.IArchChangeSets</code>
	 * object wraps a DOM element that is the base type, then the 
	 * <code>xsi:type</code> of the base element is promoted.  Otherwise, 
	 * it is left unchanged.
	 *
	 * This function also emits an <CODE>XArchEvent</CODE> with type
	 * PROMOTE_TYPE.  The source for this events is the pre-promoted
	 * IXArchElement object (should no longer be used), and the
	 * target is the post-promotion object.  The target name is
	 * the name of the interface class that was the target of the
	 * promotion.
	 * 
	 * @param value Object to promote.
	 * @return Promoted object.
	 */
	public IArchChangeSetsRationale promoteToArchChangeSetsRationale(
	edu.uci.isr.xarch.changesets.IArchChangeSets value);

	/**
	 * Create a top-level element of type <code>IArchRationale</code>.
	 * This function should be used in lieu of <code>createArchRationale</code>
	 * if the element is to be added as a sub-object of <code>IXArch</code>.
	 * @return new IArchRationale suitable for adding
	 * as a child of xArch.
	 */
	public IArchRationale createArchRationaleElement();

	/**
	 * Gets the IArchRationale child from the given <code>IXArch</code>
	 * element.  If there are multiple matching children, this returns the first one.
	 * @param xArch <code>IXArch</code> object from which to get the child.
	 * @return <code>IArchRationale</code> that is the child
	 * of <code>xArch</code> or <code>null</code> if no such object exists.
	 */
	public IArchRationale getArchRationale(IXArch xArch);

	/**
	 * Gets all the IArchRationale children from the given 
	 * <code>IXArch</code> element.
	 * @param xArch <code>IXArch</code> object from which to get the children.
	 * @return Collection of <code>IArchRationale</code> that are 	
	 * the children of <code>xArch</code> or an empty collection if no such object exists.
	 */
	public Collection getAllArchRationales(IXArch xArch);


	public final static XArchTypeMetadata TYPE_METADATA = new XArchTypeMetadata(
		XArchTypeMetadata.XARCH_CONTEXT,
		"rationale", null, null,
		new XArchPropertyMetadata[]{},
		new XArchActionMetadata[]{
			new XArchActionMetadata(XArchActionMetadata.CREATE, null, IRationale.TYPE_METADATA),
			new XArchActionMetadata(XArchActionMetadata.RECONTEXTUALIZE, IRationale.TYPE_METADATA, IRationale.TYPE_METADATA),
			new XArchActionMetadata(XArchActionMetadata.CREATE, null, edu.uci.isr.xarch.instance.IDescription.TYPE_METADATA),
			new XArchActionMetadata(XArchActionMetadata.RECONTEXTUALIZE, edu.uci.isr.xarch.instance.IDescription.TYPE_METADATA, edu.uci.isr.xarch.instance.IDescription.TYPE_METADATA),
			new XArchActionMetadata(XArchActionMetadata.CREATE, null, edu.uci.isr.xarch.instance.IXMLLink.TYPE_METADATA),
			new XArchActionMetadata(XArchActionMetadata.RECONTEXTUALIZE, edu.uci.isr.xarch.instance.IXMLLink.TYPE_METADATA, edu.uci.isr.xarch.instance.IXMLLink.TYPE_METADATA),
			new XArchActionMetadata(XArchActionMetadata.CREATE, null, IArchRationale.TYPE_METADATA),
			new XArchActionMetadata(XArchActionMetadata.RECONTEXTUALIZE, IArchRationale.TYPE_METADATA, IArchRationale.TYPE_METADATA),
			new XArchActionMetadata(XArchActionMetadata.CREATE, null, IArchChangeSetsRationale.TYPE_METADATA),
			new XArchActionMetadata(XArchActionMetadata.RECONTEXTUALIZE, IArchChangeSetsRationale.TYPE_METADATA, IArchChangeSetsRationale.TYPE_METADATA),
			new XArchActionMetadata(XArchActionMetadata.PROMOTE, edu.uci.isr.xarch.changesets.IArchChangeSets.TYPE_METADATA, IArchChangeSetsRationale.TYPE_METADATA),
			new XArchActionMetadata(XArchActionMetadata.CREATE_ELEMENT, null, IArchRationale.TYPE_METADATA)});

}

