package events;

public abstract class Event implements Comparable<Event> {
	
	public long time;

	public abstract void execute();
	
	public int compareTo(Event event) {
        if(time > event.time) return 1;
        else if(event.time > time) return -1;
        else return 0;
    }
}
