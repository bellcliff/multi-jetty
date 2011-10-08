package com.baidu.ibase;

import org.eclipse.jetty.server.Server;

public class ChildServer extends Server {
	int port;
	String prefix;
	
	public ChildServer(String prefix, int port) {
		super(port);
		this.port = port;
		this.prefix = prefix;
	}
}
