package com.gpf.study.java.timer;

import java.io.IOException;

import org.springframework.context.support.ClassPathXmlApplicationContext;

public class TimerMain {
	public static void main(String[] args) throws IOException {

		ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext( new String[] { "com/gpf/study/java/timer/applicationContext2.xml" });
		context.start();
		System.out.println("输入任意按键退出");
		System.in.read();
		context.close();
	}
}
