package com.gpf.study.thread;


public class TestInterrupt {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		testInterrupt();
	}
	
	public static void testInterrupt() {
		MyThread myThread=new MyThread();
		myThread.start();
		try {
			Thread.sleep(200);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		myThread.interrupt();

/*		try {
			Thread.sleep(12000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		
	}
}
	class MyThread extends Thread {
    public void run(){
        super.run();
        for(int i=0; i<500000; i++){
        	if(this.interrupted()) {
                System.out.println("线程已经终止， for循环不再执行");
                break;
            }
            System.out.println("i="+(i+1));
        }
    }
}