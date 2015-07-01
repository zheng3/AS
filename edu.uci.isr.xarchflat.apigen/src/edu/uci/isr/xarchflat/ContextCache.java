package edu.uci.isr.xarchflat;

import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

class ContextCache{

	static class ContextCacheKey{

		private ObjRef xArchRef;
		private String contextKind;

		public ContextCacheKey(ObjRef xArchRef, String contextKind){
			this.xArchRef = xArchRef;
			this.contextKind = contextKind;
		}

		public ObjRef getXArchRef(){
			return xArchRef;
		}

		public String getContextKind(){
			return contextKind;
		}

		@Override
		public int hashCode(){
			return xArchRef.hashCode() ^ contextKind.hashCode();
		}

		@Override
		public boolean equals(Object o){
			if(!(o instanceof ContextCacheKey)){
				return false;
			}
			ContextCacheKey cck = (ContextCacheKey)o;
			return cck.xArchRef.equals(xArchRef) && cck.contextKind.equals(contextKind);
		}
	}

	private Map<ContextCacheKey, ObjRef> map = Collections.synchronizedMap(new HashMap<ContextCacheKey, ObjRef>());

	public ContextCache(){
		super();
	}

	public void put(ObjRef xArchRef, String contextKind, ObjRef contextRef){
		map.put(new ContextCacheKey(xArchRef, contextKind), contextRef);
	}

	public ObjRef get(ObjRef xArchRef, String contextKind){
		return map.get(new ContextCacheKey(xArchRef, contextKind));
	}

	public void removeAll(ObjRef xArchRef){
		synchronized(map){
			for(Iterator<Map.Entry<ContextCacheKey, ObjRef>> i = map.entrySet().iterator(); i.hasNext();){
				if(i.next().getKey().getXArchRef().equals(xArchRef)){
					i.remove();
				}
			}
		}
	}
}
