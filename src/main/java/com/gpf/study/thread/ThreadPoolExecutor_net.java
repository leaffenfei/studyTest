package com.gpf.study.thread;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * 
 * @author GPF
 */
public class ThreadPoolExecutor_net {
	private final int WORKSIZE;
    private List<Work> works=Collections.synchronizedList(new ArrayList<Work>());
    private LinkedList<Runnable> jobs=new LinkedList<Runnable>();

    public ThreadPoolExecutor_net(int size)
    {
        WORKSIZE=size;
        initWorkThread();
    }
    public void initWorkThread()
    {
        for(int i=0;i<WORKSIZE;i++)
        {
            Work work=new Work();
            works.add(work);
            new Thread(work"workThread-"+i).start();
        }
    }
    public void execute(Runnable job)
    {
        synchronized(jobs)
        {
            jobs.add(job);
            jobs.notify();
        }
    }
    //添加方法shutdown\awaitTermination\execute
    public void shutDown(){
    	
    }
    public void shutDownNow(){
    	
    }
    
    class Work implements Runnable{

        private volatile boolean isRun=true;
        @Override
        public void run() {
            while(isRun)
            {
                Runnable job=null;
                synchronized(jobs)
                {
                    while(jobs.isEmpty()){
                        try {
                            jobs.wait();
                        } catch (InterruptedException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                            return;
                        }
                    }
                    job=jobs.removeFirst();
                }
                if(job!=null)
                {
                   /* try {
                        Thread.sleep(10);
                    } catch (InterruptedException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }*/
                    job.run();
                }
            }
        }
        public void shutdown(){
            isRun=false;
        }
    }
    
   
}
