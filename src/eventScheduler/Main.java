package eventScheduler;

import java.time.*;
import java.sql.*;
import java.time.format.*;
import java.util.InputMismatchException;
import java.util.Locale;
import java.util.Properties;
import java.util.Scanner;

public class Main {

	private static java.sql.Connection con;

	public static void main(String[] args) {

		try {
			Properties props = new Properties();
			props.setProperty("sslmode", "require");
			props.setProperty("user", "root");
			props.setProperty("password", "v2_3wFD6_gDuGkwzwqcRNAD289p77pKf");
			con = DriverManager.getConnection("jdbc:postgresql://db.bit.io/ShiraNeumann.Calendar", props);

			Scanner keyboard = new Scanner(System.in);
			UserManager users = new UserManager(con);
			int id = userSignIn(keyboard, users);
			Calendar calendar = new Calendar(con, id);

			int choice = 0;

			while (choice != 7) {

				choice = menu(keyboard);

				switch (choice) {
				case 1:
					getEventInfo(keyboard, calendar);
					break;
				case 2:
					removeEvent(keyboard, calendar);
					break;
				case 3:
					modifyEvent();
					break;

				case 4:
					displayEventTitles(calendar);
					displayEventDetails(calendar, keyboard);
					break;
					
				case 5:
					displayAllEvents(calendar);
					break;
					
				case 6:
					id = userSignIn(keyboard, users);
					calendar.setId(id);
					break;
				}

			}
			System.exit(0);
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

	public static int userSignIn(Scanner keyboard, UserManager um) {
		int choice = 2;
		int success = 0;
		while(success == 0) {
			System.out.println("Options: ");
			System.out.println("1. Sign in");
			System.out.println("2. Create new user");
			boolean tryAgain = true;
			while(tryAgain) {
				try {
					choice = keyboard.nextInt();
					keyboard.nextLine();
					tryAgain = false;
					if(choice != 1 && choice != 2) {
						tryAgain = true;
						System.out.println("Please enter a valid choice: ");
					}
				} catch (InputMismatchException e) {
					keyboard.nextLine();
					tryAgain = true;
					System.out.println("Input mismatch! Enter a new number: ");
				}
			}

			success = signIn(choice, keyboard, um);
		}
		
		return success;
	}
	
	public static int signIn(int choice, Scanner keyboard, UserManager um) {
		System.out.print("Enter your first name: ");
		String f = keyboard.nextLine();
		System.out.print("Enter your last name: ");
		String l = keyboard.nextLine();
		System.out.print("Enter your password: ");
		String p = keyboard.nextLine();
		int id=0;
		
		switch(choice) {
		case 2:
			if(!um.addUser(f, l, p)) {
				System.out.println("User already exists.\n");
				return 0;
			}
			System.out.println("\nUser added successfully.");
		case 1:
			if((id = um.signIn(f, l, p)) == 0) {
				System.out.println("Invalid Credentials!\n");
				return 0;
			}
			System.out.println("Welcome, " + f + "\n");
		}
		
		return id;
		
	}

	public static int menu(Scanner keyboard) {
		int choice = 0;

		System.out.println("Options: ");
		System.out.println("1. Add event");
		System.out.println("2. Remove event");
		System.out.println("3. Modify event");
		System.out.println("4. View an event");
		System.out.println("5. Display all events");
		System.out.println("6. Switch user");
		System.out.println("7. Exit");

		System.out.print("What would you like to do? ");

		try {
			choice = keyboard.nextInt();
		} catch (InputMismatchException e) {
			keyboard.nextLine();
			System.out.println("Input mismatch! Enter a new number: ");
		}
		return choice;
	}

	public static void getEventInfo(Scanner keyboard, Calendar calendar) {

		// getting userInput to create new event = diff fields
		System.out.println("\nPlease enter the address: ");
		System.out.print("Street number: ");
		int num = keyboard.nextInt();
		keyboard.nextLine();
		System.out.print("Street name: ");
		String streetName = keyboard.nextLine();
		System.out.print("City: ");
		String city = keyboard.nextLine();
		System.out.print("State: ");
		String state = keyboard.nextLine();
		System.out.print("Zipcode: ");
		String zip = keyboard.nextLine();

		Address addr = new Address(num, streetName, city, state, zip);

		System.out.println("\nPlease enter your event details: ");
		
		LocalDate date = getDate(keyboard);

		System.out.print("Event name: ");
		
		String eventName = keyboard.nextLine();
		
		LocalTime time = getTime(keyboard);
		
		int min = getDuration(keyboard);

		System.out.println("Add any additional notes about your event: ");
		String notes = keyboard.nextLine();

		Appointment app = new Appointment(date, eventName, time, notes, min, addr);
		System.out.println("\nYou entered:\n" + app + "\n");
		
		addEvent(app, calendar, keyboard);
	}
	
	public static LocalDate getDate(Scanner keyboard) {
		System.out.print("Event date (ex: January 21, 2005): ");

		boolean tryAgain;
		String dateString = null;
		LocalDate date = null;

		do {
			tryAgain = false;
			dateString = keyboard.nextLine();
			try {
				date = LocalDate.parse(dateString, DateTimeFormatter.ofPattern("MMMM dd, yyyy", Locale.ENGLISH));
			} catch (DateTimeParseException e) {
				tryAgain = true;
				System.out.print("Please use the proper format: ");
			}
		} while (tryAgain);
		
		return date;
	}
	
	public static LocalTime getTime(Scanner keyboard) {
		System.out.print("Event time (ex: 4:45 pm) : ");

		boolean tryAgain;
		LocalTime time = null;

		do {
			tryAgain = false;
			String timeString = keyboard.nextLine();
			timeString = timeString.toUpperCase();
			if(timeString.charAt(1) == ':') {
				timeString = "0" + timeString;
			}
			if(timeString.length() < 8) {
				timeString += "M";
			}
			try {
				time = LocalTime.parse(timeString, DateTimeFormatter.ofPattern("hh:mm a", Locale.ENGLISH));
			} catch (DateTimeParseException e) {
				tryAgain = true;
				System.out.print("Please use the proper format: ");
			}
		} while (tryAgain);
		
		return time;
	}
	
	public static int getDuration(Scanner keyboard) {
		boolean tryAgain;
		int[] nums = null;
		int l = 0;
		System.out.print("How long will the event be (h:m or m)? ");
		do {
			tryAgain = false;
			String min = keyboard.nextLine();
			String[] hm = min.split(":");
			try {
				if((l = hm.length) > 2 || l < 1) {
					throw new IllegalArgumentException();
				}
				nums = new int[l];
				for(int i=0; i<l; i++) {
					nums[i] = Integer.parseInt(hm[i]);
				}
			} catch(IllegalArgumentException e) {
				tryAgain = true;
				System.out.println("Please enter a valid value: ");
			}
		}while(tryAgain);
		
		int min = l==2 ? nums[0]*60+nums[1] : nums[0];
		
		return min;
		
	}
	
	public static void addEvent(Appointment app, Calendar calendar, Scanner keyboard) {
		int overlap = calendar.overlapping(app);
		if (overlap > 0) {
			System.out.println("The following event is already scheduled for this time:");
			calendar.displayEvent(overlap);
			System.out.print("\nDo you wish to proceed? Yes or no: ");

			if (keyboard.nextLine().toLowerCase().equals("yes")) {

				calendar.addEvent(app);
				System.out.println("\nEvent added\n");
			}
			else {
				System.out.println("Action cancelled\n");
			}

		} else {
			calendar.addEvent(app);
			System.out.println("\nEvent added\n");
		}
	}

	public static void removeEvent(Scanner keyboard, Calendar calendar) {
		displayEventTitles(calendar);
		System.out.println("Which event would you like to remove?");
		int id = keyboard.nextInt();
		keyboard.nextLine();

		if(calendar.displayEvent(id)) {
			System.out.println("Are you sure want to delete this event? ");
			if(keyboard.nextLine().toLowerCase().equals("yes")) {
				calendar.deleteEvent(id);
				System.out.println("Event deleted.");
			} else {
				System.out.println("Action cancelled");
			}
		} else {
			System.out.println("Invalid event id.");
		}
	}

	public static void modifyEvent() {
		// TODO Auto-generated method stub

		// display arrayList of events with numbers, get index from user and give them
		// options of what to change
		//

	}
	
	public static void displayAllEvents(Calendar calendar) {
		calendar.displayEvents();
	}

	public static void displayEventTitles(Calendar calendar) {
		calendar.displayShortEvents();
	}
	
	public static void displayEventDetails(Calendar calendar, Scanner keyboard) {
		System.out.print("Enter the id of an event to view more details: ");
		int id = keyboard.nextInt();
		keyboard.nextLine();
		calendar.displayEvent(id);
	}

}
