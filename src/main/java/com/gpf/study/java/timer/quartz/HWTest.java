package com.gpf.study.java.timer.quartz;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class HWTest {

    public static void main(String[] args) {
        ApplicationContext ac = new ClassPathXmlApplicationContext("com/gpf/study/java/timer/spring_quartz.xml");
    }
}