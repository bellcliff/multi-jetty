package com.baidu.ibase.modal;

public interface ParentServer {

	/**
	 * ��ѯһ���ӷ�����
	 * 
	 * @return
	 */
	ChildServer getChild(String prefix);
	
	void addChild(ChildServer cs);
}
