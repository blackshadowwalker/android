package com.cxf.handler;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.Arrays;
import java.util.Comparator;
import java.util.concurrent.ExecutionException;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;

import com.cxf.safe_android.R;

/**
 * 图片操作类，关于一些图片的操作，比如从网络上获取图片什么的...
 * 
 * @author Administrator
 * 
 */
public class ImageLoadHandler {
	static String defaultImagePath = "/Safe_android/res/drawable-mdpi/item_logo.png";
	/**
	 * 从网络上获取图片，然后变成Bitmap格式的文件
	 * 
	 * @return
	 */
	Context context;
	final String TAG = ImageLoadHandler.class.getName();
	private static final int TIMEOUT = 6 * 1000;

	public ImageLoadHandler(Context context) {
		super();
		this.context = context;
	}

	public Bitmap getImage(String url) {
		Log.i(TAG, "开始获取图片");
		Bitmap bitmap = null;
		// 获得网络连接
		try {
			HttpURLConnection connection = (HttpURLConnection) new URL(url)
					.openConnection();
			connection.setConnectTimeout(TIMEOUT);
			connection.setDoInput(true);
			connection.setRequestMethod("GET");
			// 判断是否连接成功
			if (connection.getResponseCode() == 200) {
				// 取得流
				InputStream input = connection.getInputStream();
				int length = (int) connection.getContentLength();
				if (length != -1) {
					byte[] imgData = new byte[length];
					byte[] temp = new byte[512];
					int readLen = 0;
					int destPost = 0;
					while ((readLen = input.read(temp)) > 0) {
						System.arraycopy(temp, 0, imgData, destPost,
								readLen);
						destPost += readLen;
					}
					BitmapFactory.Options opts = new BitmapFactory.Options();
					opts.inSampleSize = 6;
					bitmap = BitmapFactory.decodeByteArray(imgData, 0,
							imgData.length, opts);
				}

			}
		} catch (MalformedURLException e) {
			Log.i(TAG, e.getStackTrace().toString());
			bitmap = BitmapFactory.decodeResource(context.getResources(),
					R.drawable.item_logo);
			return bitmap;
		} catch (ProtocolException e) {
			Log.i(TAG, e.getStackTrace().toString());
			bitmap = BitmapFactory.decodeResource(context.getResources(),
					R.drawable.item_logo);
			return bitmap;
		} catch (IOException e) {
			Log.i(TAG, e.getStackTrace().toString());
			bitmap = BitmapFactory.decodeResource(context.getResources(),
					R.drawable.item_logo);
			return bitmap;
		}
		return bitmap;
	}

	

	// private void saveBmpToSd(Bitmap bm, String url) {
	// if (bm == null) {
	// Log.w(TAG, " trying to savenull bitmap");
	// return;
	// }
	// //判断sdcard上的空间
	// if (FREE_SD_SPACE_NEEDED_TO_CACHE >freeSpaceOnSd()) {
	// Log.w(TAG, "Low free space onsd, do not cache");
	// return;
	// }
	// String filename =convertUrlToFileName(url);
	// String dir = getDirectory(filename);
	// File file = new File(dir +"/" + filename);
	// try {
	// file.createNewFile();
	// OutputStream outStream = newFileOutputStream(file);
	// bm.compress(Bitmap.CompressFormat.JPEG, 100, outStream);
	// outStream.flush();
	// outStream.close();
	// Log.i(TAG, "Image saved tosd");
	// } catch (FileNotFoundException e) {
	// Log.w(TAG,"FileNotFoundException");
	// } catch (IOException e) {
	// Log.w(TAG,"IOException");
	// }
	// }
	//
	// /**
	// * 计算sdcard上的剩余空间
	// * @return
	// */
	// private int freeSpaceOnSd() {
	// StatFs stat = new StatFs(Environment.getExternalStorageDirectory()
	// .getPath());
	// double sdFreeMB = ((double)stat.getAvailableBlocks() * (double)
	// stat.getBlockSize()) / MB;
	// return (int) sdFreeMB;
	// }
	// /**
	// * 修改文件的最后修改时间
	// * @param dir
	// * @param fileName
	// */
	// private void updateFileTime(String dir,String fileName) {
	// File file = new File(dir,fileName);
	// long newModifiedTime =System.currentTimeMillis();
	// file.setLastModified(newModifiedTime);
	// }
	//
	// /**
	// *计算存储目录下的文件大小，当文件总大小大于规定的CACHE_SIZE或者sdcard剩余空间小于FREE_SD_SPACE_NEEDED_TO_CACHE的规定
	// * 那么删除40%最近没有被使用的文件
	// * @param dirPath
	// * @param filename
	// */
	// private void removeCache(String dirPath) {
	// File dir = new File(dirPath);
	// File[] files = dir.listFiles();
	// if (files == null) {
	// return;
	// }
	// int dirSize = 0;
	// for (int i = 0; i < files.length;i++) {
	// if(files[i].getName().contains(WHOLESALE_CONV)) {
	// dirSize += files[i].length();
	// }
	// }
	// if (dirSize > CACHE_SIZE * MB ||FREE_SD_SPACE_NEEDED_TO_CACHE >
	// freeSpaceOnSd()) {
	// int removeFactor = (int) ((0.4 *files.length) + 1);
	//
	// Arrays.sort(files, new FileLastModifSort());
	//
	// Log.i(TAG, "Clear some expiredcache files ");
	//
	// for (int i = 0; i <removeFactor; i++) {
	//
	// if(files[i].getName().contains(WHOLESALE_CONV)) {
	//
	// files[i].delete();
	//
	// }
	//
	// }
	//
	// }
	//
	// }
	// /**
	// * 删除过期文件
	// * @param dirPath
	// * @param filename
	// */
	// private void removeExpiredCache(String dirPath, String filename) {
	//
	// File file = new File(dirPath,filename);
	//
	// if (System.currentTimeMillis() -file.lastModified() > mTimeDiff) {
	//
	// Log.i(TAG, "Clear some expiredcache files ");
	//
	// file.delete();
	//
	// }
	//
	// }
	// //文件使用时间排序
	//
	// /**
	// * TODO 根据文件的最后修改时间进行排序 *
	// */
	// class FileLastModifSort implements Comparator <File>{
	// public int compare(File arg0, File arg1) {
	// if (arg0.lastModified() >arg1.lastModified()) {
	// return 1;
	// } else if (arg0.lastModified() ==arg1.lastModified()) {
	// return 0;
	// } else {
	// return -1;
	// }
	// }
	// }
}
