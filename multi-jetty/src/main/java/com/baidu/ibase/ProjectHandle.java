package com.baidu.ibase;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;

public class ProjectHandle extends AbstractHandler {
	final String prefix;
	final ChildServer server;

	public ProjectHandle(String prefix, ChildServer server) {
		this.prefix = prefix;
		this.server = server;
	}

	public void handle(String target, Request baseRequest,
			HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		if (target.startsWith(prefix)) {
			server.handle(target, baseRequest, request, response);
			baseRequest.setHandled(true);
		}
	}

}
