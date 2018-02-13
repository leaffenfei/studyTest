package com.gpf.study.java.timer;

import java.util.Date;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;

public class TestJobDetailBean extends QuartzJobBean {
	@Override
    protected void executeInternal(JobExecutionContext arg0) throws JobExecutionException {
        System.out.println(new Date().toString());
    }
}
