package com.gpf.study.rpc.dubbo.simple;

import java.util.ArrayList;
import java.util.List;

public class DemoServiceImpl implements DemoService {

	public String sayHello(String name) {
		System.out.println("进入server_sayHello()");
		System.out.println("init : " + name);
		return "hello " + name;
	}
	/**
	 * 实现查询，这里做模拟实现，不做具体的数据库查询
	 */
	public String queryAll() {
		List<User> list = new ArrayList<User>();
		for (int i = 0; i < 10; i++) {
			User user = new User();
			user.setAge(10 + i);
			user.setId(Long.valueOf(i + 1));
			user.setPassword("123456");
			user.setUsername("username_" + i);
			list.add(user);
			
		}
		return list.toString();
	}
}
