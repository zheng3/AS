/*
 * Copyright (c) 2000-2005 Regents of the University of California.
 * All rights reserved.
 *
 * This software was developed at the University of California, Irvine.
 *
 * Redistribution and use in source and binary forms are permitted
 * provided that the above copyright notice and this paragraph are
 * duplicated in all such forms and that any documentation,
 * advertising materials, and other materials related to such
 * distribution and use acknowledge that the software was developed
 * by the University of California, Irvine.  The name of the
 * University may not be used to endorse or promote products derived
 * from this software without specific prior written permission.
 * THIS SOFTWARE IS PROVIDED ``AS IS'' AND WITHOUT ANY EXPRESS OR
 * IMPLIED WARRANTIES, INCLUDING, WITHOUT LIMITATION, THE IMPLIED
 * WARRANTIES OF MERCHANTIBILITY AND FITNESS FOR A PARTICULAR PURPOSE.
 */
package edu.uci.isr.xarch;

import java.io.*;

public class StringReplacerReader extends FilterReader{

	protected String targetString;
	protected String replaceString;
	
	int pointer = 0;
	
	protected StringBuffer inbuf = new StringBuffer();
	protected StringBuffer outbuf = new StringBuffer();
	
	/*
	public static void main(String[] args){
		String formLetter =
			"Hello $$(FIRST) $$(LAST).  My name is $$(MYNAME).  Mr. $$(LAST), you have won $$(FOO)";
		
		char[] buf = new char[500];
		
		try{
			StringReader r = new StringReader(formLetter);
			StringReplacerReader trr = new StringReplacerReader(r, "FIRST", "Eric");
			while(true){
				//int chInt = trr.read();
				int nr = trr.read(buf, 0, buf.length);
				for(int i = 0; i < nr; i++){ System.out.print(buf[i]); }
				if(nr < buf.length){
					break;
				}
			}
		}
		catch(IOException e){
			e.printStackTrace();
		}
	}
	*/

	public StringReplacerReader(Reader r, String targetString, String replaceString){
		super(r);
		this.targetString = targetString;
		this.replaceString = replaceString;
	}
	
	public int read() throws IOException{
		if(outbuf.length() != 0){
			char ch = outbuf.charAt(0);
			outbuf.deleteCharAt(0);
			return (int)ch;
		}
		
		int chInt = super.read();
		if(chInt == -1){
			if(inbuf.length() != 0){
				outbuf.append(inbuf.toString());
				inbuf.setLength(0);
				return read();
			}
			return -1;
		}
		
		char ch = (char)chInt;
		if(ch == targetString.charAt(pointer)){
			inbuf.append(ch);
			pointer++;
			if(pointer == targetString.length()){
				outbuf.append(replaceString);
				inbuf.setLength(0);
				pointer = 0;
			}
			return read();
		}
		else{
			if(inbuf.length() != 0){
				outbuf.append(inbuf.toString());
				outbuf.append(ch);
				inbuf.setLength(0);
				pointer = 0;
				return read();
			}
			return ch;
		}
	}

	public int read(char[] cbuf, int off, int len) throws IOException{
		int bytesActuallyRead = 0;
		for(int i = 0; i < len; i++){
			int chInt = read();
			if(chInt == -1){
				if(bytesActuallyRead > 0){
					return bytesActuallyRead;
				}
				else{
					return -1;
				}
			}
			else{
				cbuf[off+i] = (char)chInt;
				bytesActuallyRead++;
			}
		}
		return len;
	}
	
	public int read(char[] cbuf) throws IOException{
		return read(cbuf, 0, cbuf.length);
	}
	
	
}