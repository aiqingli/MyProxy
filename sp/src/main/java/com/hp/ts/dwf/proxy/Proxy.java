package com.hp.ts.dwf.proxy;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletInputStream;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.hp.ts.dwf.gss.GssConstants;
import com.hp.ts.dwf.gss.GssStore;
import com.hp.ts.dwf.proxy.inject.FileInjector;
import com.hp.ts.dwf.proxy.inject.Injector;
import com.hp.ts.dwf.proxy.path.PathTranslater;
import com.hp.ts.dwf.proxy.path.SimplePathTranslater;

public class Proxy {
	
	static Log log = LogFactory.getLog(Proxy.class);
	
	private PathTranslater pathTranslater;
	private Injector injector;
	
	public Proxy(String realPath) {
		injector = new FileInjector(realPath);
		pathTranslater = new SimplePathTranslater();
	}

	public void proxy(ProxyApp app, HttpServletRequest request, HttpServletResponse response,
			String requestMethod) throws IOException, ClassNotFoundException {
		String uri = request.getRequestURI();
		String remoteUrl = app.makeRemoteURI(uri);
		log.debug(String.format("proxying: %s ==> %s", uri, remoteUrl));
		
		HttpSession session = request.getSession();
		
		long start = System.currentTimeMillis();
		AppData.setLastRequestTimestamp(session, app, start);
		
		URL url = new URL(remoteUrl);
		URLConnection con = url.openConnection();
		
		// send 
		log.debug(" ===> Sending request:");
//		@SuppressWarnings("unchecked")
//		Enumeration<String> e = request.getHeaderNames();
//		while (e.hasMoreElements()) {
//			String h = e.nextElement();
//			log.debug(h + ":" + request.getHeader(h));
//			
//			// TODO set the headers the aren't black listed
//		}
		
		String sessionCookie = AppData.getSessionCookie(session, app);
		if (sessionCookie != null) {
			// set it 
			String sc = app.getSessionCookieName() + "=" + sessionCookie;
			con.addRequestProperty("Cookie", sc);
			log.debug("setting session cookie: " + sc);
		}
		
		String gssUpdatesHdr = GssStore.getGssStore(session).getUpdateHdr(app.getLocalPath(), 
				start, app.getSerializer());
		if (gssUpdatesHdr != null) {
			log.debug("Setting updates header: " + gssUpdatesHdr);
			con.addRequestProperty(GssConstants.HEADER_UPDATES, gssUpdatesHdr);
		}
		
		String gssDeletesHdr = GssStore.getGssStore(session).getDeleteHdr(app.getLocalPath(), 
				start, app.getSerializer());
		if (gssDeletesHdr != null) {
			log.debug("Setting deletes header: " + gssDeletesHdr);
			con.addRequestProperty(GssConstants.HEADER_DELETES, gssDeletesHdr);
		}
		
		ServletInputStream rin = request.getInputStream();
		ByteArrayOutputStream bax = new ByteArrayOutputStream();
		int b = 0;
		while ((b = rin.read()) >= 0)
			bax.write(b);
		if (bax.size() > 0) {
			OutputStream out = con.getOutputStream();
			out.write(bax.toByteArray());
		}	
		
		// read
		log.debug(" ===> Reading response:");
		InputStream in = con.getInputStream();
		String contentType = con.getContentType();
		String encoding = con.getContentEncoding();
		
		if (encoding == null) {
			if (contentType.contains(";charset=")) {
				int pos = contentType.lastIndexOf(";charset=");
				encoding = contentType.substring(pos + 9);
			}
			else
				encoding = "UTF-8";
		}
		Map<String, List<String>> headers = con.getHeaderFields();
		for (String headerName : headers.keySet()) {
			log.debug(headerName + ": " + headers.get(headerName));
			
			// check for session cookies to manage
			if ("Set-Cookie".equalsIgnoreCase(headerName)) {
				List<String> values = headers.get(headerName);
				for (String value : values) {
					String[] v = value.split(";");
					if (v[0].startsWith(app.getSessionCookieName())) {
						sessionCookie = v[0].substring(app.getSessionCookieName().length()+1);
						log.debug(sessionCookie);
						AppData.setSessionCookie(session, app, sessionCookie);
					}
				}
			}
			
			// check for gss.updates 
			if (GssConstants.HEADER_UPDATES.equals(headerName)) {
				GssStore.getGssStore(session).update(headers.get(headerName).get(0), app.getLocalPath(), 
						start, app.getSerializer());
			}
			
			// check for gss.deletes
			if (GssConstants.HEADER_DELETES.equals(headerName)) {
				GssStore.getGssStore(session).delete(headers.get(headerName).get(0), app.getLocalPath(), 
						start, app.getSerializer());
			}
		}
		
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		
		response.setContentType(contentType);
		ServletOutputStream rout = response.getOutputStream();
		
		int x = 0;
		while ((x = in.read()) >= 0)
			baos.write(x);
		
		if (contentType.contains("html")) {
			// translate paths
			String html = baos.toString(encoding);
			html = pathTranslater.translate(app, html);
			
			html = injector.inject(html);
			rout.write(html.getBytes(encoding));
		}
		else
			rout.write(baos.toByteArray());
	}

	
	
}
