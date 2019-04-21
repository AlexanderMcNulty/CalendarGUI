
/**
 * MyCalendarTester - Project 1;
 * @author Alexander McNulty
 * @version 1.0 2/16/2019
 */

import java.io.FileNotFoundException;


/**
 * The starting point for the application.
 * This class contains the package's main(). 
 */
public class MyCalendarTester {
	
	/**
	 * 'main' runs calendar and creates output before closing.
	 * @param args - a standard parameter for a packages main function - No args are necessary
	 * @throws FileNotFoundException - if file events.txt is not found
	 */
	public static void main(String[] args) throws FileNotFoundException {
		System.out.println("My Calendar Application starting...");
		Menu menu = new Menu("events.txt");
		menu.run();
		menu.createOutputFile("output.txt");
		System.out.println("\n<terminated>");
		
				
	}
	
}
