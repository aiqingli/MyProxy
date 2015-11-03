package com.hp.ts.dwf.gss;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;


public class GssFilter implements Filter, GssConstants {

	private String gssNamePrefix = "gss.";
	private Serializer serializer;

	@Override
	public void init(FilterConfig cfg) throws ServletException {
		String s = cfg.getInitParameter("serializer");
		try {
			serializer = (Serializer) Class.forName(s).newInstance();
		} 
		catch (Exception e) {
			throw new ServletException("No such serializer found: " + s, e);
		}
	}

	@Override
	public void destroy() {
	}
	
	private byte[] toByteArray(Object o) throws IOException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ObjectOutputStream oos = new ObjectOutputStream(baos);
		oos.writeObject(o);
		return baos.toByteArray();
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		HttpServletRequest req = (HttpServletRequest) request;
		HttpServletResponse res = (HttpServletResponse) response;
		HttpSession session = req.getSession();
		
		try {
			// Before execution inject session attributes from header 
			Map<String, byte[]> gssAttrs = inject(req, session);
			
			// Execute the chain and request...
			chain.doFilter(request, response);
			
			// After execution compute delta
			Map<String, Object> updates = new HashMap<String, Object>();
			List<String> deletes = new ArrayList<String>();
			
			// check for elements updated by request execution
			@SuppressWarnings("unchecked")
			Enumeration<String> names = session.getAttributeNames();
			while (names.hasMoreElements()) {
				String name = names.nextElement();
				if (! name.startsWith(gssNamePrefix))
					continue;
				Object o = session.getAttribute(name);
				byte[] b = toByteArray(o);
				byte[] b4 = gssAttrs.get(name);
				if (b4 == null || ! Arrays.equals(b, b4)) {
					updates.put(name, o);
					gssAttrs.put(name, b);
				}
			}
			
			// check for deleted attributes
			for (String name : new HashSet<String>(gssAttrs.keySet())) {
				if (session.getAttribute(name) == null) {
					deletes.add(name);
					gssAttrs.remove(name);
				}
			}
			
			// send delta back to proxy with response headers
			if (! updates.isEmpty())
				res.setHeader(HEADER_UPDATES, serializer.serializeUpdates(updates));
			if (! deletes.isEmpty())
				res.setHeader(HEADER_DELETES, serializer.serializeDeletes(deletes));
		} 
		catch (ClassNotFoundException e) {
			throw new ServletException("Failed to deserialize", e);
		}
	}

	@SuppressWarnings("unchecked")
	private Map<String, byte[]> inject(HttpServletRequest req,
			HttpSession session) throws ClassNotFoundException, IOException {
		Map<String, byte[]> gssAttrs = (Map<String, byte[]>) session.getAttribute("_gss.sessionAttrs");
		if (gssAttrs == null) {
			gssAttrs = new HashMap<String, byte[]>();
		}
		
		/* the gss.update header contains a base64 ecoded serialized Map of updated session objects
		 * Map<String, byte[]>
		 * deserialize the Map and then add/update the objects in session and also in the 
		 * serialized copy.
		 */
		String updates = req.getHeader(HEADER_UPDATES);
		if (updates != null) {
			Map<String, Object> m = serializer.deserializeUpdates(updates);
			for (String name : m.keySet()) {
				Object o = m.get(name);
				byte[] b = toByteArray(o);
				session.setAttribute(name, o);
				gssAttrs.put(name, b);
			}
		}
		
		String deletes = req.getHeader(HEADER_DELETES);
		if (deletes != null) {
			List<String> l = serializer.deserializeDeletes(deletes);
			for (String name : l) {
				session.removeAttribute(name);
				gssAttrs.remove(name);
			}
		}
		
		// put the old non-updated of the attributes in the the map
		Enumeration<String> e = session.getAttributeNames();
		while (e.hasMoreElements()) {
			String name = e.nextElement();
			if (gssAttrs.containsKey(name))
				continue;
			byte[] b = toByteArray(session.getAttribute(name));
			gssAttrs.put(name, b);
		}
		
		return gssAttrs;
	}
	
}
