package edu.uci.isr.myx.fw;

//Describes, effectively, a brick type that can be loaded and instantiated.
public interface IMyxBrickDescription extends java.io.Serializable{
	public java.util.Properties getInitParams();
}
