package edu.uci.isr.bna4.logics.editing;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IWorkbenchActionConstants;

import edu.uci.isr.bna4.AbstractThingLogic;
import edu.uci.isr.bna4.BNAUtils;
import edu.uci.isr.bna4.IBNAMenuListener;
import edu.uci.isr.bna4.IBNAView;
import edu.uci.isr.bna4.IThing;
import edu.uci.isr.bna4.UserEditableUtils;
import edu.uci.isr.bna4.assemblies.AssemblyUtils;
import edu.uci.isr.bna4.assemblies.IAssembly;
import edu.uci.isr.bna4.facets.IHasMutableColor;
import edu.uci.isr.widgets.swt.ColorSelectorDialog;
import edu.uci.isr.widgets.swt.SWTWidgetUtils;

public abstract class EditColorLogic
	extends AbstractThingLogic
	implements IBNAMenuListener{

	protected static RGB copiedRGB = null;

	protected Color[] currentColors = null;
	protected ImageDescriptor currentSwatchDescriptor = null;
	protected Color copyColor = null;
	protected ImageDescriptor copySwatchDescriptor = null;
	protected Color defaultColor = null;
	protected ImageDescriptor defaultSwatchDescriptor = null;

	public EditColorLogic(){
	}

	@Override
	public void destroy(){
		super.destroy();
		disposeResources();
	}

	/**
	 * Override to indicate a default color for the given things.
	 * 
	 * @param view
	 * @param thingsToEdit
	 * @return
	 */
	protected RGB getDefaultRGB(IBNAView view, IThing[] thingsToEdit){
		return null;
	}

	protected void disposeResources(){
		if(currentColors != null){
			for(int i = 0; i < currentColors.length; i++){
				if(currentColors[i] != null && !currentColors[i].isDisposed()){
					currentColors[i].dispose();
				}
			}
			currentColors = null;
		}
		if(copyColor != null && !copyColor.isDisposed()){
			copyColor.dispose();
		}
		if(defaultColor != null && !defaultColor.isDisposed()){
			defaultColor.dispose();
		}
	}

	public void fillMenu(IBNAView view, IMenuManager m, int localX, int localY, IThing t, int worldX, int worldY){
		if(t == null){
			return;
		}
		List<IHasMutableColor> thingsToEdit = new ArrayList<IHasMutableColor>();
		for(IThing selectedThing: BNAUtils.getSelectedThings(view.getWorld().getBNAModel())){
			IAssembly assembly = AssemblyUtils.getAssemblyWithPart(selectedThing);
			if(assembly != null){
				for(IThing partThing: assembly.getParts()){
					if(partThing instanceof IHasMutableColor && UserEditableUtils.isEditableForAnyQualities(partThing, IHasMutableColor.USER_MAY_EDIT_COLOR)){
						thingsToEdit.add((IHasMutableColor)partThing);
						break;
					}
				}
			}
		}
		if(thingsToEdit.size() == 0){
			return;
		}

		Display d = BNAUtils.getParentComposite(view).getDisplay();
		disposeResources();

		currentColors = new Color[thingsToEdit.size()];
		for(int i = 0; i < thingsToEdit.size(); i++){
			RGB rgb = thingsToEdit.get(i).getColor();
			if(rgb != null){
				currentColors[i] = new Color(d, rgb);
			}
		}
		currentSwatchDescriptor = SWTWidgetUtils.createColorSwatch(d, currentColors, 16, 16, false, true);

		addActions(view, m, thingsToEdit.toArray(new IHasMutableColor[thingsToEdit.size()]), worldX, worldY);
		m.add(new Separator(IWorkbenchActionConstants.MB_ADDITIONS));
	}

	protected void addActions(final IBNAView view, IMenuManager m, final IHasMutableColor[] thingsToEdit, int worldX, int worldY){
		final Display d = BNAUtils.getParentComposite(view).getDisplay();

		final RGB initialRGB = currentColors.length == 1 ? currentColors[0].getRGB() : null;
		Action assignColorAction = new Action("Assign Color..."){

			@Override
			public void run(){
				chooseAndAssignColor(view, thingsToEdit, initialRGB);
			}
		};
		assignColorAction.setImageDescriptor(currentSwatchDescriptor);
		m.add(assignColorAction);

		final RGB defaultRGB = getDefaultRGB(view, thingsToEdit);
		if(defaultRGB != null){
			Action resetToDefaultColorAction = new Action("Reset to Default Color"){

				@Override
				public void run(){
					assignColor(view, thingsToEdit, defaultRGB);
				}
			};

			defaultColor = new Color(d, defaultRGB);
			defaultSwatchDescriptor = SWTWidgetUtils.createColorSwatch(d, defaultColor, 16, 16, false, true);
			resetToDefaultColorAction.setImageDescriptor(defaultSwatchDescriptor);
			m.add(resetToDefaultColorAction);
		}

		if(currentColors.length == 1){
			Action copyColorAction = new Action("Copy Color"){

				@Override
				public void run(){
					copiedRGB = currentColors[0].getRGB();
				}
			};
			copyColorAction.setImageDescriptor(currentSwatchDescriptor);
			m.add(copyColorAction);
		}
		else{
			m.add(SWTWidgetUtils.createNoAction("Copy Color"));
		}

		if(copiedRGB != null){
			Action pasteColorAction = new Action("Paste Color"){

				@Override
				public void run(){
					assignColor(view, thingsToEdit, copiedRGB);
				}
			};

			copyColor = new Color(d, copiedRGB);
			copySwatchDescriptor = SWTWidgetUtils.createColorSwatch(d, copyColor, 16, 16, false, true);
			pasteColorAction.setImageDescriptor(copySwatchDescriptor);
			m.add(pasteColorAction);
		}
		else{
			m.add(SWTWidgetUtils.createNoAction("Paste Color"));
		}
	}

	protected void chooseAndAssignColor(IBNAView view, IHasMutableColor[] thingsToEdit, RGB initialRGB){
		ColorSelectorDialog csd = new ColorSelectorDialog(BNAUtils.getParentComposite(view).getShell());
		RGB rgb = csd.open(initialRGB);
		if(rgb != null){
			assignColor(view, thingsToEdit, rgb);
		}
	}

	protected void assignColor(IBNAView view, IHasMutableColor[] thingsToEdit, RGB rgb){
		for(IHasMutableColor thingToEdit: thingsToEdit){
			thingToEdit.setColor(rgb);
		}
	}
}
