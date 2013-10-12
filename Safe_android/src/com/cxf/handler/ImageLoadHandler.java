package com.cxf.handler;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
/**
 * 图片操作类，关于一些图片的操作，比如从网络上获取图片什么的...
 * 
 * @author Administrator
 *
 */
public class ImageLoadHandler {
	/**
	 * 从网络上获取图片，然后变成Bitmap格式的文件
	 * @return
	 */
	public static Bitmap getImage(String url) throws Exception{
		Bitmap bitmap=null;
//		获得网络连接
		HttpURLConnection connection=(HttpURLConnection) new URL(url).openConnection();
		connection.setConnectTimeout(5000);
		connection.setRequestMethod("GET");
//		判断是否连接成功
		if(connection.getResponseCode()==200){
//			取得流
			InputStream input=connection.getInputStream();
			BitmapFactory.Options opts = new BitmapFactory.Options();
			opts.inSampleSize = 12;
			bitmap=BitmapFactory.decodeStream(input,null,opts
					);
		}
		return bitmap;
	}
}
