package com.hp.ts.dwf.proxy.path;

import com.hp.ts.dwf.proxy.ProxyApp;

public interface PathTranslater {

	String translate(ProxyApp app, String html);

}
