package com.hp.ts.dwf.proxy.path;

import com.hp.ts.dwf.proxy.ProxyApp;

public class SimplePathTranslater implements PathTranslater {

	@Override
	public String translate(ProxyApp app, String html) {
		return html.replace(app.getRemotePath(), app.getLocalPath());
	}

}
