package com.services.base;

public class baseForm {
	
	private static JdbcConfig jdbcConfig = null;
	public static String ItsImageRoot = "";
	private static String smallImageEnd = ""; 
		

	public static String getSmallImageEnd() {
		return smallImageEnd;
	}

	public static void setSmallImageEnd(String smallImageEnd) {
		baseForm.smallImageEnd = smallImageEnd;
	}

	public static String getItsImageRoot() {
		return ItsImageRoot;
	}

	public static void setItsImageRoot(String itsImageRoot) {
		ItsImageRoot = itsImageRoot;
	}

	public baseForm()
	{
		jdbcConfig=new JdbcConfig();
	}
	
	public static void setJdbcConfig(JdbcConfig config) {
		if(jdbcConfig==null)
			jdbcConfig=new JdbcConfig();
		baseForm.jdbcConfig = config;
	}

	public static JdbcConfig getJdbcConfig() {
		return jdbcConfig;
	} 

}
