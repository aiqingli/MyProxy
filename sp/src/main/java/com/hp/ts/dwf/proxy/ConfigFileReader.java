package com.hp.ts.dwf.proxy;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.hp.ts.dwf.gss.JavaSerializer;
import com.hp.ts.dwf.gss.JsonSerializer;
import com.hp.ts.dwf.gss.Serializer;

public class ConfigFileReader {
	
	private static final String PROP_LOCAL_PATH = "localPath";
	private static final String PROP_REMOTE_PROTOCOL = "remoteProtocol";
	private static final String PROP_REMOTE_HOST = "remoteHost";
	private static final String PROP_REMOTE_PORT = "remotePort";
	private static final String PROP_REMOTE_PATH = "remotePath";
	private static final String PROP_SESSION_COOKIE_NAME = "sessionCookieName";
	private static final String PROP_GSS_SERIALIZATION = "gssSerialization";
	
	static Log log = LogFactory.getLog(ConfigFileReader.class);

	public static Collection<ProxyApp> read(File file) throws IOException {
		ArrayList<ProxyApp> apps = new ArrayList<ProxyApp>();
		
		log.info(String.format("Reading config file: %s", file.getAbsolutePath()));
		Properties props = new Properties();
		props.load(new FileInputStream(file));
		
		for (int i = 1; ; i++) {
			String appPrefix = String.format("app%d.", i);
			String localPath = props.getProperty(appPrefix + PROP_LOCAL_PATH);
			if (localPath == null)
				break;
			
			String remoteProtocol = props.getProperty(appPrefix + PROP_REMOTE_PROTOCOL);
			String remoteHost = props.getProperty(appPrefix + PROP_REMOTE_HOST);
			int remotePort = Integer.parseInt(props.getProperty(appPrefix + PROP_REMOTE_PORT));
			String remotePath = props.getProperty(appPrefix + PROP_REMOTE_PATH);
			String gss = props.getProperty(appPrefix + PROP_GSS_SERIALIZATION);
			Serializer serializer = null;
			if ("json".equalsIgnoreCase(gss))
				serializer = new JsonSerializer();
			else 
				serializer = new JavaSerializer();
			ProxyApp app = new ProxyApp(localPath, remoteProtocol, remoteHost, remotePort, 
					remotePath, serializer);
			
			String sessionCookieName = props.getProperty(appPrefix + PROP_SESSION_COOKIE_NAME);
			app.setSessionCookieName(sessionCookieName);
			
			log.debug("Adding proxy app: " + app.toString());
			apps.add(app);
		}
		
		return apps;
	}

}
