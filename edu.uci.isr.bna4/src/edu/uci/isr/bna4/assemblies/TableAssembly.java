package edu.uci.isr.bna4.assemblies;

import org.eclipse.swt.graphics.RGB;

import edu.uci.isr.bna4.IBNAModel;
import edu.uci.isr.bna4.IThing;
import edu.uci.isr.bna4.facets.IHasBoundingBox;
import edu.uci.isr.bna4.logics.coordinating.MaintainGridLayoutLogic;
import edu.uci.isr.bna4.logics.coordinating.MirrorValueLogic;
import edu.uci.isr.bna4.logics.coordinating.MaintainGridLayoutLogic.GridData;
import edu.uci.isr.bna4.logics.coordinating.MaintainGridLayoutLogic.GridMasterData;
import edu.uci.isr.bna4.logics.coordinating.MaintainGridLayoutLogic.SizeHint;
import edu.uci.isr.bna4.things.borders.BoxBorderThing;
import edu.uci.isr.bna4.things.glass.BoxGlassThing;
import edu.uci.isr.bna4.things.shapes.BoxThing;

public class TableAssembly
	extends AbstractAssembly{

	private static final SizeHint[] createHints(int count){
		SizeHint[] hints = new SizeHint[count];
		for(int i = 0; i < hints.length; i++){
			hints[i] = new SizeHint(0, 1);
		}
		return hints;
	}

	public static final String GLASS = "glass";
	public static final String TABLE_BACKING = "tableBacking";
	public static final String BACKGROUND = "background";

	public TableAssembly(IBNAModel model, IThing parentThing, Object assemblyKind, SizeHint[] columnHints, SizeHint[] rowHints){
		super(model, parentThing, assemblyKind);

		// Create sub-things
		BoxThing backgroundThing = new BoxThing();
		backgroundThing.setColor(new RGB(192, 192, 255));
		model.addThing(backgroundThing, rootThing);

		BoxGlassThing tableBackingThing = new BoxGlassThing();
		model.addThing(tableBackingThing, rootThing);

		BoxGlassThing glassThing = new BoxGlassThing();
		model.addThing(glassThing, rootThing);

		for(SizeHint rowHint: rowHints){
			for(SizeHint columnHint: columnHints){
				BoxGlassThing cellBackingThing = new BoxGlassThing();
				cellBackingThing.setProperty(MaintainGridLayoutLogic.GRID_DATA_PROPERTY_NAME, new GridData(columnHint, rowHint));
				model.addThing(cellBackingThing, tableBackingThing);
			}
		}

		// Set up connections
		tableBackingThing.setProperty(MaintainGridLayoutLogic.GRID_MASTER_DATA_PROPERTY_NAME, new GridMasterData(columnHints.length));
		MirrorValueLogic.mirrorValue(glassThing, IHasBoundingBox.BOUNDING_BOX_PROPERTY_NAME, tableBackingThing, backgroundThing);

		// mark parts
		markPart(GLASS, glassThing);
		markPart(TABLE_BACKING, tableBackingThing);
		markPart(BACKGROUND, backgroundThing);
	}

	public TableAssembly(IBNAModel model, IThing parentThing, Object assemblyKind, int columns, int rows){
		this(model, parentThing, assemblyKind, createHints(columns), createHints(rows));
	}

	public BoxGlassThing getBoxGlassThing(){
		return getPart(GLASS);
	}

	public BoxGlassThing getTableBackingThing(){
		return getPart(TABLE_BACKING);
	}

	public BoxThing getBoxThing(){
		return getPart(BACKGROUND);
	}

	public BoxGlassThing getCellBacking(IBNAModel model, int column, int row){
		BoxGlassThing tableBackingThing = getTableBackingThing();
		GridMasterData gmd = tableBackingThing.getProperty(MaintainGridLayoutLogic.GRID_MASTER_DATA_PROPERTY_NAME);
		int childIndex = gmd.numColumns * row + column;
		IThing[] childThings = model.getChildThings(tableBackingThing);
		return (BoxGlassThing)(childIndex < childThings.length ? childThings[childIndex] : null);
	}

	public void addBorders(IBNAModel model){
		BoxGlassThing tableBackingThing = getTableBackingThing();
		for(IThing childThing: model.getChildThings(tableBackingThing)){
			BoxGlassThing childBackingThing = (BoxGlassThing)childThing;
			BoxBorderThing borderThing = new BoxBorderThing();
			model.addThing(borderThing, childBackingThing);
			MirrorValueLogic.mirrorValue(childBackingThing, IHasBoundingBox.BOUNDING_BOX_PROPERTY_NAME, borderThing);
		}
	}
}
