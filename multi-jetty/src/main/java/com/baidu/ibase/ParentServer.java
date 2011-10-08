package com.baidu.ibase;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.AbstractHandler;
import org.eclipse.jetty.server.handler.HandlerCollection;
import org.eclipse.jetty.webapp.WebAppContext;

public class ParentServer extends Server implements MultipleServer {

	public static void main(String... args) throws Exception {
		ParentServer server = new ParentServer();

		server.start();

		server.addHandle("/a", new WebAppContext(
				"D:\\workspace\\git\\Tangram-base", "/a"));
	}

	public ParentServer() {
		super(8080);
		this.setHandler(hc);
	}

	ArrayList<ChildServer> childServerList = new ArrayList<ChildServer>();
	HandlerCollection hc = new HandlerCollection();

	public Server addHandle(String prefix, AbstractHandler handler)
			throws ExistingServerException {
		// 查找合适端口并建立服务器
		int port = 28000;
		for (ChildServer p : childServerList) {
			if (p.prefix.equalsIgnoreCase(prefix))
				throw new ExistingServerException(prefix);
			port = port == p.port ? p.port + 1 : port;
		}
		ChildServer server = new ChildServer(prefix, port);
		server.setHandler(handler);
		childServerList.add(server);

		// // 建立基于prefix的服务器映射
		// this.hc.addHandler(new ProjectHandle(prefix, server));

		// 启动服务器
		try {
			server.start();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

		return server;
	}

	@Override
	public void handle(String target, Request baseRequest,
			HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		for (ChildServer cs : childServerList) {
			if (target.startsWith(cs.prefix)) {
				cs.handle(target, baseRequest, request, response);
				baseRequest.setHandled(true);
				break;
			}
		}

		super.handle(target, baseRequest, request, response);
	}

}
