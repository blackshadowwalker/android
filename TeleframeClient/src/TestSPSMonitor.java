import java.net.MalformedURLException;
import java.net.URL;

import javax.xml.namespace.QName;
import net.sf.json.JSONObject;
import com.teleframe.webservice.UserEndpoint;
import com.teleframe.webservice.UserEndpointService;


public class TestSPSMonitor {
	
	static java.util.Timer timer = null;
	static SPSMonitorTimer itsTimer=null;
	static String BASE_WSLD_URL = Util.SOAP_SERVER;
	static String UserEndpointName = "UserEndpointService";
	static String targetNamespace = "http://webservice.teleframe.com";

	public void start() throws MalformedURLException{
		
		URL url = new URL(BASE_WSLD_URL+UserEndpointName+"?wsdl");
		QName qname=  new QName(targetNamespace, UserEndpointName);
		UserEndpointService UserService = new UserEndpointService(url, qname);
		UserEndpoint user = UserService.getUserEndpoinInstance();

		String checkUser = user.checkUser("karl", "karl");
		System.out.println(checkUser);
		JSONObject jsonuser = JSONObject.fromObject(checkUser);
		long error = jsonuser.getLong("error");
		String msg = jsonuser.getString("msg");
		String ssid = jsonuser.getString("data");
		
		itsTimer = new SPSMonitorTimer(ssid);
		if(error==0)
		{
			timer = new java.util.Timer(true);
			timer.schedule(itsTimer, 0, 3000);
			System.out.println("timer started!");

		}else
			System.out.println(msg);
		
		
		try {
			Thread.sleep(10000000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {

		TestSPSMonitor t = new TestSPSMonitor();
		try {
			t.start();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}

	}

}
