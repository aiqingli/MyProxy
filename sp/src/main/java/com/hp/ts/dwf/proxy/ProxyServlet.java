package com.hp.ts.dwf.proxy;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.Servlet;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class ProxyServlet
 */
public class ProxyServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private List<ProxyApp> apps = new ArrayList<ProxyApp>();
	private Proxy proxy; 

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public ProxyServlet() {
	}

	/**
	 * @see Servlet#init(ServletConfig)
	 */
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		
		proxy = new Proxy(config.getServletContext().getRealPath("/"));
		
		String configFileName = config.getInitParameter("config-file");
		File file = new File(config.getServletContext().getRealPath(configFileName));
		
		try {
			apps.addAll(ConfigFileReader.read(file));
		} catch (IOException e) {
			throw new ServletException("Couldn't read config file: " + configFileName, e);
		}
	}

	
	@Override
	protected void service(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		proxy(req, resp, req.getMethod());
	}

	void proxy(HttpServletRequest request, HttpServletResponse response, String requestMethod) throws ServletException, IOException {
		String uri = request.getRequestURI();
		
		for (ProxyApp app : apps) {
			if (app.match(uri)) {
				try {
					proxy.proxy(app, request, response, requestMethod);
				} 
				catch (ClassNotFoundException e) {
					throw new ServletException(e);
				}
				return;
			}
		}
		
		// default not found app
	}


}
