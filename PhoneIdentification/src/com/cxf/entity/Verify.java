package com.cxf.entity;

import java.io.Serializable;
import java.util.List;

public class Verify implements Serializable {
	private static final long serialVersionUID = 1L;
	
	public String uniqueToken;
	public String phoneNum;
	public String password;
	public List< VerifyItem> questionsAndAnswers;
	public int returnCode;
}
