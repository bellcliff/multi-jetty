package com.baidu.ibase;

public class ProjectProxyServer extends ProjectServer{

	public ProjectProxyServer(String prefix, MainServer ps, int port, String proxyTo, String...strings)
			throws Exception {
		super(prefix, ps, port);
		ps.addChild(new ProxyProjectServer(proxyTo, strings));
	}

}
