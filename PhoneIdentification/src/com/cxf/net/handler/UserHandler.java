package com.cxf.net.handler;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.MarshalBase64;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import android.content.Context;
import android.os.Handler;
import android.util.Log;

import com.cxf.entity.Constant;
import com.cxf.entity.Verify;
import com.cxf.entity.VerifyItem;

public class UserHandler extends Handler {

	// 上下文
	Context context;
	String TAG = "UserHandler";

	public UserHandler(Context context) {
		super();
		this.context = context;

	}

	public Verify request(String pPhoneNum, String pUtf8PassWord,
			String longitude, String latitude, String pIpAddr)
			throws IOException, XmlPullParserException {
		// 返回的参数
		Verify verify = new Verify();
		String namespace = Constant.NAMESPACE;
		String methoName = Constant.USER_METHOD_REQUEST;
		SoapObject soapObject = new SoapObject(namespace, methoName);
		soapObject.addProperty("pPhoneNum", pPhoneNum);
		soapObject.addProperty("pUtf8PassWord", pUtf8PassWord);
		soapObject.addProperty("nLongitude", longitude);
		soapObject.addProperty("nLatitude", latitude);
		soapObject.addProperty("pIpAddr", pIpAddr);
		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
				SoapEnvelope.VER11);
		envelope.encodingStyle = SoapSerializationEnvelope.ENC;
		envelope.encodingStyle = "gbk";
		envelope.bodyOut = soapObject;

		(new MarshalBase64()).register(envelope);
		// 设置是否调用的是dotNet开发的WebService

		HttpTransportSE httpTranstation = new ConnectHandler(context)
				.connectToServer();
		if (httpTranstation != null && namespace != null && methoName != null
				&& envelope != null) {
			try {
				httpTranstation.call(namespace + methoName, envelope);
				SoapObject resultObject = null;
				if (envelope.getResponse() != null) {
					resultObject = (SoapObject) envelope.getResponse();
				}
				if (resultObject != null) {
					Integer result = Integer.parseInt(resultObject.getProperty(
							"result").toString());
					if (result == 200) {
						String pRandomQuestion = new String(resultObject
								.getPropertyAsString("pRandomQuestion")
								.getBytes("UTF-8"), "utf-8");
						String pAnswer = new String(resultObject
								.getProperty("pAnswer").toString()
								.getBytes("utf-8"), "utf-8");
						String pUniqueToken = new String(resultObject
								.getProperty("pUniqueToken").toString()
								.getBytes("UTF-8"), "UTF-8");
						verify.questionsAndAnswers = new ArrayList<VerifyItem>();
						VerifyItem item = new VerifyItem();
						item.question = pRandomQuestion;
						item.answer = pAnswer;
						verify.uniqueToken = pUniqueToken;
						verify.phoneNum = pPhoneNum;
						verify.password = pUtf8PassWord;
						verify.questionsAndAnswers.add(item);
						verify.returnCode = 200;
					} else {
						verify.returnCode = result;
					}

				}
			} catch (NullPointerException e) {
				e.printStackTrace();
				throw e;

			}
		}

		return verify;
	}

	public int verify(String pPhoneNum, String pUniqueToken)
			throws IOException, XmlPullParserException {
		// 返回的参数
		Integer returnCode = 0;
		String namespace = Constant.NAMESPACE;
		String methoName = Constant.USER_VERIFY;
		SoapObject soapObject = new SoapObject(namespace, methoName);
		soapObject.addProperty("pPhoneNum", pPhoneNum);
		soapObject.addProperty("pUniqueToken", pUniqueToken);
		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
				SoapEnvelope.VER12);
		envelope.bodyOut = soapObject;

		// 设置是否调用的是dotNet开发的WebService

		HttpTransportSE httpTranstation = new ConnectHandler(context)
				.connectToServer();
		if (!(httpTranstation == null || namespace == null || methoName == null || envelope == null)) {
			try {
				httpTranstation.call(namespace + methoName, envelope);
				SoapObject resultObject = null;
				if (envelope.bodyIn != null) {
					resultObject = (SoapObject) envelope.bodyIn;
				}
				if (resultObject != null) {
					returnCode = Integer.parseInt(resultObject.getProperty(
							"result").toString());
				}
			} catch (NullPointerException e) {
				e.printStackTrace();
				throw e;
			}
		}
		return returnCode;
	}

	public int logff(String pPhoneNum, String pUniqueToken) throws IOException,
			XmlPullParserException {
		// 返回的参数
		Integer returnCode = 0;
		String namespace = Constant.NAMESPACE;
		String methoName = Constant.USER_LOGOFF;
		SoapObject soapObject = new SoapObject(namespace, methoName);
		soapObject.addProperty("pPhoneNum", pPhoneNum);
		soapObject.addProperty("pUniqueToken", pUniqueToken);
		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
				SoapEnvelope.VER12);
		envelope.bodyOut = soapObject;

		// 设置是否调用的是dotNet开发的WebService

		HttpTransportSE httpTranstation = (new ConnectHandler(context))
				.connectToServer();
		if (httpTranstation != null && namespace != null && methoName != null
				&& envelope != null) {
			try {
				httpTranstation.call(namespace + methoName, envelope);
				SoapObject resultObject = null;
				if (envelope.getResponse() != null) {
					resultObject = (SoapObject) envelope.getResponse();
				}
				if (resultObject != null
						&& resultObject.getProperty("result") != null) {

					returnCode = Integer.parseInt(resultObject.getProperty(
							"result").toString());

				}
			} catch (NullPointerException e) {
				e.printStackTrace();

			}
		}
		return returnCode;
	}

	public static String toHexString(String s) {
		String str = "";
		for (int i = 0; i < s.length(); i++) {
			int ch = (int) s.charAt(i);
			String s4 = Integer.toHexString(ch);
			str = str + s4;
		}
		return str;
	}

	public int register(String szPhoneNum, String szPassWord,
			String szPicFileBase64File, int nPicFileLen,
			String szVocFileBase64File, int nVocFileLen, List<VerifyItem> list)
			throws IOException, XmlPullParserException {
		// 返回的参数
		int flag = 0;
		String namespace = Constant.NAMESPACE;
		String methoName = Constant.USER_REGISTER_METHOD;
		SoapObject soapObject = new SoapObject(namespace, methoName);
		soapObject.addProperty("szPhoneNum", szPhoneNum);
		soapObject.addProperty("szPassWord", szPassWord);
		int size = list.size();
		int i = 0;
		for (; list != null && i < size; i++) {
			if (list.get(i) != null) {
				soapObject.addProperty("szRadomQuestion" + (i + 1),
						list.get(i).question);
				soapObject.addProperty("szRadomAnswer" + (i + 1),
						list.get(i).answer);
			}
		}
		for (int j = i; j < 6; j++) {
			soapObject.addProperty("szRadomQuestion" + (j + 1), "");
			soapObject.addProperty("szRadomAnswer" + (j + 1), "");
		}
		soapObject.addProperty("szPicFileBase64File", szPicFileBase64File);
		soapObject.addProperty("nPicFileLen", nPicFileLen);
		soapObject.addProperty("szVocFileBase64File", szVocFileBase64File);
		soapObject.addProperty("nVocFileLen", nVocFileLen);
		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
				SoapEnvelope.VER12);
		envelope.encodingStyle = SoapSerializationEnvelope.ENC;
		envelope.bodyOut = soapObject;

		(new MarshalBase64()).register(envelope);
		// 设置是否调用的是dotNet开发的WebService

		HttpTransportSE httpTranstation = new ConnectHandler(context)
				.connectToServer();
		if (httpTranstation != null && namespace != null && methoName != null
				&& envelope != null) {
			try {
				httpTranstation.call(namespace + methoName, envelope);
				SoapObject resultObject = null;
				if (envelope.getResponse() != null) {

					resultObject = (SoapObject) envelope.bodyIn;
					Log.i(TAG, "resultObject:" + resultObject.toString());
				}
				if (resultObject != null) {
					Integer result = Integer.parseInt(resultObject.getProperty(
							"result").toString());
					Log.i(TAG, "resultCode:" + String.valueOf(result));
					if (result == 200) {
						flag = 200;
					}

				}
			} catch (NullPointerException e) {
				e.printStackTrace();
				throw e;
			}
		}

		return flag;
	}

}
