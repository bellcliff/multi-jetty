package com.baidu.ibase.modal;

import org.eclipse.jetty.server.Request;

public interface ProxyServer extends ChildServer {
	void setProxy(String... strings);
	boolean checkProxy(Request baseRequest);
}
