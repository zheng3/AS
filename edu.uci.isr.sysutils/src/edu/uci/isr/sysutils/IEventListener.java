package edu.uci.isr.sysutils;

public interface IEventListener<T>{

	public void handleEvent(T evt);
}