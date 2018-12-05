package com.gpf.study.thread;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TestCotroller {
/*	public static void main(String[] args) {
		// TODO Auto-generated method stub
		// testOriginalThreadPool();
		testMyFixedThreadPooFrom16();
		// System.exit(0);
	}*/

	/*public static void testMyFixedThreadPooFrom16() {
		// ThreadPoolExecutorFromJDK6_backup_format_execute
		// threadPoolExecutorFromJDK6=new
		// ThreadPoolExecutorFromJDK6_backup_format_execute(3);
		ThreadPoolExecutorFromJDK6 threadPoolExecutorFromJDK6 = new ThreadPoolExecutorFromJDK6(3);

		for (int i = 0; i < 10000; i++) {
			if(i==100){
				System.out.println("执行任务总数:" + threadPoolExecutorFromJDK6.getCompletedTaskCount());
				System.out.println("开始shutdown");
				threadPoolExecutorFromJDK6.shutdown();
			}
			final int index = i;
			threadPoolExecutorFromJDK6.execute(new Runnable() {

				public void run() {
					System.out.println(Thread.currentThread().getName() + "执行添加的任务" + index);
					// Thread.sleep(2000);
				}
			});
		}
		System.out.println("主线程" + Thread.currentThread().getName() + "开始睡眠");
		try {
			Thread.currentThread().sleep(5000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//System.out.println("主线程" + Thread.currentThread().getName() + "睡眠结束");
		

		System.out.println("主线程睡眠结束，线程池开始执行新的循环任务，");
		for (int i = 0; i < 100; i++) {
			final int index = i;
			threadPoolExecutorFromJDK6.execute(new Runnable() {
				public void run() {
					System.out.println(Thread.currentThread().getName() + "第二次执行添加的任务" + index);
					// Thread.sleep(2000);
				}
			});
		}

		System.out.println("执行任务总数:" + threadPoolExecutorFromJDK6.getCompletedTaskCount());
	}

	public static void testOriginalThreadPool() {
		ExecutorService fixedThreadPool = Executors.newFixedThreadPool(3);
		for (int i = 0; i < 10000; i++) {
			final int index = i;
			fixedThreadPool.execute(new Runnable() {
				public void run() {
					System.out.println(Thread.currentThread().getName() + "执行添加的任务" + index);

				}
			});

		}
		try {
			Thread.sleep(7000);
			System.out.println("执行ShutDown()，关闭线程池");
			// fixedThreadPool.shutdown(); //关闭线程池
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		for (int i = 0; i < 100; i++) {
			final int index = i;
			fixedThreadPool.execute(new Runnable() {

				public void run() {
					System.out.println(Thread.currentThread().getName() + "执行新一轮添加的任务" + index);
					// Thread.sleep(2000);
				}
			});
		}

		Thread.currentThread().getState();

	}*/

}
