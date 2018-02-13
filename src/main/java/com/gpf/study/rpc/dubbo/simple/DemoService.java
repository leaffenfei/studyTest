package com.gpf.study.rpc.dubbo.simple;

public interface DemoService {

	String sayHello(String name);
	/**
	 * 查询所有的用户数据
	 * 
	 * @return
	 */
	public String queryAll();
}
