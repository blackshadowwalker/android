package com.services.base;

public class ErrorUtil {

	public static final long SSID_NULL = -1;//user ssid null
	public static final long OK = 0;// There's no error
	public static final long CONNECT_NULL = 9 ;// connect is null
	public static final long DATA_NULL = 1; // the data is null
	public static final long SQLEXCEPTION = 5; // sql exception
	public static final long DATASOURCE_NULL = 101; // sql exception
	public static final long CONNECTION_NULL = 102; // sql exception
	public static final long SUCCESS=200;
	public static final long FAILED=201;
	public static final long HASLOGINED=11;
	
	public static String parse(long error){
		String ret = "";
		switch((int)error){
		case (int) SSID_NULL:
			break;
		case (int) OK:
			break;
		default:
				break;
		}
		return ret;
	}
}
