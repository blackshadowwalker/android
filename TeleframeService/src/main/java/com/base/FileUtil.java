package com.base;

import java.io.File;

public class FileUtil extends File {

	public FileUtil(File parent, String child) {
		super(parent, child);
	}
	
	public static boolean mkdirs(String dirname){
		File f = new File(dirname);
		return f.mkdirs();
	}
}
