package com.hp.ts.dwf.proxy;

import java.io.Serializable;
import java.util.Hashtable;
import java.util.Map;

import javax.servlet.http.HttpSession;

public class AppData implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private String sessionCookie;
	private long lastRequestTimestamp;

	public String getSessionCookie() {
		return sessionCookie;
	}

	public void setSessionCookie(String sessionCookie) {
		this.sessionCookie = sessionCookie;
	}

	public long getLastRequestTimestamp() {
		return lastRequestTimestamp;
	}

	public void setLastRequestTimestamp(long lastRequestTimestamp) {
		this.lastRequestTimestamp = lastRequestTimestamp;
	}
	
	
	public static void setLastRequestTimestamp(HttpSession session, ProxyApp app,
			long ts) {
		AppData data = getAppData(session, app);
		data.setLastRequestTimestamp(ts);
	}

	public static void setSessionCookie(HttpSession session, ProxyApp app, String sessionCookie) {
		AppData data = getAppData(session, app);
		data.setSessionCookie(sessionCookie);
	}

	public static String getSessionCookie(HttpSession session, ProxyApp app) {
		AppData data = getAppData(session, app);
		return data.getSessionCookie();
	}

	public static AppData getAppData(HttpSession session, ProxyApp app) {
		@SuppressWarnings("unchecked")
		Map<ProxyApp, AppData> dataMap = (Map<ProxyApp, AppData>) session.getAttribute("appDataMap");
		
		if (dataMap == null) {
			dataMap = new Hashtable<ProxyApp, AppData>();
			session.setAttribute("appDataMap", dataMap);
		}
		
		AppData data = dataMap.get(app);
		if (data == null) {
			data = new AppData();
			dataMap.put(app, data);
		}
		
		return data;
	}
}
