package com.gpf.study.rpc.dubbo.simple;

import org.springframework.context.support.ClassPathXmlApplicationContext;

public class ClientMain {
	public static void main(String[] args) {
		ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(
				new String[] { "com/gpf/study/rpc/dubbo/simple/applicationConsumer.xml" });
		context.start();
		DemoService service = (DemoService) context.getBean("demoService");
		System.out.println("此处进入server调用");
		System.out.println(service.sayHello("gpf"));
		System.out.println(service.queryAll());
		context.close();
	}
}
