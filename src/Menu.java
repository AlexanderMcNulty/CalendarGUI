import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.TemporalAdjusters;
import java.util.Scanner;

import javax.swing.BorderFactory;
import javax.swing.JFrame;

import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;

/**
 * Class 'Menu', allows user to interface with the 'EventList'.
 * Class can load and save events from/to a text file
 * Offers users the ability to view events, create events and delete events
 */
public class Menu {
	
	private EventList events;
	private String mainMenu = "\nSelect one of the following options:\n" + 
							  "[V]iew by  [C]reate, [G]o to [E]vent list [D]elete  [Q]uit\n";			
	private String viewBy = "\n[D]ay View or [M]onth view\n";
	private String seeMoreDays = "\n[P]revious or [N]ext or [G]o back to main menu ?\n";

	/**
	 * Constructs a 'Menu' object.
	 * @param fileName - file to load events from
	 * @throws FileNotFoundException if 'file' not found
	 */
	public Menu(String fileName) throws FileNotFoundException  {
		initializeEventsList(fileName); 
		

		JFrame frame = new JFrame();
		JLabel label = new JLabel();
			
		frame.setPreferredSize(new Dimension(290, 350));
		frame.getRootPane().setBorder(BorderFactory.createMatteBorder(3, 3, 3, 3, Color.lightGray));
		frame.getContentPane().setBackground(Color.WHITE);
		//frame.add(data, BorderLayout.SOUTH);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.pack();
		frame.setVisible(true);
		
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
		EventList events = new EventList(); 
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
	 * 'run' engages the user's terminal interface
	 * function terminates when quits from 'mainMenu()'
	 * - Takes no arguments.
	 * - Returns nothing
	 */
	public void run() {
		String choice = "";
		while(!(choice.equals("Q"))) {
			choice = mainMenu();
		}
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
	 * 'getInput' creates whitespace, prompts users, takes the 'next' user input
	 * - takes no arguments
	 * @return String - of users command, to first whitespace
	 */
	public String getInput() {
		Scanner reader = new Scanner(System.in);
		System.out.print("\nEnter Command: ");
		String toReturn = reader.next().toUpperCase();
		System.out.println("");
		return toReturn;
	}


	/**
	 * 'getSilentInput' takes the 'next' user input, no prompt, no whitespace
	 * - takes no arguments
	 * @return String- of users command, to first whitespace
	 */
	public String getSilentInput() {
		Scanner reader = new Scanner(System.in);
		String toReturn = reader.next();
		return toReturn;
	}
	

	/**
	 * 'getSilentInput' takes the 'next' user input,
	 * - takes no arguments
	 * @return String of users command, to first newline
	 */
	public String getSilentLineInput() {
		Scanner reader = new Scanner(System.in);
		String toReturn = reader.nextLine();
		return toReturn;
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
	 */
	public String mainMenu() {
		System.out.println(mainMenu);
		String choice = getInput();
		switch(choice) {
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
			default:
				System.out.println("invalid option");
				break;
		}
		return choice;
	}
	

	/**
	 * 'deleteEvents' initiates the user interface for specifying and deleting events
	 * - takes no arguments
	 * - returns nothing
	 */
	public void deleteEvent() {
		System.out.println("Press [S] to Select a single event to delete, press [A] to delete all events on a certain day, enter [DR] to delete a regular event.");
		String choice  = getInput();
		if(choice.equals("S")) {
			//choose a single non-repeating event to delete
			//select date
			System.out.print("Enter event date (MM/DD/YYYY): ");
			String input = getSilentInput();
			LocalDate date = stringToDate(input);
			//select title
			System.out.print("Enter event title: ");
			String title = getSilentLineInput().trim();
			events.deleteSingleEvent(title, date);
		} else if(choice.equals("A")) {
			//choose a date to delete all non-repeating events
			//select a date
			System.out.print("Enter event date (MM/DD/YYYY): ");
			String input = getSilentInput();
			LocalDate date = stringToDate(input);
			events.deleteAllSingleEventsForASpecificDate(date);
		} else if(choice.equals("DR")) {
			//choose a repeating event to delete all repeating events
			//select a title
			System.out.print("Enter event title: ");
			String input = getSilentLineInput();
			events.deleteRepeatingEvents(input);
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
	public void goTo() {
		System.out.print("Enter a date to go to (MM/DD/YYYY) or [C] to cancel: ");
		String input = getSilentInput();
		System.out.println("");
		if(input.toUpperCase().equals("C")) {
			return;
		}
		LocalDate date = stringToDate(input);
		printDayEvents(date);
	}
	

	/**
	 * 'createEvent' prompts user for information to generate a 'SingleEvent'
	 * - takes no arguments
	 * - returns nothing
	 */
	public void createEvent() {
		boolean created = false;
		//menu for creating event
		System.out.println("\nTo create an event enter a title, date, and times.");
		while(created != true) {
			System.out.print("Enter a Title: ");
			String title = getSilentLineInput();
			System.out.print("Enter a Date (MM/DD/YYYY): ");
			String date = getSilentInput();
			System.out.print("Enter a Starting Time (Military Time - HH:MM): ");
			String startingTime = getSilentInput();
			System.out.print("Enter an Ending Time (Military Time - HH:MM): ");
			String endingTime = getSilentInput();
			created = events.checkEvents(title, date, startingTime, endingTime);
			if(created == false) {
				System.out.println("Error: time conflict with existing event");
				System.out.println("\n --- please try again \n");
			}
		}
		System.out.println("\nResults: Event was created.");
	}
	

	/**
	 * 'viewBy' allows views to browse available dates by day or by month
	 * - takes no arguments
	 * - returns nothing
	 */
	public void viewBy() {
		LocalDate selectedDay = LocalDate.now();
		String choice= "";
		while(!(choice.equals("D")) && !(choice.equals("M"))) {
			System.out.print(viewBy);
			choice = getInput();
			//was it day view or month view?
			if(choice.equals("D")) {
				printDayEvents(selectedDay);
				//asked to see more
				seeMoreDays(selectedDay);
			} else if(choice.equals("M")) {
				//print month
				printMonth(selectedDay);
				seeMoreMonths(selectedDay);
			} else {
				invalid();
			}
		}
	}
	

	/**
	 * 'printMonth' print a calendar layout for planned event for an entire month
	 * @param selectedDay - a LocalDate object used to identify the month
	 * - returns nothing
	 */
	public void printMonth(LocalDate selectedDay) {
		LocalDate tempDay = selectedDay.with(TemporalAdjusters.firstDayOfMonth());
		//create header
		System.out.println(" " + selectedDay.getMonth().toString() + ", "+ selectedDay.getYear() + " --\n");
		System.out.println("Mo  Tu  We  Th  Fr  Sa  Su");
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
		while(count != lengthOfMonth+startSize) {
			date = String.valueOf(tempDay.getDayOfMonth());
			hasEvent = checkDay(tempDay);
			if (hasEvent) {
				date = "[" + date + "]";
			}
			if(tempDay.getDayOfMonth() < 10) {
				System.out.print(date + "   ");
			} else {
				System.out.print(date + "  ");
			}
			if( (count % 7) == 0) {
				System.out.println("");
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
	public void seeMoreMonths(LocalDate selectedDay) {
		String choice = "";
		while(true) {
			System.out.print("\n"+seeMoreDays);
			choice = getInput();
			if(choice.equals("P")) {
				selectedDay = selectedDay.minusMonths(1);
				printMonth(selectedDay);
			} else if(choice.equals("N")) {
				selectedDay = selectedDay.plusMonths(1);
				printMonth(selectedDay);
			} else if(choice.equals("G")) {
				return;
			}  else {
				invalid();
			}
		}
	}
	

	/**
	 * 'seeMoreDay'  this function is used to iterate the days while in 'viewBy' function.
	 * @param selectedDay - a LocalDate object used to identify the month
	 * - returns nothing
	 */
	public void seeMoreDays(LocalDate selectedDay) {
		String choice = "";
		while(true) {
			System.out.print(seeMoreDays);
			choice = getInput();
			if(choice.equals("P")) {
				selectedDay = selectedDay.minusDays(1);
				printDayEvents(selectedDay);
			} else if(choice.equals("N")) {
				selectedDay = selectedDay.plusDays(1);
				printDayEvents(selectedDay);
			} else if(choice.equals("G")) {
				return;
			}  else {
				invalid();
			}
		}
	}
	
	/**
	 * 'printDayEvents'  is used to print a list of a days scheduled events
	 * @param selectedDay - a LocalDate object used to identify the month
	 * - returns nothing
	 */
	public void printDayEvents(LocalDate selectedDay) {
		ArrayList<Event> daysEvents = new ArrayList<Event>(events.getOrderedDays(selectedDay));
		System.out.println(selectedDay.getDayOfWeek() + ", " + 
				   selectedDay.getMonth() + " " + selectedDay.getDayOfMonth() + ", " + 
				   selectedDay.getYear());
		//System.out.println("\n" + selectedDay.format(DateTimeFormatter.ofPattern("MM/dd/yyyy")));
		if(daysEvents.size() == 0) {
			System.out.println(" - No Events Today\n");
		} else {
			for(Event e : daysEvents) {
				System.out.println(" - " + e.getTitle() + " : " + 
								   e.getStartTime() + " - "  + e.getEndTime());
			}
		}	
	}
}
