package com.hp.ts.dwf.proxy;

import java.io.Serializable;

import com.hp.ts.dwf.gss.Serializer;

public class ProxyApp implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private String localPath;
	private String remoteProtocol;
	private String remoteHost;
	private int remotePort;
	private String remotePath;
	private String sessionCookieName;
	private Serializer serializer;
	
	public ProxyApp(String localPath, String remoteProtocol, String remoteHost, int remotePort, 
			String remotePath, Serializer serializer) {
		this.localPath = localPath;
		this.remoteProtocol = remoteProtocol;
		this.remoteHost = remoteHost;
		this.remotePort = remotePort;
		this.remotePath = remotePath;
		this.setSerializer(serializer);
	}

	public String getLocalPath() {
		return localPath;
	}

	public void setLocalPath(String localPath) {
		this.localPath = localPath;
	}

	public String getRemotePath() {
		return remotePath;
	}

	public void setRemotePath(String remotePath) {
		this.remotePath = remotePath;
	}

	public boolean match(String uri) {
		return uri.startsWith(localPath);
	}

	public String makeRemoteURI(String uri) {
		String s = uri.substring(localPath.length());
		return remoteProtocol.concat("://").concat(remoteHost).concat(":").concat(Integer.toString(remotePort)).concat(remotePath).concat(s);
	}

	public String getSessionCookieName() {
		return sessionCookieName;
	}

	public void setSessionCookieName(String sessionCookieName) {
		this.sessionCookieName = sessionCookieName;
	}

	@Override
	public String toString() {
		return "ProxyApp [localPath=" + localPath + ", remotePath=" + remotePath
				+ ", sessionCookieName=" + sessionCookieName + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((localPath == null) ? 0 : localPath.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ProxyApp other = (ProxyApp) obj;
		if (localPath == null) {
			if (other.localPath != null)
				return false;
		} else if (!localPath.equals(other.localPath))
			return false;
		return true;
	}

	public String getRemoteProtocol() {
		return remoteProtocol;
	}

	public void setRemoteProtocol(String remoteProtocol) {
		this.remoteProtocol = remoteProtocol;
	}

	public String getRemoteHost() {
		return remoteHost;
	}

	public void setRemoteHost(String remoteHost) {
		this.remoteHost = remoteHost;
	}

	public int getRemotePort() {
		return remotePort;
	}

	public void setRemotePort(int remotePort) {
		this.remotePort = remotePort;
	}

	public Serializer getSerializer() {
		return serializer;
	}

	public void setSerializer(Serializer serializer) {
		this.serializer = serializer;
	}

}
