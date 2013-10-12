import java.net.MalformedURLException;
import java.net.URL;

import javax.xml.namespace.QName;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.teleframe.webservice.ITSMonitorEndpoint;
import com.teleframe.webservice.ITSMonitorEndpointService;
import com.teleframe.webservice.SPSMonitorEndpoint;
import com.teleframe.webservice.SPSMonitorEndpointService;
import com.teleframe.webservice.UserEndpoint;
import com.teleframe.webservice.UserEndpointService;


public class SPSMonitorTimer extends java.util.TimerTask  {

	private String SSID = "";
	SPSMonitorEndpoint sps = null;
	private static long times = 0;
	static String BASE_WSLD_URL = Util.SOAP_SERVER;
	static String UserEndpointName = "SPSMonitorEndpointService";
	static String targetNamespace = "http://webservice.teleframe.com";
	
	public SPSMonitorTimer(){}
	
	public SPSMonitorTimer(String ssid){
		this.SSID = ssid;
		QName qname=  new QName(targetNamespace, UserEndpointName);
		URL url = null;
		try {
			url = new URL(BASE_WSLD_URL+UserEndpointName+"?wsdl");
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		SPSMonitorEndpointService ITSservice = new SPSMonitorEndpointService(url, qname);
		sps = ITSservice.getSPSMonitorEndpointInstance();
	}

	@Override
	public void run() {
		times++;
		System.out.println("ITSMonitorTimer Runing...@ "+times+"  SSID="+this.SSID);

		String warning = sps.getWarning(this.SSID, 5, -1);////获得车辆告警信息
		System.out.println(warning);
		JSONObject jsonWarning = JSONObject.fromObject(warning);
		
		JSONArray jsonIds = new JSONArray();
		Object obj = jsonWarning.get("data");
		if(obj instanceof net.sf.json.JSONArray ){
			JSONArray jsonArray = (JSONArray) jsonWarning.getJSONArray("data");
			for(int i=0;i<jsonArray.size();i++){  
				JSONObject jsonObject = JSONObject.fromObject(jsonArray.get(i));  
				long id = jsonObject.getLong("id");
				JSONObject jsonId = new JSONObject();
				jsonId.put("id", id);
				jsonIds.add(jsonId);
				System.out.println("recevied data @ id="+id);
			}
			if(jsonIds.size()>0){
				System.out.println("recevieds @ ids = "+jsonIds);
				sps.receiveds(this.SSID, jsonIds.toString());////返回通知服务器接收成功
			}
			
		}
	}

}
