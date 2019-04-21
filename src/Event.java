import java.time.LocalTime;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;


/**
 * 'Event' class holds all the properties of a single event
 * implements the 'Comparable' interface in order to do comparisons on time.
 */
public class Event  implements Comparable<Event>{
	protected String title;
	protected LocalTime start;
	protected LocalTime end;
	protected LocalDate date;
	protected String type = "single";
	
	
	/**
	 * 'Event' constructor takes 4 arguments in order to generate an Event object
	 * @param title - a String which describes the event
	 * @param start - a LocalTime which denotes the being of the event
	 * @param end -  a LocalTime which denotes the end of the event
	 * @param date - a LocalDate which specifies the day the event is scheduled
	 * - returns nothing
	 */
	public Event(String title, LocalTime start, LocalTime end, LocalDate date) {
		this.title = title;
		this.start = start;
		this.end = end;
		this.date = date;
	}
	
	
	/**
	 * 'daysOfWeek' is a function to find the day of the week an event is scheduled for
	 * - takes no parameters
	 * @return ArrayList - returns an arrayList with an entry which specifies the day an event will be held
	 */
	public ArrayList<DayOfWeek> daysOfWeek() {
		ArrayList<DayOfWeek> daysOfWeek = new ArrayList<DayOfWeek>();
		daysOfWeek.add(date.getDayOfWeek());
		return daysOfWeek;
	}
	
	
	/**
	 * 'getDays' is defined here in order to allow of polymorphic use of the EventList
	 * - takes no parameters
	 * @return String - returns an empty string
	 */
	public String getDays() {
		return "";
	}
	
	/**
	 * 'getType' a getter function for retrieving event type
	 * - takes no parameters
	 * @return 'String' - returns the value of the type property
	 */
	public String getType() {
		return type;
	}


	/**
	 * 'getStartDate' is defined here in order to allow of polymorphic use of the EventList
	 * - takes no parameters
	 * @return LocalDate - object return denotes the start date, which is the only date property of a 'single event'
	 */
	public LocalDate getStartDate() {
		return date;
	}
	
	/**
	 * 'getEndDate' is defined here in order to allow of polymorphic use of the EventList
	 * - takes no parameters
	 * @return LocalDate - object return denotes the end date, which is the only date property of a 'single event'
	 */
	public LocalDate getEndDate() {
		return date;
	}
	

	/**
	 * 'getTitle' returns the title of the event
	 * - takes no parameters
	 * @return String - string returned contains the event's title
	 */
	public String getTitle() {
		return title;
	}


	/**
	 * 'getStartTime' function returns the event's start time
	 * - takes no parameters
	 * @return LocalTime - the LocalTime object denoting the time of the start of the event
	 */
	public LocalTime getStartTime() {
		return start;
	}
	
	/**
	 * 'getEndTime' function returns the event's end time
	 * - takes no parameters
	 * @return LocalTime - the LocalTime object denoting the time of the end of the event
	 */
	public LocalTime getEndTime() {
		return end;
	}
	
	/**
	 * 'toString' function returns a string which contains all the events properties
	 * - takes no parameters
	 * @return String - the String returned contains all of the events properties
	 */
	public String toString() {
		return "{title: " + title + ", date: " + date + ", starts: " + start + ", ends: " + end + "}";
	}


	/**
	 * 'compareTo' must be created in order to use the comparable interface
	 * @param event - the Event object being compared in order to see which event occurs first in a day 
	 * @return int - the int returned is -1 if the current event occurs first, 0 if the event starts at the same time, 1 'this' event occurs later
	 */
	@Override
	public int compareTo(Event event) {
		// TODO Auto-generated method stub
		if(this.start == event.start) {
			return 0;
		} else if(this.start.isAfter(event.start)) {
			return 1;
		} else {
			return -1;
		}
	}
}
