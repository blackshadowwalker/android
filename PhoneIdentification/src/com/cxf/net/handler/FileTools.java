package com.cxf.net.handler;

import java.io.File;

public class FileTools {

	public static boolean deleteFoder(File file) {
		if (file.exists()) { // 判断文件是否存在
			if (file.isFile()) { // 判断是否是文件
				file.delete(); // delete()方法 你应该知道 是删除的意思;
			} else if (file.isDirectory()) { // 否则如果它是一个目录
				File files[] = file.listFiles(); // 声明目录下所有的文件 files[];
				if (files != null) {
					for (int i = 0; i < files.length; i++) { // 遍历目录下所有的文件
						deleteFoder(files[i]); // 把每个文件 用这个方法进行迭代
					}
				}
			}
			boolean isSuccess = file.delete();
			if (!isSuccess) {
				return false;
			}
		}
		return true;
	}

	public static boolean checkResolution(int w, int h) {
		final int height = 320;
		final int width = 240;
		if ((w * h) < (width * height)) {
			return false;
		}
		return true;
	}
}
