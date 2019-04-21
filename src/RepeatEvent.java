import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;


/**
 * 'RepeatEvent' is a class which extends the Events
 * This class in order to modify functions for the properties of a repeating event
 */
class RepeatEvent extends Event {
		private String[] days;
		private LocalDate startDate;
		private LocalDate endDate;
		private String type = "repeat";
		

		/**
		 * 'RepeatEvent' this constructor prepares and saves input for a Repeating Event
		 * - takes no parameters
		 * @return LocalTime - the LocalTime object denoting the start of the event
		 */
		public RepeatEvent(String title, LocalTime start, LocalTime end, String repeatDays, LocalDate startDay, LocalDate endDay) {
			super(title, start, end, startDay);
			days = repeatDays.split("");			
			this.startDate = startDay;
			this.endDate = endDay;
		}
		

		/**
		 * 'getDays' function returns a String containing all the days a event is being held
		 * - takes no parameters
		 * @return String - a String containning no more than 7 letters one for each day M (Monday), T (Tuesday), W (wednesday), R (Thursday), F (Friday), S (Saturday), N (Sunday) 
		 */
		public String getDays() {
			String toReturn = "";
			for(String s : days) {
				toReturn += s;
			}
			return toReturn;
		}
		
		/**
		 * 'getType' returns the type of event, which would be 'repeating'
		 * - takes no parameters
		 * @return String - returns a string of value 'repeating'
		 */
		public String getType() {
			return type;
		}


		/**
		 * 'getStartDate' returns the starting date to which this repeating event will start appearing on the schedule
		 * - takes no parameters
		 * @return LocalDate - a local date which denotes the start of a period to which this repeating event will appear on the calendar
		 */
		public LocalDate getStartDate() {
			return startDate;
		}
		
		/**
		 * 'getEndDate' returns the ending date to which this repeating event will end appearing on the schedule
		 * - takes no parameters
		 * @return LocalDate - a local date which denotes the ending of a period to which this repeating event will appear on the calendar
		 */
		public LocalDate getEndDate() {
			return endDate;
		}

		/**
		 * 'daysOfWeek' a function used to find the days of the week an event is scheduled for
		 * - takes no parameters
		 * @return ArrayList - returns an arrayList which specifies the days of the week an event will be held
		 */
		public ArrayList<DayOfWeek> daysOfWeek() {
			ArrayList<DayOfWeek> daysOfWeek = new ArrayList<DayOfWeek>();
			for(String d : days) {
				switch(d) {
					case "M":
						daysOfWeek.add(DayOfWeek.MONDAY);
						break;
					case "T":
						daysOfWeek.add(DayOfWeek.TUESDAY);
						break;
					case "W":
						daysOfWeek.add(DayOfWeek.WEDNESDAY);
						break;
					case "R":
						daysOfWeek.add(DayOfWeek.THURSDAY);
						break;
					case "F":
						daysOfWeek.add(DayOfWeek.FRIDAY);
						break;
					case "S":
						daysOfWeek.add(DayOfWeek.SATURDAY);
						break;
					case "N":
						daysOfWeek.add(DayOfWeek.SUNDAY);
						break;
					default:
						break;
				}
			}
			return daysOfWeek;
		}
		
		/**
		 * 'toString' returns a string which contains all of the properties of a repeating event
		 * - takes no parameters
		 * @return String - a String who value can hold all of the repeating events properties
		 */
		public String toString() {
			return "{title: " + title + ", duration: " + startDate + " "+ endDate + ", starts: " + start + ", ends: " + end + "}";
		}
			
	}
	