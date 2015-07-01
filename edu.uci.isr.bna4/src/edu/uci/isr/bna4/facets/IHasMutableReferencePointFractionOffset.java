package edu.uci.isr.bna4.facets;

public interface IHasMutableReferencePointFractionOffset
	extends IRelativeMovable, IHasBoundingBox{

	public static final String REFERENCE_POINT_FRACTION_OFFSET_PROPERTY_NAME = "referencePointFractionOffset";

	public float[] getReferencePointFractionOffset();

	public void setReferencePointFractionOffset(float[] offset);
}
