package com.test;

import java.io.File;
import java.io.IOException;
import java.util.Hashtable;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;

public class TestZX {

	/**
	 * @param args
	 * @throws WriterException 
	 * @throws IOException 
	 */
	public static void main(String[] args) throws WriterException, IOException {
		
		  String text = "http://www.baidu.com";  
	        int width = 300;  
	        int height = 300;  
	        //二维码的图片格式  
	        String format = "png";  
	        Hashtable hints = new Hashtable();  
	        //内容所使用编码  
	        hints.put(EncodeHintType.CHARACTER_SET, "utf-8");  
	        BitMatrix bitMatrix = new MultiFormatWriter().encode(text,  
	                BarcodeFormat.QR_CODE, width, height, hints);  
	        //生成二维码  
	        File outputFile = new File("f:"+File.separator+"qr.png");  
	        MatrixToImageWriter.writeToFile(bitMatrix, format, outputFile);

	}

}
