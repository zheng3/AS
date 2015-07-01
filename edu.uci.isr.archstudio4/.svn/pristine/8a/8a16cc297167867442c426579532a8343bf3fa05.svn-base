package edu.uci.isr.archstudio4.comp.archipelago.interactions.logics.editing;

import java.util.Collection;

import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;

import edu.uci.isr.archstudio4.comp.resources.IResources;
import edu.uci.isr.archstudio4.util.ArchstudioResources;
import edu.uci.isr.archstudio4.util.XadlTreeUtils;

public class MethodLabelProvider extends LabelProvider implements
		ILabelProvider {

	protected IResources resources;

	public MethodLabelProvider(IResources resources) {
		this.resources = resources;
		ArchstudioResources.init(resources);
	}

	@Override
	public String getText(Object element) {
		if (element instanceof MethodLabel) {
			MethodLabel methodkLabel = (MethodLabel) element;
			return methodkLabel.toLabelString();
		}
		return super.getText(element);
	}

	@Override
	public Image getImage(Object element) {
		if (element instanceof Collection) {
			return XadlTreeUtils.getIconForType(resources, XadlTreeUtils.DOCUMENT);
		}
		else if (element instanceof MethodLabel) {
			return XadlTreeUtils.getIconForType(resources, XadlTreeUtils.COMPONENT);
		}
		return super.getImage(element);
	}
}
