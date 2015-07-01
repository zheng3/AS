package edu.uci.isr.bna4.logics.coordinating;

import org.eclipse.swt.graphics.Rectangle;

import edu.uci.isr.bna4.IBNAModel;
import edu.uci.isr.bna4.IThing;
import edu.uci.isr.bna4.ThingEvent;
import edu.uci.isr.bna4.facets.IHasBoundingBox;
import edu.uci.isr.bna4.facets.IHasMutableBoundingBox;

public class MaintainGridLayoutLogic
	extends
	AbstractMaintainThingsLogic<IHasBoundingBox, IHasMutableBoundingBox>{

	public static class SizeHint{

		public int minSize = 0;
		public int minRatio = 0;

		public SizeHint(int minSize, int minRatio){
			this.minSize = minSize;
			this.minRatio = minRatio;
		}

		@Override
		public String toString(){
			return "minSize=" + minSize + " minRatio=" + minRatio;
		}
	}

	public static class GridData{

		public SizeHint widthHint;
		public SizeHint heightHint;

		public GridData(SizeHint widthHint, SizeHint heightHint){
			this.widthHint = widthHint;
			this.heightHint = heightHint;
		}

		public GridData(int wMinSize, int wMinRatio, int hMinSize, int hMinRatio){
			this(new SizeHint(wMinSize, wMinRatio), new SizeHint(hMinSize, hMinRatio));
		}

		@Override
		public String toString(){
			return "widthHint=(" + widthHint + ") heightHint=(" + heightHint + ")";
		}
	}

	public static class GridMasterData{

		public int numColumns = 1;

		public GridMasterData(int numColumns){
			this.numColumns = numColumns;
		}
	}

	public static final String GRID_MASTER_DATA_PROPERTY_NAME = "gridMasterData";

	public static final String GRID_DATA_PROPERTY_NAME = "gridData";

	private static final int[] calculateSizes(int totalSize, SizeHint[] hints){

		if(totalSize == 0){
			// return 0's
			return new int[hints.length];
		}

		int[] sizes = new int[hints.length];
		int totalHintsSize = 0;
		int totalHintsRatio = 0;
		int totalHintsThatHaveRatios = 0;

		for(SizeHint element: hints){
			if(element != null){
				totalHintsSize += element.minSize;
				if(element.minSize == 0){
					totalHintsRatio += element.minRatio;
					totalHintsThatHaveRatios++;
				}
			}
		}

		if(totalHintsSize > totalSize){
			// if the default sizes don't fit, then distribute using defaults
			for(int i = 0; i < hints.length; i++){
				if(hints[i] != null){
					sizes[i] = totalSize * hints[i].minSize / totalHintsSize;
				}
			}
		}
		else if(totalHintsRatio > 0){
			// assign the defaults, and distribute the remaining using fractions
			for(int i = 0; i < hints.length; i++){
				if(hints[i] != null){
					if(hints[i].minSize > 0){
						sizes[i] = hints[i].minSize;
					}
					else{
						sizes[i] = (totalSize - totalHintsSize) * hints[i].minRatio / totalHintsRatio;
					}
				}
			}
		}
		else{
			// assign the defaults, and distribute the remaining evenly
			for(int i = 0; i < hints.length; i++){
				if(hints[i] != null){
					if(hints[i].minSize > 0){
						sizes[i] = hints[i].minSize;
					}
					else{
						sizes[i] = (totalSize - totalHintsSize) * 1 / totalHintsThatHaveRatios;
					}
				}
			}
		}

		return sizes;
	}

	private static int[] calculateOffsets(int offset0, int[] sizes){
		int[] offsets = new int[sizes.length + 1];
		offsets[0] = offset0;
		for(int i = 0; i < sizes.length; i++){
			offsets[i + 1] = offsets[i] + sizes[i];
		}
		return offsets;
	}

	public MaintainGridLayoutLogic(){
		super(IHasBoundingBox.class, new String[]{IHasBoundingBox.BOUNDING_BOX_PROPERTY_NAME, GRID_MASTER_DATA_PROPERTY_NAME}, IHasMutableBoundingBox.class, new String[]{GRID_DATA_PROPERTY_NAME});
	}

	@Override
	protected void updateFromSource(IBNAModel sourceModel, IHasBoundingBox sourceThing, ThingEvent sourceThingEvent){
		IBNAModel targetModel = getBNAModel();
		Rectangle tableBB = sourceThing.getBoundingBox();
		GridMasterData gmd = sourceThing.getProperty(GRID_MASTER_DATA_PROPERTY_NAME);
		if(tableBB != null && gmd != null){
			IThing[] childThings = sourceModel.getChildThings(sourceThing);

			int cols = gmd.numColumns;
			int rows = (childThings.length + gmd.numColumns - 1) / gmd.numColumns;

			SizeHint[] colSizeHints = new SizeHint[cols];
			SizeHint[] rowSizeHints = new SizeHint[rows];

			// calculate cumulative column and row size hints
			for(int i = 0; i < childThings.length; i++){
				GridData gd = childThings[i].getProperty(GRID_DATA_PROPERTY_NAME);
				int row = i / cols;
				int col = i % cols;
				//System.err.println("row=" + row + " col=" + col + " hint=" + gd);
				if(gd != null){

					SizeHint colHint = colSizeHints[col];
					if(colHint == null){
						colSizeHints[col] = colHint = new SizeHint(0, 0);
					}
					SizeHint widthHint = gd.widthHint;
					if(widthHint != null){
						if(colHint.minSize < widthHint.minSize){
							colHint.minSize = widthHint.minSize;
						}
						if(colHint.minRatio < widthHint.minRatio){
							colHint.minRatio = widthHint.minRatio;
						}
					}

					SizeHint rowHint = rowSizeHints[row];
					if(rowHint == null){
						rowSizeHints[row] = rowHint = new SizeHint(0, 0);
					}
					SizeHint heightHint = gd.heightHint;
					if(heightHint != null){
						if(rowHint.minSize < heightHint.minSize){
							rowHint.minSize = heightHint.minSize;
						}
						if(rowHint.minRatio < heightHint.minRatio){
							rowHint.minRatio = heightHint.minRatio;
						}
					}
				}
			}

			// distribute table size to rows and columns
			int[] widths = calculateSizes(tableBB.width, colSizeHints);
			int[] heights = calculateSizes(tableBB.height, rowSizeHints);
			int[] xs = calculateOffsets(tableBB.x, widths);
			int[] ys = calculateOffsets(tableBB.y, heights);

			// assign cell sizes to things
			for(int i = 0; i < childThings.length; i++){
				int col = i % cols;
				int row = i / cols;

				childThings[i].setProperty(IHasBoundingBox.BOUNDING_BOX_PROPERTY_NAME, new Rectangle(xs[col], ys[row], widths[col], heights[row]));
			}
		}
	}

	@Override
	protected void updateToTarget(IBNAModel sourceModel, IHasMutableBoundingBox targetThing, ThingEvent targetThingEvent){
		IBNAModel targetModel = getBNAModel();
		if(sourceModel == null){
			sourceModel = targetModel;
		}
		IThing gridMasterThing = sourceModel.getParentThing(targetThing);
		if(gridMasterThing instanceof IHasBoundingBox){
			updateFromSource(sourceModel, (IHasBoundingBox)gridMasterThing, null);
		}
	}
}
