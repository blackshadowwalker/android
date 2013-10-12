package com.services.base;

public class JdbcConfig {
	
	private String jdbcname = "";
	private String driverClassName = "";
	private String url = "";
	private String username = "";
	private String password = "";
	
	public static String JDBC_NAME="jdbc.name";
	public static String JDBC_DRIVERCLASSNAME="jdbc.driverClassName";
	public static String JDBC_URL="jdbc.url";
	public static String JDBC_USERNAME="jdbc.username";
	public static String JDBC_PASSWORD="jdbc.password";
	
	public String getJdbcname() {
		return jdbcname;
	}
	public void setJdbcname(String jdbcname) {
		this.jdbcname = jdbcname;
	}
	public String getDriverClassName() {
		return driverClassName;
	}
	public void setDriverClassName(String driverClassName) {
		this.driverClassName = driverClassName;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	

}
