package com.baidu.ibase;

import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.Collections;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.servlets.ProxyServlet.Transparent;
import org.eclipse.jetty.util.log.Log;

import com.baidu.ibase.modal.ChildServer;
import com.baidu.ibase.modal.ProxyServer;

public class ProxyProjectServer extends Server implements ProxyServer,
		ChildServer {
	String proxyTo;
	final ArrayList<String> uriList = new ArrayList<String>();

	public ProxyProjectServer(String proxyTo, String... strings) {
		this.proxyTo = proxyTo;
		setProxy(strings);
	}

	public boolean checkProxy(Request baseRequest) {
		boolean checked = false;
		String uri = baseRequest.getRequestURI();
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
		return checked;
	}

	public boolean doHandle(String target, Request baseRequest,
			HttpServletRequest request, HttpServletResponse response) {
		boolean checked = checkProxy(baseRequest);
		if (checked) {
			try {
				handle(target, baseRequest, request, response);
				return true;
			} catch (IOException e) {
				e.printStackTrace();
			} catch (ServletException e) {
				e.printStackTrace();
			}
		}
		return false;
	}

	public int getPort() {
		return 0;
	}

	public String getPrefix() {
		return null;
	}

	public boolean startService() {
		try {
			if (!isRunning())
				this.start();
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	public void setProxy(String... strings) {
		final ServletContextHandler sch = new ServletContextHandler();
		ServletHolder sh = new ServletHolder(Transparent.class);
		Collections.addAll(uriList, strings);
		sh.setInitParameter("Prefix", "/");
		sh.setInitParameter("ProxyTo", proxyTo);
		sch.addServlet(sh, "/*");
		// this.hc.addHandler(sch);
		this.setHandler(sch);
	}

}
