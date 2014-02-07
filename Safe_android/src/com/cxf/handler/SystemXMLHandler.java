package com.cxf.handler;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;
import org.xmlpull.v1.XmlSerializer;

import android.os.Environment;

import com.cxf.entity.RequestSetting;

public class SystemXMLHandler {

	public SystemXMLHandler() {

	}

	public RequestSetting getRequestSetting(InputStream xml) throws Exception {

		String elementname = null;
		XmlPullParserFactory pullFac = null;
		XmlPullParser pullParser = null;
		RequestSetting rs = null;
		if (xml != null) {
			try {
				pullFac = XmlPullParserFactory.newInstance();
				pullParser = pullFac.newPullParser();
				// 设置输入流的编码
				pullParser.setInput(xml, "utf-8");
				// 设置事件变量
				int eventType = pullParser.getEventType();
				boolean hasFind = false;
				// 当事件类型不是结束文档时
				while (!hasFind && eventType != XmlPullParser.END_DOCUMENT) {

					if (eventType == XmlPullParser.START_DOCUMENT) {
					} else if (eventType == XmlPullParser.START_TAG) {

						elementname = pullParser.getName();
						if (elementname.equals("RequestSetting")) {
							rs = new RequestSetting();
						}
					} else if (eventType == XmlPullParser.END_TAG) {

						elementname = pullParser.getName();
						if (elementname.equals("RequestSetting")) {
						}
						elementname = null;
					} else if (eventType == XmlPullParser.TEXT) {
						if ("host".equals(elementname)) {
							rs.host = pullParser.getText();
						}

					}

					try {
						eventType = pullParser.next();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}

			} catch (XmlPullParserException e) {
				e.printStackTrace();
			}
		}

		return rs;
	}

	public List<RequestSetting> getRequestSettings(InputStream xml)
			throws Exception {

		String elementname = null;
		XmlPullParserFactory pullFac = null;
		List<RequestSetting> list = null;
		XmlPullParser pullParser = null;
		if (xml != null) {
			try {
				pullFac = XmlPullParserFactory.newInstance();
				pullParser = pullFac.newPullParser();
				// 设置输入流的编码
				pullParser.setInput(xml, "utf-8");
				// 设置事件变量
				int eventType = pullParser.getEventType();
				RequestSetting rs = null;
				// 当事件类型不是结束文档时
				while (eventType != XmlPullParser.END_DOCUMENT) {

					if (eventType == XmlPullParser.START_DOCUMENT) {
						list = new ArrayList<RequestSetting>();
					} else if (eventType == XmlPullParser.START_TAG) {

						elementname = pullParser.getName();
						if (elementname.equals("RequestSetting")) {
							rs = new RequestSetting();
						}
					} else if (eventType == XmlPullParser.END_TAG) {

						elementname = pullParser.getName();
						if (elementname.equals("RequestSetting")) {
							list.add(rs);
						}
						elementname = null;
					} else if (eventType == XmlPullParser.TEXT) {
						if ("host".equals(elementname)) {
							rs.host = pullParser.getText();
						} 

					}

					try {
						eventType = pullParser.next();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}

			} catch (XmlPullParserException e) {
				e.printStackTrace();
			}
		}
		
		return list;
	}

	public void saveRequestSetting(String filename, RequestSetting rs) {
		if (Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)) {
			File f = null;
			FileOutputStream output = null;
			String path = "" + Environment.getExternalStorageDirectory()
					+ File.separator + filename;
			f = new File(path);
			if (!f.getParentFile().exists()) {
				f.getParentFile().mkdirs();
			}

			try {
				output = new FileOutputStream(f);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}

			if (rs != null) {
				XmlPullParserFactory pullFac = null;
				XmlSerializer xmlSerializer = null;
				try {
					pullFac = XmlPullParserFactory.newInstance();
				} catch (XmlPullParserException e1) {
					e1.printStackTrace();
				}

				try {
					xmlSerializer = pullFac.newSerializer();
				} catch (XmlPullParserException e) {
					e.printStackTrace();
				}

				try {
					xmlSerializer.setOutput(output, "utf-8");
					// 开始创建文档
					xmlSerializer.startDocument("utf-8", null);
					xmlSerializer.startTag(null, "RequestSetting");
					xmlSerializer.startTag(null, "host");
					xmlSerializer.text(rs.host);
					xmlSerializer.endTag(null, "host");
					xmlSerializer.endTag(null, "RequestSetting");
					xmlSerializer.endDocument();
					xmlSerializer.flush();
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				} catch (IllegalStateException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

		}
	}

	public void saveRequestSetting(String filename, List<RequestSetting> list) {
		if (Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)) {
			File f = null;
			FileOutputStream output = null;
			String path = "" + Environment.getExternalStorageDirectory()
					+ File.separator + filename;
			f = new File(path);
			if (!f.getParentFile().exists()) {
				f.getParentFile().mkdirs();
			}

			try {
				output = new FileOutputStream(f);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}

			if (list != null && list.size() > 0) {
				XmlPullParserFactory pullFac = null;
				XmlSerializer xmlSerializer = null;
				try {
					pullFac = XmlPullParserFactory.newInstance();
				} catch (XmlPullParserException e1) {
					e1.printStackTrace();
				}

				try {
					xmlSerializer = pullFac.newSerializer();
				} catch (XmlPullParserException e) {
					e.printStackTrace();
				}

				try {
					xmlSerializer.setOutput(output, "utf-8");
					// 开始创建文档
					xmlSerializer.startDocument("utf-8", null);
					
					for (int i = 0; i < list.size(); i++) {
						xmlSerializer.startTag(null, "RequestSetting");
						RequestSetting rs = list.get(i);
						xmlSerializer.startTag(null, "host");
						xmlSerializer.text(rs.host);
						xmlSerializer.endTag(null, "host");
						xmlSerializer.endTag(null, "RequestSetting");
					}
					
					xmlSerializer.endDocument();
					xmlSerializer.flush();
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				} catch (IllegalStateException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

		}
	}

}
