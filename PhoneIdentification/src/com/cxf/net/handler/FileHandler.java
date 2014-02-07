package com.cxf.net.handler;

import java.io.FileInputStream;
import java.io.IOException;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.MarshalBase64;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import android.content.Context;
import android.os.Handler;

import com.cxf.entity.Constant;

public class FileHandler extends Handler {

	// 上下文
	Context context;

	public FileHandler(Context context) {
		super();
		this.context = context;

	}

	@SuppressWarnings("finally")
	public String uploadFile(String pUniqueToken, String pPhoneNum,
			int nAuthMediaType) throws IOException, XmlPullParserException {
		// 返回的参数
		String result = null;
		String namespace = Constant.NAMESPACE;
		String methoName = Constant.FILE_UPLOAD_METHOD_REQUEST;
		SoapObject soapObject = new SoapObject(namespace, methoName);
		soapObject.addProperty("pUniqueToken", pUniqueToken);
		soapObject.addProperty("pPhoneNum", pPhoneNum);
		soapObject.addProperty("nAuthMediaType", nAuthMediaType);
		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
				SoapEnvelope.VER12);
		envelope.bodyOut = soapObject;

		(new MarshalBase64()).register(envelope);
		// 设置是否调用的是dotNet开发的WebService

		HttpTransportSE httpTranstation = (new ConnectHandler(context))
				.connectToServer();
		if (httpTranstation != null && namespace != null && methoName != null
				&& envelope != null) {
			try {
				httpTranstation.call(namespace + methoName, envelope);

				SoapObject resultObject = (SoapObject) envelope.getResponse();
				if (resultObject != null) {
					int resultCode = Integer.valueOf(resultObject
							.getPropertyAsString("result"));
					if (resultCode == 200) {
						result = resultObject
								.getPropertyAsString("pUploadPath");
					}
				}
			} catch (NullPointerException e) {
				e.printStackTrace();
				throw e;
			} finally {
				return result;
			}
		}

		return result;
	}

	/**
	 * 通过ftp上传文件
	 * 
	 * @param url
	 *            ftp服务器地址
	 * @param port
	 *            端口如 ： 21
	 * @param username
	 *            登录名
	 * @param password
	 *            密码
	 * @param remotePath
	 *            上到ftp服务器的磁盘路径
	 * @param fileNamePath
	 *            要上传的文件路径
	 * @param fileName
	 *            要上传的文件名
	 * @return
	 * @throws FTPException
	 * @throws FTPIllegalReplyException
	 * @throws IOException
	 * @throws IllegalStateException
	 */
	public String ftpUpload(String url, String port, String username,
			String password, String remotePath, String fileNamePath,
			String fileName) throws Exception{
		FTPClient ftpClient = new FTPClient();
		FileInputStream fis = null;
		String returnMessage = "0";
		try {
			ftpClient.setConnectTimeout(5000);
			ftpClient.connect(url, Integer.parseInt(port));
			boolean loginResult = ftpClient.login(username, password);
			int returnCode = ftpClient.getReplyCode();
			if (loginResult && FTPReply.isPositiveCompletion(returnCode)) {// 如果登录成功
				ftpClient.makeDirectory(remotePath);
				// 设置上传目录
				ftpClient.changeWorkingDirectory(remotePath);
				ftpClient.setBufferSize(1024);
				// ftpClient.setControlEncoding("UTF-8");
				ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);
				ftpClient.enterLocalPassiveMode();
				
				fis = new FileInputStream(fileNamePath + fileName);
				ftpClient.storeFile(fileName, fis);

				returnMessage = "1"; // 上传成功
			} else {// 如果登录失败
				returnMessage = "0";
			}

		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException("FTP客户端出错！", e);
		} finally {
			// IOUtils.closeQuietly(fis);
			try {
				ftpClient.disconnect();
			} catch (IOException e) {
				e.printStackTrace();
				throw new RuntimeException("关闭FTP连接发生异常！", e);
			}
		}
		return returnMessage;
	}

}
