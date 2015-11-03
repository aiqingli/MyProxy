package com.hp.ts.dwf.gss;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


public class GssStore implements GssConstants {

	static Log log = LogFactory.getLog(GssStore.class);
	ConcurrentHashMap<String, GssItem> map = new ConcurrentHashMap<String, GssItem>();
	
	/**
	 * Get the global session store for this session.
	 * @param session
	 * @return
	 */
	public static GssStore getGssStore(HttpSession session) {
		GssStore s = (GssStore) session.getAttribute("gssStore");
		if (s == null) {
			s = new GssStore();
			session.setAttribute("gssStore", s);
		}
		return s;
	}
	
	
	/**
	 * create the GSS request headers based on the last execution time stamp for the 
	 * proxied app.
	 * 
	 * @param appName 
	 * @param ts
	 * @throws IOException 
	 */
	public String getUpdateHdr(String appName, long ts, Serializer s) throws IOException {
		Map<String, Object> updates = new HashMap<String, Object>();
		
		for (String name : map.keySet()) {
			GssItem item = map.get(name);
			if (item.getUpdateTs() > ts || item.isDeleted()
					|| appName.equals(item.getLastApp()))	// only changes from other apps
				continue;
			
			updates.put(name, item.getValue());
		}
		
		if (updates.isEmpty())
			return null;
		return s.serializeUpdates(updates);
	}

	/**
	 * Create GSS delete header.
	 * @param appName
	 * @param ts
	 * @param s
	 * @return
	 * @throws IOException
	 */
	public String getDeleteHdr(String appName, long ts, Serializer s) throws IOException {
		List<String> deletes = new ArrayList<String>();
		
		for (String name : map.keySet()) {
			GssItem item = map.get(name);
			if (item.getUpdateTs() > ts || ! item.isDeleted()
					|| appName.equals(item.getLastApp()))	// only changes from other apps
				continue;
			
			deletes.add(name);
		}
		
		if (deletes.isEmpty())
			return null;
		return s.serializeDeletes(deletes);
	}

	/**
	 * Update the session store with values from feature
	 * @param hdr
	 * @param appName
	 * @param ts
	 * @throws ClassNotFoundException
	 * @throws IOException
	 */
	public void update(String hdr, String appName, long ts, Serializer s) throws ClassNotFoundException, IOException {
		Map<String, Object> updates = s.deserializeUpdates(hdr);
		
		for (String name : updates.keySet()) {
			Object o = updates.get(name);
			GssItem item = new GssItem(name, o, appName, ts);
			log.debug("updating item: " + item);
			map.put(name, item);
		}
	}
	
	/**
	 * Delete values from the session store.
	 * @param hdr
	 * @param appName
	 * @param ts
	 * @param s
	 * @throws ClassNotFoundException
	 * @throws IOException
	 */
	public void delete(String hdr, String appName, long ts, Serializer s) throws ClassNotFoundException, IOException {
		List<String> deletes = s.deserializeDeletes(hdr);
		
		for (String name : deletes) {
			GssItem item = map.get(name);
			if (item == null)
				continue;
			
			item.setLastApp(appName);
			item.setValue(null);
			item.setUpdateTs(ts);
			log.debug("deleting item: " + item);
		}
	}
	
}
