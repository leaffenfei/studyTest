package com.gpf.study.java.timer;

import java.text.SimpleDateFormat;
import java.util.Date;

public class TestJob2 {
	SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	public void doSomething() {
        System.out.println("进入方法doSomething() ：当前时间:" +simpleDateFormat.format(new Date()));
    }
}
