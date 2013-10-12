package com.cxf.handler;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.graphics.Bitmap;

import com.cxf.entity.CarAlarm;
import com.cxf.entity.SecurityAlarm;

public class FromJsonToClassConverter {
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static List toList(String jsonStr, Class c) {
		List list = null;
		// 得到JSON对象数组
		JSONArray arr;
		list = new ArrayList<CarAlarm>();
		if (!(jsonStr == null || "\"\"".equals(jsonStr) || "".equals(jsonStr))) {

			try {
				arr = new JSONArray(jsonStr);

				if (CarAlarm.class.getName().equals(c.getName())) {

					for (int i = 0; i < arr.length(); i++) {
						JSONObject temp = (JSONObject) arr.get(i);
						final long id = temp.getLong("id");
						String absTime = temp.getString("absTime");
						String shortImageAStr = temp.getString("shortImageA");
						String location = temp.getString("location");

						int dir = temp.getInt("dir");
						String LPNumber = temp.getString("LPNumber");
						Bitmap shortImageA = null;
						 try {
						 // 加载网络图片
						 shortImageA = ImageLoadHandler
						 .getImage(shortImageAStr);
						 } catch (Exception e) {
						 e.printStackTrace();
						 }
						CarAlarm carAlarm = new CarAlarm(id, LPNumber,dir, location,
								absTime, shortImageA, true);
						list.add(carAlarm);
					}
					if (!list.isEmpty()) {
						Collections.sort(list);
					}

				} else if (SecurityAlarm.class.getName().equals(c.getName())) {
					list = new ArrayList<SecurityAlarm>();
					
					for (int i = 0; i < arr.length(); i++) {
						JSONObject temp = (JSONObject) arr.get(i);
						Long id = temp.getLong("id");
						String absTime = temp.getString("absTime");
						int level = temp.getInt("level");
						String location = temp.getString("location");
						String eventName = temp.getString("eventName");
						String personInCharge = temp
								.getString("personInCharge");
						String desp = temp.getString("desp");
						String shortImageStr = temp.getString("shortImage");

						Bitmap shortImage = null;
						try {
							// 加载网络图片
							shortImage = ImageLoadHandler
									.getImage(shortImageStr);
						} catch (Exception e) {
							e.printStackTrace();
						}

						SecurityAlarm sa = new SecurityAlarm(id,absTime,  location,
								level, eventName, personInCharge,desp,
								shortImage, true);

						list.add(sa);
						
					}
					
					if (!list.isEmpty()) {
						Collections.sort(list);
					}

				}
			} catch (JSONException e) {
				e.printStackTrace();
			}

		}
		return list;
	}

}
