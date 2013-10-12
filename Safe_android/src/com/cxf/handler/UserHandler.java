package com.cxf.handler;

import java.io.IOException;

import org.json.JSONException;
import org.json.JSONObject;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;
import android.content.Context;
import android.os.Handler;

public class UserHandler extends Handler {

	// 上下文
	Context context;
	String ssid;

	public UserHandler(Context context) {
		super();
		this.context = context;
	}

	public String getSsid() {
		return ssid;
	}

	public String login(String name, String password) {
		String ssid = null;
		String namespace = "http://webservice.teleframe.com";
		String methoName = "checkUser";
		String url = "http://10.168.1.250:8888/TeleframeService/service/UserEndpointService?wsdl";
		SoapObject soapObject = new SoapObject(namespace, methoName);
		soapObject.addProperty("username", name);
		soapObject.addProperty("password", password);
		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
				SoapEnvelope.VER11);
		envelope.setOutputSoapObject(soapObject);

		try {
			HttpTransportSE httpTranstation = new HttpTransportSE(url);
			httpTranstation.call(namespace + methoName, envelope);

			Object result = envelope.getResponse();
			String str = (String) result.toString();
			// 获得ssid
			JSONObject jsonuser;

			jsonuser = new JSONObject(str);

			long error = jsonuser.getLong("error"); // 返回0表示成功
			String msg = jsonuser.getString("msg"); // 与error对应的错误解释
			if (error == 0 && "OK".equals(msg)) {
				ssid = jsonuser.getString("data");// 分配给用户的SSID,
				return ssid;
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (XmlPullParserException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return ssid;
	}


}
