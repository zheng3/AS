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
package edu.uci.isr.xarch;

import java.util.*;

class XArchContextCache {

	static class ContextCacheKey{
		private IXArch xArch;
		private String contextKind;
		
		public ContextCacheKey(IXArch xArchRef, String contextKind){
			this.xArch = xArchRef;
			this.contextKind = contextKind;
		}
		
		public IXArch getXArch(){
			return xArch;
		}
		
		public String getContextKind(){
			return contextKind;
		}
		
		public int hashCode(){
			return xArch.hashCode() ^ contextKind.hashCode();
		}
		
		public boolean equals(Object o){
			if(!(o instanceof ContextCacheKey)){
				return false;
			}
			ContextCacheKey cck = (ContextCacheKey)o;
			return cck.xArch.equals(xArch) && cck.contextKind.equals(contextKind);
		}
	}

	private Map map = Collections.synchronizedMap(new HashMap()); 

	public XArchContextCache(){
		super();
	}
	
	public void put(IXArch xArch, String contextKind, IXArchContext contextRef){
		map.put(new ContextCacheKey(xArch, contextKind), contextRef);
	}
	
	public IXArchContext get(IXArch xArch, String contextKind){
		return (IXArchContext)map.get(new ContextCacheKey(xArch, contextKind));
	}
	
	public void removeAll(IXArch xArch){
		ArrayList doomedEntries = new ArrayList();
		for(Iterator it = map.keySet().iterator(); it.hasNext(); ){
			ContextCacheKey cck = (ContextCacheKey)it.next();
			if(cck.getXArch().equals(xArch)){
				doomedEntries.add(cck);
			}
		}
		for(Iterator it = doomedEntries.iterator(); it.hasNext(); ){
			ContextCacheKey cck = (ContextCacheKey)it.next();
			map.remove(cck);
		}
	}
}
