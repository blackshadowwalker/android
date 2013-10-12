package com.services.base;

public class BaseException {
	
	private static String LastErrorMsg="";
	private static long LastErrorCode=0;
	
	public String getLastErrorMsg()
	{
		return LastErrorMsg;
	}
	public void setLastErrorMsg(String msg)
	{
		this.LastErrorMsg = msg;
	}

}
