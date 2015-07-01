package edu.uci.isr.xarchflat.utils;

import edu.uci.isr.xarchflat.ObjRef;
import edu.uci.isr.xarchflat.XArchFlatEvent;
import edu.uci.isr.xarchflat.XArchPath;

public interface IXArchRelativePathTrackerListener{

	public void processAdd(ObjRef objRef, ObjRef[] relativeAncestorRefs);

	public void processUpdate(ObjRef objRef, ObjRef[] relativeAncestorRefs, XArchFlatEvent evt, XArchPath relativeSourceTargetPath);

	public void processRemove(ObjRef objRef, ObjRef[] relativeAncestorRefs);
}
