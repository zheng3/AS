package edu.uci.isr.myx.conn;

public interface IMultiwayProgressListener{
	public void callProgress(int calleeNum, int totalCallees, Object returnValue, Throwable exception);
}
