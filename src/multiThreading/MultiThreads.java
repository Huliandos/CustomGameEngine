package multiThreading;

import java.util.concurrent.atomic.AtomicInteger;

public class MultiThreads {
	public AtomicInteger numOfThreadsWaiting = new AtomicInteger();
	final Object TOKEN = new Object();
	int numberOfThreads = 5;
	
	public void start() {
		Thread[] threads = new Thread[numberOfThreads];
		
		for(int i=0; i<threads.length; i++) {
			threads[i] = new Thread(new MyRunnable(i, TOKEN, numOfThreadsWaiting, numberOfThreads));
			
			//threads[i].start();
		}
		
		for(int i=0; i<threads.length; i++) {
			threads[i].start();
		}
	}
}
