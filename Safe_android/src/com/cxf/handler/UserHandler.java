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
import android.content.SharedPreferences;
import android.os.Handler;
import com.cxf.entity.Constant;

public class UserHandler extends Handler {

	// 上下文
	Context context;
	String ssid = null;
	SharedPreferences sp;

	public UserHandler(Context context) {
		super();
		this.context = context;
		sp = context.getSharedPreferences("sys_setting", 0);

	}

	public String getSsid() {
		return ssid;
	}

	public String login(String name, String password) {

		String host = sp.getString("host", "");
		Constant.init(host);
		String namespace = Constant.NAMESPACE;
		String methoName = Constant.USER_METHOD_CHECKUSER;
		String url = Constant.USER_URL;
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
			System.out.println(str);
			// 获得ssid
			JSONObject jsonuser;

			jsonuser = new JSONObject(str);

			long error = jsonuser.getLong("error"); // 返回0表示成功
			String msg = jsonuser.getString("msg"); // 与error对应的错误解释
			if (error == 0 && "OK".equals(msg)) {
				ssid = jsonuser.getString("data");// 分配给用户的SSID,
				String user=jsonuser.getString("username");
				sp.edit().putString("nameuser", user).commit();
				
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

/**
 * 修改密码
 * @param ssid 
 * @param oldpwd 就密码
 * @param newpwd 新密码
 * @return
 */
	public String modifyPassword(String ssid, String oldpwd, String newpwd) {
		String result = "修改失败！";
		String host = sp.getString("host", "");
		Constant.init(host);
		String namespace = Constant.NAMESPACE;
		String methoName = Constant.USER_METHOD_MODIFYPASSWORD;
		String url = Constant.USER_URL;
		SoapObject soapObject = new SoapObject(namespace, methoName);
		soapObject.addProperty("ssid", ssid);
		soapObject.addProperty("oldpwd", oldpwd);
		soapObject.addProperty("newpwd", newpwd);
		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
				SoapEnvelope.VER11);
		envelope.setOutputSoapObject(soapObject);

		try {
			HttpTransportSE httpTranstation = new HttpTransportSE(url);
			httpTranstation.call(namespace + methoName, envelope);

			Object resultObject = envelope.getResponse();
			String resultStr = (String) resultObject.toString();
			System.out.println(resultStr);
			// 获得ssid
			JSONObject jsonuser;

			 jsonuser = new JSONObject(resultStr);
			 long error = jsonuser.getLong("error"); // 返回0表示成功
			 if (error == 0 ) {
			 result = jsonuser.getString("msg");// 分配给用户的SSID,
			 if(" Modify Password Success.".equals(result))
				 result="修改成功";
			 sp.edit().putString("password", newpwd).commit();
			 return result;
			 }
		} catch (IOException e) {
			e.printStackTrace();
		} catch (XmlPullParserException e) {
			e.printStackTrace();
		}
		 catch (JSONException e) {
		 e.printStackTrace();
		 }
		return result;
	}
	
	public String logoff(String ssid)
	{
		String result = "";
		String host = sp.getString("host", "");
		Constant.init(host);
		String namespace = Constant.NAMESPACE;
		String methoName = Constant.USER_METHOD_LOGOFF;
		String url = Constant.USER_URL;
		SoapObject soapObject = new SoapObject(namespace, methoName);
		soapObject.addProperty("ssid", ssid);
		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
				SoapEnvelope.VER11);
		envelope.setOutputSoapObject(soapObject);

		try {
			HttpTransportSE httpTranstation = new HttpTransportSE(url);
			httpTranstation.call(namespace + methoName, envelope);

			Object resultObject = envelope.getResponse();
			String resultStr = (String) resultObject.toString();
			System.out.println(resultStr);
			// 获得ssid
			JSONObject jsonuser;

			 jsonuser = new JSONObject(resultStr);
			 long error = jsonuser.getLong("error"); // 返回0表示成功
			 if (error == 0 ) {
			 result = jsonuser.getString("msg");// 分配给用户的SSID,
			 return result;
			 }
		} catch (IOException e) {
			e.printStackTrace();
		} catch (XmlPullParserException e) {
			e.printStackTrace();
		}
		 catch (JSONException e) {
		 e.printStackTrace();
		 }
		return result;
	}

}
