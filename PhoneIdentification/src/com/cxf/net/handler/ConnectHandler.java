package com.cxf.net.handler;

import org.ksoap2.transport.HttpTransportSE;

import android.content.Context;
import com.cxf.entity.MyApplication;

public class ConnectHandler {
	Context context;
	MyApplication app;
	final int TIMEOUT=30000;
	public ConnectHandler(Context context) {
		this.context=context;
		app=(MyApplication) context.getApplicationContext();
	}

	public  HttpTransportSE connectToServer() {

		if(app!=null)
		{
			String url=app.url;
			if(url!=null)
			{
				if(url.endsWith("/"))
				{
				url=url+"teleframe?wsdl";
				}
				else
				{
					url=url+"/teleframe?wsdl";
				}
				return new HttpTransportSE(url,TIMEOUT);
			}
		}
		return new HttpTransportSE("",TIMEOUT);
	}
}
