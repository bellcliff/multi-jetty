package com.baidu.ibase.modal;

public interface ParentServer {

	/**
	 * 查询一个子服务器
	 * 
	 * @return
	 */
	ChildServer getChild(String prefix);
	
	void addChild(ChildServer cs);
}
