package _MultiThreadingTest;

import java.util.concurrent.atomic.AtomicInteger;

public class MyRunnable implements Runnable {
	int threadNumber;
	Object TOKEN;
	AtomicInteger numOfThreadsWaiting;
	int numberOfThreads;
	
	public MyRunnable(int threadNumber, Object TOKEN, AtomicInteger numOfThreadsWaiting, int numberOfThreads) {
		this.threadNumber = threadNumber;
		this.TOKEN = TOKEN;
		this.numOfThreadsWaiting = numOfThreadsWaiting;
		this.numberOfThreads = numberOfThreads;
	} 
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		for(int i=1; i<100; i++) {
			if(i%20 == 0) {
				waitForOtherThreads();
			}
			
			System.out.println(threadNumber + ": " + (i+1));
		}
	}

	void waitForOtherThreads() {
		//count number of waiting threads
		synchronized(TOKEN) {
			numOfThreadsWaiting.getAndIncrement();
			if(numOfThreadsWaiting.get() == numberOfThreads) {
				numOfThreadsWaiting.set(0);
				TOKEN.notifyAll();
			}
			else {
				try {
					TOKEN.wait();
				} catch (InterruptedException e) {
					// Ist völlig ungefährlich
					e.printStackTrace();
				}
			}
		}
	}
}
