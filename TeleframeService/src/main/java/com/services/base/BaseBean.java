package com.services.base;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class BaseBean {
	
	private String version;
	private String author;
	private String authorID;
	private String data;
	

	public String getClassInfo(){
		JSONObject jsonmsg = new JSONObject();
		jsonmsg.put("class", this.getClass());
		jsonmsg.put("version", this.getVersion());
		jsonmsg.put("author", this.getAuthor());
		jsonmsg.put("authorID", this.getAuthorID());
		jsonmsg.put("data", this.getData());
		return jsonmsg.toString();
	}
	
	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

	public String getAuthorID() {
		return authorID;
	}

	public void setAuthorID(String authorID) {
		this.authorID = authorID;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}


}
