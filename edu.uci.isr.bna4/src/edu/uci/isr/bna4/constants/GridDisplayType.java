package edu.uci.isr.bna4.constants;

public enum GridDisplayType{
	NONE,
	DOTS_AT_CORNERS,
	CROSSES_AT_CORNERS,
	DOTTED_LINES,
	SOLID_LINES;
	
	public String toString(){
		switch(this){
			case NONE:
				return "None";
			case DOTS_AT_CORNERS:
				return "Dots at Corners";
			case CROSSES_AT_CORNERS:
				return "Crosses at Corners";
			case DOTTED_LINES:
				return "Dotted Lines";
			case SOLID_LINES:
				return "Solid Lines";
		}
		return super.toString();
	}
}
