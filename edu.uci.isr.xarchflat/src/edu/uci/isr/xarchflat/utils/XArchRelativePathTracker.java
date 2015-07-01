package edu.uci.isr.xarchflat.utils;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import edu.uci.isr.sysutils.ListenerList;
import edu.uci.isr.xarchflat.IXArchPropertyMetadata;
import edu.uci.isr.xarchflat.IXArchTypeMetadata;
import edu.uci.isr.xarchflat.NoSuchObjectException;
import edu.uci.isr.xarchflat.ObjRef;
import edu.uci.isr.xarchflat.XArchFlatEvent;
import edu.uci.isr.xarchflat.XArchFlatInterface;
import edu.uci.isr.xarchflat.XArchFlatListener;
import edu.uci.isr.xarchflat.XArchPath;

public final class XArchRelativePathTracker
	implements XArchFlatListener{

	private static final boolean equalz(Object o1, Object o2){
		return o1 == null ? o2 == null : o1.equals(o2);
	}

	public static interface IFilter{

		public boolean exclude(ObjRef objRef, ObjRef[] relativeAncestors, XArchFlatEvent evt, XArchPath relativeSourceTargetPath);
	}

	public static class RequiredAttributeFilter
		implements IFilter{

		protected final XArchFlatInterface xarch;
		protected final String attributeName;
		protected final String requiredValue;

		public RequiredAttributeFilter(XArchFlatInterface xarch, String attributeName, String requiredValue){
			this.xarch = xarch;
			this.attributeName = attributeName;
			this.requiredValue = requiredValue;
		}

		public boolean exclude(ObjRef objRef, ObjRef[] relativeAncestors, XArchFlatEvent evt, XArchPath relativeSourceTargetPath){
			if(evt != null){
				if(relativeSourceTargetPath.getLength() == 0){
					// we are dealing with the objRef
					if(objRef.equals(evt.getSource())){
						if(attributeName.equals(evt.getTargetName())){
							return !requiredValue.equals(evt.getTarget());
						}
						// we are dealing with some other attribute
						return false;
					}
				}
				else{
					// we are dealing with a child
					return false;
				}
			}
			return !xarch.has(objRef, attributeName, requiredValue);
		}
	}

	private static final int indexOf(ObjRef objRef, ObjRef[] objRefs){
		for(int i = 0; i < objRefs.length; i++){
			if(objRef.equals(objRefs[i])){
				return i;
			}
		}
		return -1;
	}

	private final Map<ObjRef, ObjRef[]> addedObjRefToAncestorRefs = Collections.synchronizedMap(new HashMap<ObjRef, ObjRef[]>());

	private boolean scanning = true;

	private ObjRef rootObjRef = null;
	private boolean firstNameIsElement = false;

	private String relativePath = null;
	private String[] relativePathNames = null;
	private int relativePathNamesLength = 0;
	protected final XArchFlatInterface xarch;

	public XArchRelativePathTracker(XArchFlatInterface xarch){
		this(xarch, null, null, false);
	}

	public XArchRelativePathTracker(XArchFlatInterface xarch, ObjRef rootObjRef, String relativePath, boolean startScanning){
		this.xarch = xarch;
		this.scanning = startScanning;
		setTrackInfo(rootObjRef, relativePath);
	}

	public ObjRef getRootObjRef(){
		return rootObjRef;
	}

	public String getRelativePath(){
		return relativePath;
	}

	public void setTrackInfo(ObjRef rootObjRef, String relativePath){
		for(String relativePathName: relativePath != null ? relativePath.split("\\/") : new String[0]){
			if(Character.isUpperCase(relativePathName.charAt(0))){
				throw new IllegalArgumentException("Each path segment must start with a lower case letter: " + relativePath);
			}
		}

		if(!equalz(this.rootObjRef, rootObjRef) || !equalz(this.relativePath, relativePath)){
			boolean wasScanning = scanning;
			stopScanning();

			this.rootObjRef = rootObjRef;
			this.firstNameIsElement = rootObjRef != null ? rootObjRef.equals(xarch.getXArch(rootObjRef)) : false;
			this.relativePath = relativePath;
			this.relativePathNames = relativePath != null ? relativePath.split("\\/") : null;
			this.relativePathNamesLength = relativePath != null ? relativePathNames.length : 0;

			if(wasScanning){
				startScanning();
			}
		}
	}

	public ObjRef[] getAddedObjRefs(){
		return addedObjRefToAncestorRefs.keySet().toArray(new ObjRef[addedObjRefToAncestorRefs.size()]);
	}

	public boolean isAddedObjRef(ObjRef objRef){
		return addedObjRefToAncestorRefs.containsKey(objRef);
	}

	public void startScanning(){
		if(!scanning && rootObjRef != null && relativePath != null){
			scanning = true;
			ObjRef[] relativeAncestors = new ObjRef[relativePathNamesLength + 1];
			relativeAncestors[relativeAncestors.length - 1] = rootObjRef;
			scanObjRef(rootObjRef, relativeAncestors, 0);
		}
	}

	public boolean isScanning(){
		return scanning;
	}

	public void stopScanning(){
		if(scanning){
			scanning = false;
			for(Map.Entry<ObjRef, ObjRef[]> entry: addedObjRefToAncestorRefs.entrySet()){
				processRemove(entry.getKey(), entry.getValue());
			}
			addedObjRefToAncestorRefs.clear();
		}
	}

	public void rescan(){
		rescan(null);
	}

	public void rescan(ObjRef startingRef){
		if(scanning){
			if(startingRef == null){
				ObjRef[] relativeAncestors = new ObjRef[relativePathNamesLength + 1];
				relativeAncestors[relativeAncestors.length - 1] = rootObjRef;
				scanObjRef(rootObjRef, relativeAncestors, 0);
			}
			else{
				ObjRef[] startingAncestors = xarch.getAllAncestors(startingRef);
				int index = indexOf(rootObjRef, startingAncestors);
				if(index >= 0){
					int count = index + 1;
					ObjRef[] relativeAncestors = new ObjRef[relativePathNamesLength + 1];
					System.arraycopy(startingAncestors, 0, relativeAncestors, relativeAncestors.length - count, count);
					scanObjRef(startingRef, relativeAncestors, count - 1);
				}
			}
		}
	}

	protected int getRelativePathMatchLength(String[] pathNames, int pathNamesStart){
		int iR = 0;
		int iP = pathNamesStart;
		int cP = pathNames.length - iP;
		int cM = cP < relativePathNamesLength ? cP : relativePathNamesLength;
		int c = 0;
		while(c < cM && relativePathNames[iR++].equals(pathNames[iP++])){
			c++;
		}
		return c;
	}

	protected void scanObjRef(ObjRef objRef, ObjRef[] relativeAncestors, int childNameIndex){
		if(childNameIndex < relativePathNamesLength){
			String name = childNameIndex == 0 && firstNameIsElement ? "object" : relativePathNames[childNameIndex];
			IXArchTypeMetadata type = xarch.getTypeMetadata(objRef);
			IXArchPropertyMetadata property = type.getProperty(name);

			if(property != null){
				switch(property.getMetadataType()){
				case IXArchPropertyMetadata.ATTRIBUTE:
					break;

				case IXArchPropertyMetadata.ELEMENT: {
					ObjRef childRef = (ObjRef)xarch.get(objRef, name);
					if(childRef != null){
						relativeAncestors[relativeAncestors.length - childNameIndex - 2] = childRef;
						scanObjRef(childRef, relativeAncestors, childNameIndex + 1);
					}
				}
					break;

				case IXArchPropertyMetadata.ELEMENT_MANY: {
					for(ObjRef childRef: xarch.getAll(objRef, name)){
						if(firstNameIsElement && childNameIndex == 0){
							if(!relativePathNames[childNameIndex].equals(xarch.getElementName(childRef))){
								continue;
							}
						}
						relativeAncestors[relativeAncestors.length - childNameIndex - 2] = childRef;
						scanObjRef(childRef, relativeAncestors, childNameIndex + 1);
					}
				}
					break;
				}
			}
		}
		else if(childNameIndex == relativePathNamesLength){
			ObjRef[] relativeAncestorsClone = new ObjRef[relativeAncestors.length];
			System.arraycopy(relativeAncestors, 0, relativeAncestorsClone, 0, relativeAncestors.length);
			changedObjRef(objRef, relativeAncestorsClone, null, null);
		}
	}

	public void handleXArchFlatEvent(XArchFlatEvent evt){
		try{
			if(scanning && !evt.getIsExtra()){
				ObjRef[] sourceAncestors = evt.getSourceAncestors();
				int rootObjRefIndex = indexOf(rootObjRef, sourceAncestors);
				if(rootObjRefIndex >= 0){
					Object target = evt.getTarget();
					int sourceAncestorsLength = sourceAncestors.length;
					int relativePathStartIndex = sourceAncestorsLength - rootObjRefIndex;
					XArchPath stPath = evt.getSourceTargetPath();
					String[] stPathTagNames = stPath.getTagNames();
					int relativePathMatchLength = getRelativePathMatchLength(stPathTagNames, relativePathStartIndex);

					switch(evt.getEventType()){

					case XArchFlatEvent.SET_EVENT: {
						if(!(target instanceof ObjRef)){
							if(relativePathMatchLength == relativePathNamesLength){
								int objRefIndex = relativePathStartIndex + relativePathNamesLength;
								if(sourceAncestorsLength >= objRefIndex){
									int ancestorIndex = sourceAncestorsLength - objRefIndex;
									ObjRef[] relativeAncestors = new ObjRef[relativePathNamesLength + 1];
									System.arraycopy(sourceAncestors, ancestorIndex, relativeAncestors, 0, relativeAncestors.length);
									changedObjRef(sourceAncestors[ancestorIndex], relativeAncestors, evt, stPath.subpath(objRefIndex));
								}
							}
							break;
						}
					}
						// fall through

					case XArchFlatEvent.ADD_EVENT: {
						if(relativePathMatchLength == relativePathNamesLength){
							int objRefIndex = relativePathStartIndex + relativePathNamesLength;
							ObjRef[] relativeAncestors = new ObjRef[relativePathNamesLength + 1];
							if(sourceAncestorsLength >= objRefIndex){
								int ancestorIndex = sourceAncestorsLength - objRefIndex;
								System.arraycopy(sourceAncestors, ancestorIndex, relativeAncestors, 0, relativeAncestors.length);
								changedObjRef(sourceAncestors[ancestorIndex], relativeAncestors, evt, stPath.subpath(objRefIndex));
							}
							else{
								System.arraycopy(sourceAncestors, 0, relativeAncestors, 1, relativeAncestors.length - 1);
								relativeAncestors[0] = (ObjRef)target;
								changedObjRef((ObjRef)target, relativeAncestors, evt, evt.getSourceTargetPath().subpath(objRefIndex));
							}
						}
						else if(relativePathMatchLength > 0){
							ObjRef[] relativeAncestors = new ObjRef[relativePathNamesLength + 1];
							System.arraycopy(sourceAncestors, 0, relativeAncestors, relativeAncestors.length - relativePathMatchLength, relativeAncestors.length - relativePathMatchLength - 1);
							relativeAncestors[relativeAncestors.length - relativePathMatchLength - 1] = (ObjRef)target;
							scanObjRef((ObjRef)target, relativeAncestors, relativePathMatchLength);
						}
					}
						break;

					case XArchFlatEvent.CLEAR_EVENT: {
						if(!(target instanceof ObjRef)){
							if(relativePathMatchLength == relativePathNamesLength){
								int objRefIndex = relativePathStartIndex + relativePathNamesLength;
								if(sourceAncestorsLength >= objRefIndex){
									int ancestorIndex = sourceAncestorsLength - objRefIndex;
									ObjRef[] relativeAncestors = new ObjRef[relativePathNamesLength + 1];
									System.arraycopy(sourceAncestors, ancestorIndex, relativeAncestors, 0, relativeAncestors.length);
									changedObjRef(sourceAncestors[ancestorIndex], relativeAncestors, evt, stPath.subpath(objRefIndex));
								}
							}
							break;
						}
					}
						// fall through
					case XArchFlatEvent.REMOVE_EVENT: {
						if(relativePathMatchLength == relativePathNamesLength){
							int objRefIndex = relativePathStartIndex + relativePathNamesLength;
							if(sourceAncestorsLength >= objRefIndex){
								int ancestorIndex = sourceAncestorsLength - objRefIndex;
								ObjRef[] relativeAncestors = new ObjRef[relativePathNamesLength + 1];
								System.arraycopy(sourceAncestors, ancestorIndex, relativeAncestors, 0, relativeAncestors.length);
								changedObjRef(sourceAncestors[ancestorIndex], relativeAncestors, evt, stPath.subpath(objRefIndex));
							}
							else{
								removedObjRef((ObjRef)target);
							}
						}
						else if(relativePathMatchLength > 0){
							int ancestorIndex = relativePathNamesLength - relativePathMatchLength;
							Set<ObjRef> toRemoveRefs = new HashSet<ObjRef>();
							for(Entry<ObjRef, ObjRef[]> entry: addedObjRefToAncestorRefs.entrySet()){
								ObjRef[] ancestorRefs = entry.getValue();
								if(target.equals(ancestorRefs[ancestorIndex])){
									toRemoveRefs.add(entry.getKey());
								}
							}
							for(ObjRef toRemoveRef: toRemoveRefs){
								ObjRef[] ancestorRefs = addedObjRefToAncestorRefs.remove(toRemoveRef);
								if(ancestorRefs != null){
									processRemove(toRemoveRef, ancestorRefs);
								}
							}
						}
					}
						break;

					case XArchFlatEvent.PROMOTE_EVENT: {
						if(relativePathMatchLength == relativePathNamesLength){
							int objRefIndex = relativePathStartIndex + relativePathNamesLength;
							ObjRef[] relativeAncestors = new ObjRef[relativePathNamesLength + 1];
							if(sourceAncestorsLength >= objRefIndex){
								int ancestorIndex = sourceAncestorsLength - objRefIndex;
								System.arraycopy(sourceAncestors, ancestorIndex, relativeAncestors, 0, relativeAncestors.length);
								changedObjRef(sourceAncestors[ancestorIndex], relativeAncestors, evt, stPath.subpath(objRefIndex));
							}
						}
					}
						break;
					}
				}
			}
		}
		catch(NoSuchObjectException e){
			stopScanning();
		}
	}

	ListenerList<IFilter> filters = new ListenerList<IFilter>(IFilter.class);

	public void addFilter(IFilter f){
		filters.add(f);
	}

	public void removeFilter(IFilter f){
		filters.remove(f);
	}

	protected void changedObjRef(ObjRef objRef, ObjRef[] relativeAncestors, XArchFlatEvent evt, XArchPath relativeSourceTargetPath){
		assert relativeAncestors[0].equals(objRef);
		assert relativeAncestors[relativeAncestors.length - 1].equals(rootObjRef);

		for(IFilter f: filters.getListeners()){
			if(f.exclude(objRef, relativeAncestors, evt, relativeSourceTargetPath)){
				removedObjRef(objRef);
				return;
			}
		}

		if(addedObjRefToAncestorRefs.put(objRef, relativeAncestors) == null){
			processAdd(objRef, relativeAncestors);
		}
		else if(evt != null){
			processUpdate(objRef, relativeAncestors, evt, relativeSourceTargetPath);
		}
	}

	protected void removedObjRef(ObjRef objRef){
		ObjRef[] ancestorRefs = addedObjRefToAncestorRefs.remove(objRef);
		if(ancestorRefs != null){
			processRemove(objRef, ancestorRefs);
		}
	}

	@Override
	public String toString(){
		return "" + (rootObjRef != null ? xarch.getXArchPath(rootObjRef).toTagsOnlyString() : "") + " : " + relativePath + " " + scanning;
	}

	ListenerList<IXArchRelativePathTrackerListener> pathTrackerListeners = new ListenerList<IXArchRelativePathTrackerListener>(IXArchRelativePathTrackerListener.class);

	public void addTrackerListener(IXArchRelativePathTrackerListener l){
		pathTrackerListeners.add(l);
	}

	public void removeTrackerListener(IXArchRelativePathTrackerListener l){
		pathTrackerListeners.remove(l);
	}

	protected void processAdd(ObjRef objRef, ObjRef[] relativeAncestors){
		for(IXArchRelativePathTrackerListener l: pathTrackerListeners.getListeners()){
			l.processAdd(objRef, relativeAncestors);
		}
	}

	protected void processUpdate(ObjRef objRef, ObjRef[] relativeAncestors, XArchFlatEvent evt, XArchPath relativeSourceTargetPath){
		for(IXArchRelativePathTrackerListener l: pathTrackerListeners.getListeners()){
			l.processUpdate(objRef, relativeAncestors, evt, relativeSourceTargetPath);
		}
	}

	protected void processRemove(ObjRef objRef, ObjRef[] relativeAncestors){
		for(IXArchRelativePathTrackerListener l: pathTrackerListeners.getListeners()){
			l.processRemove(objRef, relativeAncestors);
		}
	}
}