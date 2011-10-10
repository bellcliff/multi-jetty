package com.baidu.ibase;

import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.UnavailableException;

import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.servlets.ProxyServlet.Transparent;
import org.eclipse.jetty.util.log.Log;

public class ProxyExample {

	public static void main(String... args) {
		Server server = new Server(80);
		final ServletContextHandler sch = new ServletContextHandler();
		final ArrayList<String> uriList = new ArrayList<String>();
		ServletHolder sh = new ServletHolder(Transparent.class) {

			@Override
			public void handle(Request baseRequest, ServletRequest request,
					ServletResponse response) throws ServletException,
					UnavailableException, IOException {
				String uri = baseRequest.getRequestURI();
				boolean checked = false;
				if (uriList.contains(uri)) {
					// 判断当前请求是否包含在列表中
					checked = true;
				} else {
					String refererUrl = baseRequest.getHeader("Referer");
					if (refererUrl != null) {
						// 判断referer
						String referer = URI.create(refererUrl).getPath();
						// 需要转换uri
						Log.info(uri + " - " + referer);
						if (uriList.contains(referer)) {
							uriList.add(uri);
							checked = true;
						}
					}
				}
				if (checked)
					super.handle(baseRequest, request, response);
			}
		};
		uriList.add("/index");
		uriList.add("/builds");
		sh.setInitParameter("Prefix", "/");
		sh.setInitParameter("ProxyTo", "http://10.32.34.115:8080/");
		sch.addServlet(sh, "/*");
		server.setHandler(sch);
		try {
			server.start();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
