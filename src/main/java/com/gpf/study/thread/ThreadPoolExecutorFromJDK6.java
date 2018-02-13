package com.gpf.study.thread;


import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.RunnableFuture;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.ThreadPoolExecutor.AbortPolicy;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class ThreadPoolExecutorFromJDK6 implements ExecutorService {
	
	volatile int runState;
	static final int RUNNING = 0;
	static final int SHUTDOWN = 1;
	static final int STOP = 2;
	static final int TERMINATED = 3;
	private final BlockingQueue<Runnable> workQueue = new LinkedBlockingQueue<Runnable>();
	/**
	 * Permission for checking shutdown
	 */
	private static final RuntimePermission shutdownPerm = new RuntimePermission("modifyThread");
	/**
	 * Lock held on updates to poolSize, corePoolSize, maximumPoolSize,
	 * runState, and workers set.
	 */
	private final ReentrantLock mainLock = new ReentrantLock();
	/**
	 * Wait condition to support awaitTermination
	 */
	private final Condition termination = mainLock.newCondition();
	/**
	 * Set containing all worker threads in pool. Accessed only when holding
	 * mainLock.
	 */
	private final HashSet<Worker> workers = new HashSet<Worker>();

	/**
	 * Core pool size, updated only while holding mainLock, but volatile to
	 * allow concurrent readability even during updates.
	 */
	private volatile int corePoolSize;
	/**
	 * Current pool size, updated only while holding mainLock but volatile to
	 * allow concurrent readability even during updates.
	 */
	private final AtomicInteger poolSize = new AtomicInteger(0);

	public int getPoolSize() {
		return poolSize.get();
	}

	/**
	 * Handler called when saturated or shutdown in execute.
	 */
	private volatile RejectedExecutionHandler handler = new AbortPolicy();
	/**
	 * Counter for completed tasks. Updated only on termination of worker
	 * threads.
	 */
	private AtomicLong completedTaskCount = new AtomicLong(0);

	public long getCompletedTaskCount() {
		return completedTaskCount.get();
	}

	public ThreadPoolExecutorFromJDK6(int corePoolSize) {
		if (corePoolSize < 0)
			throw new IllegalArgumentException();
		this.corePoolSize = corePoolSize;
		initMyFixedThreadPool();
	}

	void initMyFixedThreadPool() {

		for (int i = 0; i < corePoolSize; i++) {
			final ReentrantLock mainLock = this.mainLock;
			mainLock.lock();
			try {
				if (runState == RUNNING) {
					Runnable firstTask = new Runnable() {
						@Override
						public void run() {
							// TODO Auto-generated method stub
							System.out.println("初始化任务执行run，打印线程名称" + Thread.currentThread().getName());
						}
					};
					Worker w = new Worker(firstTask);
					w.thread = new Thread(w);
					workers.add(w);
					System.out.println("初始化任务中，work添加到works");
					w.thread.start();
					poolSize.getAndIncrement();
				}
			} finally {
				mainLock.unlock();
			}
		}
	};

	@Override
	public void execute(Runnable command) {
		// TODO Auto-generated method stub
		/*
		 * if (command == null) throw new NullPointerException();
		 */

		System.out.println("进入execute");

		// 如果poolSize>= corePoolSize 或者 执行addIfUnderCorePoolSize() 失败
		// 如果poolSize>= corePoolSize
		// if (!addIfUnderCorePoolSize(command)) {
		// 放到阻塞队列之后，就不管了吗?????
		if (runState == RUNNING) {
			if (command != null) {
				if (workQueue.offer(command)) {
					System.out.println("放入队列成功,队列此时长度：" + workQueue.size());
					if (runState != RUNNING || poolSize.get() == 0) {
						// ensureQueuedTaskHandled(command);//?????????????????????????
					}
				}
			}
		} else if (runState != RUNNING && command != null) {

			System.out.println("不再接受新任务!");
			// 测试
			for (Worker worker : workers) {
				System.out.println("线程名称：" + worker.thread.getName() + "	状态:" + worker.thread.getState());

			}
			throw new RejectedExecutionException();
		}

	}

	// worker thread调用runLock方法，防止线程在执行任务时，被interrupt
	private final class Worker implements Runnable {
		/**
		 * The runLock is acquired and released surrounding each task execution.
		 * It mainly protects against interrupts that are intended to cancel the
		 * worker thread from instead interrupting the task being run.
		 */
		private final ReentrantLock runLock = new ReentrantLock();

		/**
		 * Initial task to run before entering run loop. Possibly null.
		 */
		private Runnable firstTask;

		/**
		 * Per thread completed task counter; accumulated into
		 * completedTaskCount upon termination.
		 */
		final AtomicInteger completedTasks = new AtomicInteger(0);;

		/**
		 * Thread this worker is running in. Acts as a final field, but cannot
		 * be set until thread is created.
		 */
		Thread thread;

		Worker(Runnable firstTask) {
			this.firstTask = firstTask;
		}

		boolean isActive() {
			return runLock.isLocked();
		}

		/**
		 * Interrupts thread if not running a task.
		 */
		void interruptIfIdle() {
			final ReentrantLock runLock = this.runLock;
			if (runLock.tryLock()) {
				try {
					if (thread != Thread.currentThread())
						thread.interrupt();
				} finally {
					runLock.unlock();
				}
			}
		}

		/**
		 * Interrupts thread even if running a task.
		 */
		void interruptNow() {
			thread.interrupt();
		}

		/**
		 * Main run loop
		 */
		public void run() {
			try {
				System.out.println(Thread.currentThread() + "进入worker run()");
				Runnable task = firstTask;
				firstTask = null;

				while (task != null || (task = getTask()) != null) {

					final ReentrantLock runLock = this.runLock;
					runLock.lock();
					try {
						/*
						 * Ensure that unless pool is stopping, this thread does
						 * not have its interrupt set. This requires a
						 * double-check of state in case the interrupt was
						 * cleared concurrently with a shutdownNow -- if so, the
						 * interrupt is re-enabled.
						 */
						if (runState < STOP && Thread.interrupted() && runState >= STOP)
							thread.interrupt();
						/*
						 * Track execution state to ensure that afterExecute is
						 * called only if task completed or threw exception.
						 * Otherwise, the caught runtime exception will have
						 * been thrown by afterExecute itself, in which case we
						 * don't want to call it again.
						 */
						try {
							task.run();
							completedTasks.getAndIncrement();
							completedTaskCount.getAndIncrement();
						} catch (RuntimeException ex) {
							throw ex;
						}
					} finally {
						runLock.unlock();
					}
					task = null;
				}
			} finally {
				// workerDone(this);//??????????
			}
		}
	}

	Runnable getTask() {
		for (;;) {
			try {
				int state = runState;
				if (state > SHUTDOWN)
					return null;
				Runnable r;
				if (state == SHUTDOWN)
					r = workQueue.poll();
				else
					// take的话，拿不到会阻塞
					r = workQueue.take();
				if (r != null)
					return r;
			} catch (InterruptedException ie) {
				// On interruption, re-check runState
			}
		}
	}

	void interruptIdleWorkers() {
		final ReentrantLock mainLock = this.mainLock;
		mainLock.lock();
		try {
			for (Worker w : workers)
				w.interruptIfIdle();
		} finally {
			mainLock.unlock();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.concurrent.ExecutorService#shutdown()
	 */
	@Override
	public void shutdown() {
		// TODO Auto-generated method stub
		SecurityManager security = System.getSecurityManager();
		if (security != null)
			security.checkPermission(shutdownPerm);
		final ReentrantLock mainLock = this.mainLock;
		mainLock.lock();
		try {
			if (security != null) { // Check if caller can modify our threads
				for (Worker w : workers)
					security.checkAccess(w.thread);
			}
			runState = SHUTDOWN;
			try {
				for (Worker w : workers) {
					//经测试，如果阻塞队列为空，则线程的状态是：WAITING
					System.out.println("当前线程的状态是：****************************"+w.thread.getState().toString());
					if (w.thread.getState().toString() == "BLOCKED") {
						w.thread.interrupt();
						System.out.println(
								Thread.currentThread().getName() + "已经执行interrupt()方法，即将抛异常*************************");
						System.out.println(
								Thread.currentThread().getName() + "已经执行interrupt()方法，即将抛异常*************************");

						throw new InterruptedException();

					}

				}
			} catch (SecurityException se) { // Try to back out
				// tryTerminate() here would be a no-op
				throw se;
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			// tryTerminate(); // Terminate now if pool and queue empty
		} finally {
			mainLock.unlock();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.concurrent.ExecutorService#shutdownNow()
	 */
	@Override
	public List<Runnable> shutdownNow() {
		// TODO Auto-generated method stub
		/*SecurityManager security = System.getSecurityManager();
		if (security != null)
	            security.checkPermission(shutdownPerm);

	        final ReentrantLock mainLock = this.mainLock;
	        mainLock.lock();
	        try {
	            if (security != null) { // Check if caller can modify our threads
	                for (Worker w : workers)
	                    security.checkAccess(w.thread);
	            }
	            runState = STOP;

	            try {
	                for (Worker w : workers) {
	                   w.thread.interrupt();
	                }
	            } catch (SecurityException se) { // Try to back out
	                runState = state;
	                // tryTerminate() here would be a no-op
	                throw se;
	            }

	            List<Runnable> tasks = drainQueue();
	            tryTerminate(); // Terminate now if pool and queue empty
	            return tasks;
	        } finally {
	            mainLock.unlock();
	        }*/
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.concurrent.ExecutorService#isShutdown()
	 */
	@Override
	public boolean isShutdown() {
		// TODO Auto-generated method stub
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.concurrent.ExecutorService#isTerminated()
	 */
	@Override
	public boolean isTerminated() {
		// TODO Auto-generated method stub
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.concurrent.ExecutorService#awaitTermination(long,
	 * java.util.concurrent.TimeUnit)
	 */
	@Override
	public boolean awaitTermination(long timeout, TimeUnit unit) throws InterruptedException {
		// TODO Auto-generated method stub
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.concurrent.ExecutorService#submit(java.util.concurrent.
	 * Callable)
	 */
	@Override
	public <T> Future<T> submit(Callable<T> task) {
		// TODO Auto-generated method stub
        if (task == null) throw new NullPointerException();
        RunnableFuture<T> ftask =new FutureTask<T>(task);
        execute(ftask);
        return ftask;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.concurrent.ExecutorService#submit(java.lang.Runnable)
	 */
	@Override
	public Future<?> submit(Runnable task) {
		// TODO Auto-generated method stub
        if (task == null) throw new NullPointerException();
        RunnableFuture<Object> ftask =new FutureTask<Object>(task, null);
        execute(ftask);
        return ftask;
	}
/*    protected <T> RunnableFuture<T> newTaskFor(Runnable runnable, T value) {
        return new FutureTask<T>(runnable, value);
        
	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.concurrent.ExecutorService#submit(java.lang.Runnable,
	 * java.lang.Object)
	 */
	@Override
	public <T> Future<T> submit(Runnable task, T result) {
		// TODO Auto-generated method stub
        if (task == null) throw new NullPointerException();
        RunnableFuture<T> ftask = new FutureTask<T>(task, result);
        execute(ftask);
        return ftask;
	}


	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.concurrent.ExecutorService#invokeAll(java.util.Collection)
	 */
	@Override
	public <T> List<Future<T>> invokeAll(Collection<? extends Callable<T>> tasks) throws InterruptedException {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.concurrent.ExecutorService#invokeAll(java.util.Collection,
	 * long, java.util.concurrent.TimeUnit)
	 */
	@Override
	public <T> List<Future<T>> invokeAll(Collection<? extends Callable<T>> tasks, long timeout, TimeUnit unit)
			throws InterruptedException {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.concurrent.ExecutorService#invokeAny(java.util.Collection)
	 */
	@Override
	public <T> T invokeAny(Collection<? extends Callable<T>> tasks) throws InterruptedException, ExecutionException {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.concurrent.ExecutorService#invokeAny(java.util.Collection,
	 * long, java.util.concurrent.TimeUnit)
	 */
	@Override
	public <T> T invokeAny(Collection<? extends Callable<T>> tasks, long timeout, TimeUnit unit)
			throws InterruptedException, ExecutionException, TimeoutException {
		// TODO Auto-generated method stub
		return null;
	}

}

