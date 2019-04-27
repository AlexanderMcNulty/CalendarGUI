import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.TemporalAdjusters;
import java.util.Scanner;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;

import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.Border;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;

/**
 * Class 'Menu', allows user to interface with the 'EventList'.
 * Class can load and save events from/to a text file
 * Offers users the ability to view events, create events and delete events
 */
public class Calendar {
	
	private EventList events;
	private String choice = null;
	private String mainMenu = "\nSelect one of the following options:\n" + 
							  "[V]iew by  [C]reate, [G]o to [E]vent list [D]elete  [Q]uit\n";			
	private String viewBy = "\n[D]ay View or [M]onth view\n";
	private String seeMoreDays = "\n[P]revious or [N]ext or [G]o back to main menu ?\n";
	private String textToLoad = "";
	private LocalDate selectedDay;
	private ArrayList<Observer> observers;

	
	/**
	 * Constructs a 'Menu' object.
	 * @param fileName - file to load events from
	 * @throws FileNotFoundException if 'file' not found
	 */
	public Calendar(String fileName) throws FileNotFoundException  {
		initializeEventsList(fileName);
		selectedDay = LocalDate.now();
		observers = new ArrayList<Observer>();
	}
	

	public void initializeEventsList(String fileName) throws FileNotFoundException {
		//variables to constructing events from events.txt
		String[] times;
		String[] parts;
		String title;
		Event newEvent;
		//open file with scanner
		File file = new File(fileName);
		Scanner in = new Scanner(file);
		//EventList repeatEvents = new EventList();
		events = new EventList(); 
		//construct events from events.txt
		while(in.hasNext()) {
			title = in.nextLine();
			parts = in.nextLine().split("\\s+");
			boolean isRepeat = false;
			if(parts[0].matches("^[a-zA-Z]*$")) {
				isRepeat = true;					
			}
			if(isRepeat) {
				String days = parts[0];
				times = parts[1].split("[:]");
				LocalTime startTime = LocalTime.of(Integer.parseInt(times[0]),
									  			   Integer.parseInt(times[1]));
				times = parts[2].split("[:]");
				LocalTime endTime = LocalTime.of(Integer.parseInt(times[0]),
												 Integer.parseInt(times[1]));
				times = parts[3].split("[/]");
				LocalDate startDay = LocalDate.of(Integer.parseInt(times[2])+2000,
											 Integer.parseInt(times[0]),
											 Integer.parseInt(times[1]));
				times = parts[4].split("[/]");
				LocalDate endDay = LocalDate.of(Integer.parseInt(times[2])+2000,
											 Integer.parseInt(times[0]),
											 Integer.parseInt(times[1]));
				newEvent = new RepeatEvent(title, startTime, endTime, days, startDay, endDay);
				events.addEvent(newEvent);
			} else {	
				times = parts[1].split("[:]");
				LocalTime startTime = LocalTime.of(Integer.parseInt(times[0]),
									  			   Integer.parseInt(times[1]));
				times = parts[2].split("[:]");
				LocalTime endTime = LocalTime.of(Integer.parseInt(times[0]),
												 Integer.parseInt(times[1]));
				times = parts[0].split("[/]");
				LocalDate day = LocalDate.of(Integer.parseInt(times[2])+2000,
											 Integer.parseInt(times[0]),
											 Integer.parseInt(times[1]));
				newEvent = new Event(title, startTime, endTime, day);
				events.addEvent(newEvent);
			}
		}
		in.close();
	}
	

	/**
	 * 'createOutputFile' saves currently loaded events into selected file
	 * Function creates file if necessary.
	 * @param file - file to load events from
	 * @throws FileNotFoundException - if 'file' could not be created
	 */
	public void createOutputFile(String file) throws FileNotFoundException{
		PrintWriter writer = new PrintWriter(file);
		LocalDate startDate;
		LocalDate endDate;
		LocalTime startTime;
		LocalTime endTime;
		for(Event e : events.events) {
			//initialize property variables
			startDate = e.getStartDate();
			endDate = e.getEndDate();
			startTime = e.getStartTime();
			endTime = e.getEndTime();
			//write to file
			writer.println(e.getTitle());
			if(e.getType().equals("single")) {
				writer.print(startDate.getMonth().getValue() + "/" + startDate.getDayOfMonth()  + "/" + (startDate.getYear()-2000) + " ");
				if (startTime.getMinute() == 0) {
					writer.print(startTime.getHour() + ":0" + startTime.getMinute() + " ");
				} else if (startTime.getMinute() < 10) {
					writer.print(startTime.getHour() + ":0" + startTime.getMinute() + " ");
				} else {
					writer.print(startTime.getHour() + ":" + startTime.getMinute() + " ");
				}
				if (endTime.getMinute() == 0) {
					writer.print(endTime.getHour() + ":0" + endTime.getMinute() + "\n");
				} else if (endTime.getMinute() < 10) {
					writer.print(endTime.getHour() + ":0" + startTime.getMinute() + "\n");
			    } else {
					writer.print(endTime.getHour() + ":" + endTime.getMinute() + "\n");
				}
			} else { //is a repeat
				writer.print(e.getDays() + " ");
				if (startTime.getMinute() == 0) {
					writer.print(startTime.getHour() + ":0" + startTime.getMinute() + " ");
				} else if (startTime.getMinute() < 10) {
					writer.print(startTime.getHour() + ":0" + startTime.getMinute() + " ");
				} else {
					writer.print(startTime.getHour() + ":" + startTime.getMinute() + " ");
				}
				if (endTime.getMinute() == 0) {
					writer.print(endTime.getHour() + ":0" + endTime.getMinute() + " ");
				} else if (endTime.getMinute() < 10) {
					writer.print(endTime.getHour() + ":0" + startTime.getMinute() + " ");
			    } else {
					writer.print(endTime.getHour() + ":" + endTime.getMinute() + " ");
				}
				writer.print(startDate.getMonth().getValue() + "/" + startDate.getDayOfMonth()  + "/" + (startDate.getYear()-2000) + " ");
				writer.println(endDate.getMonth().getValue() + "/" + endDate.getDayOfMonth()  + "/" + (endDate.getYear()-2000));
			}
		}
		writer.close();
	}


	

	/**
	 * 'stringToDate' creates a 'LocalDate' from a string
	 * @param input - used to initialize LocalDate
	 * @return LocalDate - imported java.time object
	 */
	public LocalDate stringToDate(String input) {
		String[] times = input.split("[/]");
		LocalDate date = LocalDate.of(Integer.parseInt(times[2]),
									 Integer.parseInt(times[0]),
									 Integer.parseInt(times[1]));
		return date;
	}
	

	/**
	 * 'invalid' - tells user their input was invalid
	 * - takes no arguements
	 * - returns nothing
	 */
	public void invalid() { System.out.println("\ninvalid input"); }
	

	/**
	 * 'mainMenu' takes the 'next' user input, no prompt, no whitespace
	 * - takes no arguments
	 * @return String - used to determine if we should terminate the program.
	 
	case "V": 
		viewBy();
		return choice;
	case "C": 
		createEvent();
		return choice;
	case "G": 
		goTo();
		return choice;
	case "E": 
		eventList();
		return choice;
	case "D": 
		deleteEvent();
		return choice; 
	case "Q": 
		System.out.println("\n\nGoodbye :(\n");
		return choice;
	*/
	

	
	

	/**
	 * 'deleteEvents' initiates the user interface for specifying and deleting events
	 * - takes no arguments
	 * - returns nothing
	 */
	public void deleteEvent(String choice, String dateInput1, String title, String dateInput2, String toDelete) {
		System.out.println("Press [S] to Select a single event to delete, press [A] to delete all events on a certain day, enter [DR] to delete a regular event.");
		if(choice.equals("S")) {
			//choose a single non-repeating event to delete
			//select date
			System.out.print("Enter event date (MM/DD/YYYY): ");
			LocalDate date = stringToDate(dateInput1);
			//select title
			System.out.print("Enter event title: ");
			events.deleteSingleEvent(title, date);
		} else if(choice.equals("A")) {
			//choose a date to delete all non-repeating events
			//select a date
			System.out.print("Enter event date (MM/DD/YYYY): ");
			LocalDate date = stringToDate(dateInput2);
			events.deleteAllSingleEventsForASpecificDate(date);
		} else if(choice.equals("DR")) {
			//choose a repeating event to delete all repeating events
			//select a title
			System.out.print("Enter event title: ");
			events.deleteRepeatingEvents(toDelete);
		} else {
			invalid();
		}
		
	}
	

	/**
	 * 'eventList' prints a list of all event occurrences 
	 * - takes no arguments
	 * - returns nothing
	 */
	public void eventList() {
		int curYear = events.events.get(0).date.getYear();
		LocalDate oldestDate = events.events.get(events.events.size()-1).getEndDate();
		LocalDate currentDate = events.events.get(0).date;
		for(Event e : events.events) {
			if(e.getEndDate().isAfter(oldestDate)) {
				oldestDate = e.getEndDate();
			}
		}
		//System.out..println(old)
		System.out.println(curYear);
		while(currentDate.isBefore(oldestDate.plusDays(1))) {
			ArrayList<Event> daysEvents = events.getOrderedDays(currentDate);
			for(Event e : daysEvents) {
				if(curYear != e.date.getYear()) {
					curYear = e.date.getYear();
					System.out.println(curYear);
				}
				String name  = currentDate.getDayOfWeek().name(); 
				String dayOfWeek = name.substring(0,1).toUpperCase() + name.substring(1).toLowerCase();
				name  = currentDate.getMonth().name(); 
				String month = name.substring(0,1).toUpperCase() + name.substring(1).toLowerCase();
				System.out.println("   " + dayOfWeek 
						           + " " + month 
						           + " " + currentDate.getDayOfMonth() + " "
						           + e.getStartTime().getHour() + ":" + e.getStartTime().getMinute()
						           + " - " + e.getEndTime().getHour() + ":" + e.getEndTime().getMinute()
						           + " " + e.getTitle());
			}
			currentDate = currentDate.plusDays(1);
		}	
	}
	

	/**
	 * 'goTo' allows the user to enter a date for scheduled events
	 * - takes no arguments
	 * - returns nothing
	 */
	public void goTo(String input) {
		System.out.print("Enter a date to go to (MM/DD/YYYY) or [C] to cancel: ");
		System.out.println("");
		if(input.toUpperCase().equals("C")) {
			return;
		}
		LocalDate date = stringToDate(input);
		printDayEvents();
	}
	

	/**
	 * 'createEvent' prompts user for information to generate a 'SingleEvent'
	 * - takes no arguments
	 * - returns nothing
	 */
	public void createEvent(String title, String date, String startingTime, String endingTime) {
		boolean created = false;
		//menu for creating event
		System.out.println("\nTo create an event enter a title, date, and times.");
		while(created != true) {
			System.out.print("Enter a Title: ");
			System.out.print("Enter a Date (MM/DD/YYYY): ");
			System.out.print("Enter a Starting Time (Military Time - HH:MM): ");
			System.out.print("Enter an Ending Time (Military Time - HH:MM): ");
			created = events.checkEvents(title, date, startingTime, endingTime);
			if(created == false) {
				created = true;
			}
		}
		System.out.println("\nResults: Event was created.");
		stateChanged();
	}
	
	/**
	 * 'viewBy' allows views to browse available dates by day or by month
	 * - takes no arguments
	 * - returns nothing
	 */
	public void viewBy(String choiceA, String choiceB) {
		if(choiceA.equals("D")) {
			//print Day
			printDayEvents();
			seeMoreDays(choiceB);
		} else if(choiceA.equals("M")) {
			//print Month
			printMonth();
			seeMoreMonths(choiceB);
		} else {
			invalid();
		}
	}
	

	/**
	 * 'printMonth' print a calendar layout for planned event for an entire month
	 * @param selectedDay - a LocalDate object used to identify the month
	 * - returns nothing
	 */
	public void printMonth() {
		textToLoad = "";
		LocalDate tempDay = selectedDay.with(TemporalAdjusters.firstDayOfMonth());
		//create header
		System.out.println(" " + selectedDay.getMonth().toString() + ", "+ selectedDay.getYear() + " --\n");
		textToLoad += " " + selectedDay.getMonth().toString() + ", "+ selectedDay.getYear() + " --\n";
		System.out.println("Mo  Tu  We  Th  Fr  Sa  Su");
		textToLoad += "Mo  Tu  We  Th  Fr  Sa  Su\n";
		//prepare spacing at being of calendar
		String buffer = "";
		int startSize = tempDay.getDayOfWeek().getValue();
		for(int i = 1; i < startSize; i++) {
			buffer += "    ";
		}
		//print days in month
		int count = startSize;
		boolean hasEvent;
		String date;
		int lengthOfMonth = tempDay.lengthOfMonth();
		System.out.print(buffer);
		textToLoad += buffer;
		while(count != lengthOfMonth+startSize) {
			date = String.valueOf(tempDay.getDayOfMonth());
			hasEvent = checkDay(tempDay);
			if (hasEvent) {
				date = "[" + date + "]";
			}
			if(tempDay.getDayOfMonth() < 10) {
				System.out.print(date + "   ");
				textToLoad += date + "   ";
			} else {
				System.out.print(date + "  ");
				textToLoad += date + "  ";
			}
			if( (count % 7) == 0) {
				System.out.println("");
				textToLoad += "\n";
			}
			tempDay = tempDay.plusDays(1);
			count++;
		}
	}
	

	/**
	 * 'checkDay' is used to evaulate weather a day has planned events
	 * @param day - day to be checked for planned events
	 * @return boolean - shows if days has events
	 */
	public boolean checkDay(LocalDate day){
		if(events.getOrderedDays(day).size() > 0) {
			return true;
		} else {
			return false;
		}
		
	}
	

	/**
	 * 'seeMoreMonths' this function is used to iterate the months while in 'viewBy' function.
	 * @param selectedDay - a LocalDate object used to identify the month
	 * - returns nothing
	 */
	public void seeMoreMonths(String choiceA) {
		System.out.print("\n"+seeMoreDays);
		if(choiceA.equals("P")) {
			selectedDay = selectedDay.minusMonths(1);
			printMonth();
			this.stateChanged();
		} else if(choiceA.equals("N")) {
			selectedDay = selectedDay.plusMonths(1);
			printMonth();
			this.stateChanged();
		} else if(choiceA.equals("G")) {
			return;
		}  else {
			invalid();
		}
	}
	

	/**
	 * 'seeMoreDay'  this function is used to iterate the days while in 'viewBy' function.
	 * @param selectedDay - a LocalDate object used to identify the month
	 * - returns nothing
	 */
	public void seeMoreDays(String choice) {
		System.out.print(seeMoreDays);
		if(choice.equals("P")) {
			selectedDay = selectedDay.minusDays(1);
			printDayEvents();
			this.stateChanged();
		} else if(choice.equals("N")) {
			selectedDay = selectedDay.plusDays(1);
			printDayEvents();
			this.stateChanged();
		} else if(choice.equals("G")) {
			return;
		}  else {
			invalid();
		}
	}
	
	/**
	 * 'printDayEvents'  is used to print a list of a days scheduled events
	 * @param selectedDay - a LocalDate object used to identify the month
	 * - returns nothing
	 */
	public void printDayEvents() {
		textToLoad = "";
		ArrayList<Event> daysEvents = new ArrayList<Event>(events.getOrderedDays(selectedDay));
		textToLoad += selectedDay.getDayOfWeek() + ", " + 
				   selectedDay.getMonth() + " " + selectedDay.getDayOfMonth() + ", " + 
				   selectedDay.getYear();
		System.out.println(textToLoad);
		//System.out.println("\n" + selectedDay.format(DateTimeFormatter.ofPattern("MM/dd/yyyy")));
		if(daysEvents.size() == 0) {
			textToLoad += "\n\n - No Events Today\n";
			System.out.println(textToLoad);
		} else {
			textToLoad += "\n";
			for(Event e : daysEvents) {
				textToLoad += "\n - " + e.getTitle() + " : " + 
								   e.getStartTime() + " - "  + e.getEndTime();
				System.out.println(textToLoad);
			}
		}	
	}


	public EventList getEvents() {
		return events;
	}
	
	public String getText() {
		String toReturn = textToLoad;
		textToLoad = "";
		return toReturn;
	}

	public void attach(Observer observer) {
		observers.add(observer);
		stateChanged();
	}
	public void stateChanged() {
		for(Observer observer : observers) {
			observer.viewNotify();
		}
	}
	

	public LocalDate getSelectedDay() {
		return selectedDay;
	}
}
