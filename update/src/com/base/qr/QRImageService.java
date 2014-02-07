package com.base.qr;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.imageio.ImageIO;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.base.qr.QRCodeUtil;

/*
 * Input :  text
 * Output : QRCode image
 * e.g:
 *  http://10.168.1.110/TeleframeService/qrservice?text=http://www.baidu.com&logo="logo/logo.jpg"
 *  http://10.168.1.110/TeleframeService/qrservice?text=http://www.baidu.com&logo="http://www.baidu.com/img/bdlogo.gif"
 */
public class QRImageService extends HttpServlet {
	
	SimpleDateFormat fileNameTimeFormat= new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss-SSS");

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException 
	{
		
		response.setContentType("text/html");
		response.setCharacterEncoding("UTF-8");
		
		String qrName = fileNameTimeFormat.format(new Date())+".jpg";//文件名称 
		
		String text = (String) request.getParameter("text");
		if(text==null)
		{
			this.Help(request, response);
			
		}else{
			
			ServletContext sc = this.getServletContext();
			String logoPath = (String) sc.getInitParameter("logoPath");
			if(sc==null)
				return;
			String qrServicePath = sc.getInitParameter("qrServicePath");
			if(qrServicePath==null){
				qrServicePath = "qrimage";
			}
			String thisPath = sc.getRealPath("/");
			String thisName = sc.getContextPath();
			thisPath = thisPath.replaceAll("\\\\", "/");
			String qrImageRoot = thisPath;
			
			if(qrServicePath.startsWith("/"))
			{
				qrImageRoot = qrImageRoot.substring(0, qrImageRoot.lastIndexOf(thisName));
			}
			
			response.setContentType("image/jpeg");//设置相应类型,告诉浏览器输出的内容为图片
	        response.setHeader("Pragma", "No-cache");//设置响应头信息，告诉浏览器不要缓存此内容
	        response.setHeader("Cache-Control", "no-cache");
	        response.setDateHeader("Expire", 0);
			BufferedImage image;
			try {
				image = QRCodeUtil.encodeStream(text, 
						thisPath+"/"+logoPath, 
						qrImageRoot+"/"+qrServicePath+"/"+qrName, 
						true);
				ImageIO.write(image, "JPEG", response.getOutputStream());
			} catch (Exception e) {
				e.printStackTrace();
			}
			System.out.println(qrImageRoot+"/"+qrServicePath+"/"+qrName);
			//response.getWriter().write("sucess");
		}
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		super.doPost(req, resp);
	}
	
	public void Help(HttpServletRequest request, HttpServletResponse response){

		RequestDispatcher rd = request.getRequestDispatcher("modules/help/qrcode.jsp");   
		try {
			rd.forward(request,response);
		} catch (ServletException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
