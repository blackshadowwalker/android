package com.cxf.handler;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import android.content.Context;

public class ExceptionHandler implements Thread.UncaughtExceptionHandler{

	Context context;
	
	public ExceptionHandler(Context context) {
		super();
		this.context = context;
	}

	@Override
	public void uncaughtException(Thread arg0, Throwable ex) {
		String info = null;  
		ByteArrayOutputStream baos = null;  
		PrintStream printStream = null;  
		try {  
		    baos = new ByteArrayOutputStream();  
		    printStream = new PrintStream(baos);  
		    ex.printStackTrace(printStream);  
		    byte[] data = baos.toByteArray();  
		    info = new String(data);  
		    data = null;  
		} catch (Exception e) {  
		    e.printStackTrace();  
		} finally {  
		    try {  
		        if (printStream != null) {  
		            printStream.close();  
		        }  
		        if (baos != null) {  
		            baos.close();  
		        }  
		    } catch (Exception e) {  
		        e.printStackTrace();  
		    }  
		} 
	}
	
	

}
