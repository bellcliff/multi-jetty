package com.baidu.ibase;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.Server;

import com.baidu.ibase.modal.ChildServer;
import com.baidu.ibase.modal.ParentServer;

public class MainServer extends Server implements ParentServer {
	final ArrayList<ChildServer> list = new ArrayList<ChildServer>();

	static final int PORT = 28000;

	public MainServer() {
		super(80);
	}

	public ChildServer getChild(String prefix) {
		for (ChildServer cs : list) {
			if (cs.getPrefix().equalsIgnoreCase(prefix))
				return cs;
		}
		return null;
	}

	@Override
	public void handle(String target, Request baseRequest,
			HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		for (ChildServer cs : list) {
			if (cs.doHandle(target, baseRequest, request, response)) {
				break;
			}
		}
		super.handle(target, baseRequest, request, response);
	}

	public int getUsablePort() {
		int port = PORT;
		for (ChildServer cs : list) {
			if (cs.getPort() == 0 || port == cs.getPort())
				port++;
		}
		return port;
	}

	public void addChild(ChildServer cs) {
		this.list.add(cs);
	}

	public void startChild() {
		for (ChildServer cs : list) {
			cs.startService();
		}
	}

	public static void main(String... args) throws Exception {
		MainServer ms = new MainServer();
		ProjectServer ps = new ProjectServer("/t", ms, ms.getUsablePort());
		ps.initHandle();
		ms.addChild(ps);
		
		ProxyProjectServer pps = new ProxyProjectServer("http://10.32.34.115:8080/", "/index", "/builds");
		ms.addChild(pps);
		
//		ProjectProxyServer pps1 = new ProjectProxyServer("/g", ms, ms.getUsablePort(), "http://www.baidu.com/", "/t");

		ms.startChild();
		ms.start();
	}
}
