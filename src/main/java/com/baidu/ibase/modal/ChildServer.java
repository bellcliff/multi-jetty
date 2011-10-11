package com.baidu.ibase.modal;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.jetty.server.Request;

public interface ChildServer {

	int getPort();
	void setPort(int port);

//	String getPrefix();

	boolean doHandle(String target, Request baseRequest,
			HttpServletRequest request, HttpServletResponse response);
	
	boolean startService();
}
