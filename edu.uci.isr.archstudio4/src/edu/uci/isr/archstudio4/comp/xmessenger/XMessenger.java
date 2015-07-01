package edu.uci.isr.archstudio4.comp.xmessenger;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.Path;

public class XMessenger {
	
	public XMessenger(){
		
	}
	
	public void notify(IFile targetF, String[] msgs) throws CoreException{
		//IFile targetF = ResourcesPlugin.getWorkspace().getRoot().getFile(new Path(target));
		for (int i=0;i<msgs.length;i++){
			IMarker marker = targetF.createMarker("edu.uci.isr.archstudio4.xmessenger");
			Map map = new HashMap(2);
			map.put(IMarker.MESSAGE, msgs[i]);
			map.put(IMarker.SEVERITY, new Integer(IMarker.SEVERITY_INFO));
			marker.setAttributes(map);				
		}
	}

}
