package edu.uci.isr.bna4.facets;

public interface IHasMutableSelected
	extends IHasSelected{

	public static final String USER_MAY_SELECT = "userMaySelect";

	public void setSelected(boolean selected);
}
