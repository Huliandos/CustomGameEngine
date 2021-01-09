package events;

import java.util.PriorityQueue;

public class EventThread implements Runnable{

	PriorityQueue<Event> queue = new PriorityQueue<>();
	
	public void add(Event event) {
		synchronized (queue) {
			queue.offer(event);
			//queue.add(event);
			queue.notify();
		}
	}
	
	@Override
	public void run() {
		synchronized(queue){
			while(true) {
				long timeout = queue.peek().time - System.currentTimeMillis();
				/*
				System.out.println(queue.peek() + " " + timeout);
				System.out.println((((Event)queue.toArray()[0]).time - System.currentTimeMillis()) + " " + 
						(((Event)queue.toArray()[1]).time - System.currentTimeMillis()) + " " + 
						(((Event)queue.toArray()[2]).time - System.currentTimeMillis()));
				*/
				if(timeout<= 0)	{
					Event event = queue.poll();
					event.execute();
					queue.add(event);
					//System.out.println("Entered if " + timeout);
				}
				else {
					try {
						synchronized (queue) {
							//System.out.println("Entered else " + timeout);
							queue.wait(timeout);
						}
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}
	}
	
}
