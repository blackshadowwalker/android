package com.cxf.net.handler;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.List;

import android.content.Context;
import android.location.LocationManager;

public class LocationHandler {
	private LocationManager locationManager;
	public static final int LOCATION_CHANGE = 70;

	// 0表示由gps获得坐标，1表示由网络获得坐标

	public LocationHandler(Context context) {
		locationManager = (LocationManager) context
				.getSystemService(Context.LOCATION_SERVICE);
	}

	public boolean hasGPSDevice() {
		if (locationManager == null)
			return false;
		final List<String> providers = locationManager.getAllProviders();
		if (providers == null)
			return false;
		return providers.contains(LocationManager.GPS_PROVIDER);
	}

	public boolean isGPSOpen() {
		if (locationManager
				.isProviderEnabled(android.location.LocationManager.GPS_PROVIDER)) {
			return true;
		} else {
			return false;
		}
	}

	public boolean hasNetworkDevice() {
		if (locationManager == null)
			return false;
		final List<String> providers = locationManager.getAllProviders();
		if (providers == null)
			return false;
		return providers.contains(LocationManager.NETWORK_PROVIDER);
	}

	// 获得ip地址
	public String getHostIp() {
		try {
			for (Enumeration<NetworkInterface> en = NetworkInterface
					.getNetworkInterfaces(); en.hasMoreElements();) {
				NetworkInterface intf = en.nextElement();
				for (Enumeration<InetAddress> ipAddr = intf.getInetAddresses(); ipAddr
						.hasMoreElements();) {
					InetAddress inetAddress = ipAddr.nextElement();
					if (!inetAddress.isLoopbackAddress()) {
						return inetAddress.getHostAddress();
					}
				}
			}
		} catch (SocketException ex) {
			return "";
		} catch (Exception e) {
			return "";
		}
		return "";
	}

}
