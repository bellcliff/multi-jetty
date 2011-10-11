package com.baidu.ibase;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.HandlerCollection;
import org.eclipse.jetty.webapp.WebAppContext;

import com.baidu.ibase.modal.ChildServer;
import com.baidu.ibase.modal.ParentServer;

public class ProjectServer extends Server implements ChildServer {
	String prefix;
	int port;
	MainServer ps;
	HandlerCollection hc;

	public ProjectServer(String prefix, MainServer ps, int port)
			throws Exception {
		super(port);
		this.prefix = prefix;
		this.ps = ps;
		this.port = port;
		this.hc = new HandlerCollection();
		this.setHandler(this.hc);
	}
	
	public void setPort(int port) {
		getConnectors()[0].setPort(port);
	}

	public boolean startService() {
		try {
			this.start();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	public void initHandle() {
		this.hc.addHandler(new WebAppContext(
				"D:\\workspace\\git\\Tangram-base", prefix));
	}

	public ParentServer getParent() {
		return this.ps;
	}

	public int getPort() {
		return port;
	}

	public String getPrefix() {
		return this.prefix;
	}

	public boolean doHandle(String target, Request baseRequest,
			HttpServletRequest request, HttpServletResponse response) {
		if (target.startsWith(this.prefix)) {
			try {
				this.handle(target, baseRequest, request, response);
				baseRequest.setHandled(true);
				return true;
			} catch (IOException e) {
				e.printStackTrace();
			} catch (ServletException e) {
				e.printStackTrace();
			}
		}
		return false;
	}

}
