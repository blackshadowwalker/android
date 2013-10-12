package com.cxf.handler;

import java.io.IOException;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;
import com.cxf.entity.CarAlarm;
import com.cxf.entity.SecurityAlarm;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

public class SecurityGuardHandler extends Handler {

	// 上下文
	Context context;
	// 数据集合
	List<CarAlarm> list;

	public SecurityGuardHandler(Context context) {
		super();
		this.context = context;
	}

	@SuppressWarnings("unchecked")
	public List<SecurityAlarm> requestSecurityAlarms(String ssid, int reqLines,
			int level) {
		List<SecurityAlarm> list = null;

		String namespace = "http://webservice.teleframe.com";
		String methoName = "getWarning";
		String url = "http://10.168.1.250:8888/TeleframeService/service/SPSMonitorEndpointService?wsdl";

		SoapObject soapObject = new SoapObject(namespace, methoName);
		soapObject.addProperty("ssid", ssid);
		soapObject.addProperty("reqLines", reqLines);
		soapObject.addProperty("level", level);
		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
				SoapEnvelope.VER11);
		envelope.setOutputSoapObject(soapObject);
		HttpTransportSE httpTranstation = new HttpTransportSE(url);
		try {
			httpTranstation.call(namespace + methoName, envelope);

			Object result = envelope.getResponse();
			String str = (String) result.toString();

			JSONObject jsonuser;

			jsonuser = new JSONObject(str);

			long error = jsonuser.getLong("error"); // 返回0表示成功
			String msg = jsonuser.getString("msg"); // 与error对应的错误解释
			if (error == 0 && "OK".equals(msg)) {
				String dataStr = jsonuser.getString("data");
				list = FromJsonToClassConverter.toList(dataStr,
						SecurityAlarm.class);
				// 获得请求的字符串
				System.out.println(dataStr);
				return list;

			}

		} catch (IOException e) {
			e.printStackTrace();
		} catch (XmlPullParserException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return list;
	}

	public List<CarAlarm> getCarAlarms() {
		return list;
	}

	@Override
	public void handleMessage(Message msg) {
		super.handleMessage(msg);
		Bundle data = msg.getData();
		if (data != null) {
		}
	}

	public int receiveds(String ssid, String jsonIds) {

		int returnCode = 0;
		String namespace = "http://webservice.teleframe.com";
		String methoName = "Receiveds";
		String url = "http://10.168.1.250:8888/TeleframeService/service/SPSMonitorEndpointService?wsdl";

		SoapObject soapObject = new SoapObject(namespace, methoName);
		soapObject.addProperty("ssid", ssid);
		soapObject.addProperty("msgids", jsonIds);

		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
				SoapEnvelope.VER11);
		envelope.setOutputSoapObject(soapObject);
		HttpTransportSE httpTranstation = new HttpTransportSE(url);
		try {
			httpTranstation.call(namespace + methoName, envelope);

			Object result = envelope.getResponse();
			String str = (String) result.toString();
			returnCode = Integer.parseInt(str);
			System.out.println(str);

		} catch (IOException e) {
			e.printStackTrace();
		} catch (XmlPullParserException e) {
			e.printStackTrace();
		}
		return returnCode;
	}

	public String idsJson(List<SecurityAlarm> list) {
		StringBuilder sb = new StringBuilder();
		sb.append("[");
		for (int i = 0; list.size() > 0 && i < list.size(); i++) {
			if (i > 0 && i <= list.size() - 1) {
				sb.append(",{\"id\":\"" + list.get(i).id + "\"}");
			} else {
				sb.append("{\"id\":\"" + list.get(i).id + "\"}");
			}
		}
		sb.append("]");
		return sb.toString();
	}

}
