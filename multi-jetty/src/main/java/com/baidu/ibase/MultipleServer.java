package com.baidu.ibase;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.AbstractHandler;

public interface MultipleServer {
	/**
	 * add handler with prefix, any request with this handler will transform to
	 * new Server
	 * 
	 * @param prefix
	 * @param handler
	 */
	Server addHandle(String prefix, AbstractHandler handler)
			throws ExistingServerException;

	public static class ExistingServerException extends Exception {

		public ExistingServerException(String prefix) {
			super("server existing : " + prefix);
		}

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

	}
}
