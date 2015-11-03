package com.hp.ts.dwf.proxy.inject;

/**
 * Inject header footer into the feature service html.
 * 
 * @author kelly
 */
public interface Injector {

	/**
	 * Inject the header/footer 
	 * @param html
	 * @return html
	 */
	public String inject(String html);
	
}
