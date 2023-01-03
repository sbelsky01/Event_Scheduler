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
			int id = signInMenu(keyboard, users);
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
					modifyEvent(keyboard, calendar);
					break;

				case 4:
					displayEventTitles(calendar);
					displayEventDetails(calendar, keyboard);
					break;

				case 5:
					displayAllEvents(calendar, keyboard);
					break;

				case 6:
					id = signInMenu(keyboard, users);
					calendar.setId(id);
					break;
				}

			}
			System.exit(0);
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

	public static int signInMenu(Scanner keyboard, UserManager um) {
		int choice = 2;
		int success = 0;
		while (success == 0) {
			System.out.println("Options: ");
			System.out.println("1. Sign in");
			System.out.println("2. Create new user");
			boolean tryAgain = true;
			while (tryAgain) {
				try {
					choice = keyboard.nextInt();
					keyboard.nextLine();
					tryAgain = false;
					if (choice != 1 && choice != 2) {
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

	public static int signIn(int choice, Scanner keyboard, UserManager userManager) {
		System.out.print("Enter your first name: ");
		String first = keyboard.nextLine();
		System.out.print("Enter your last name: ");
		String last = keyboard.nextLine();
		System.out.print("Enter your password: ");
		String pwd = keyboard.nextLine();
		int id = 0;

		switch (choice) {
		case 2:
			System.out.println("Enter your email: ");
			String email = keyboard.nextLine();
			boolean userAdded = userManager.addUser(first, last, pwd, email);
			if (!userAdded) {
				System.out.println("User already exists.\n");
				return 0;
			} else {
				System.out.println("\nUser added successfully.");
			}
		case 1:
			id = userManager.signIn(first, last, pwd);
			if (id == 0) {
				System.out.println("Invalid Credentials!\n");
				return 0;
			}
		}
		System.out.println("Welcome, " + first + "\n");

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

	private static Address receiveAddress(Scanner keyboard) {
		boolean tryAgain = true;
		int num = 0;
		String streetName = null;
		String city = null;
		String state = null;
		String zip = null;
		System.out.println("\nPlease enter the address: ");
		System.out.print("Street number: ");

		while (tryAgain) {
			try {
				num = keyboard.nextInt();
				keyboard.nextLine();
				tryAgain = false;
			} catch (InputMismatchException e) {
				System.out.print("Please enter a street number: ");
				keyboard.nextLine();
			}
		}

		System.out.print("Street name: ");
		streetName = keyboard.nextLine();

		System.out.print("City: ");
		tryAgain = true;

		while (tryAgain) {
			try {
				city = keyboard.nextLine();
				tryAgain = false;
			} catch (InputMismatchException e) {
				System.out.print("Please enter a city: ");
				keyboard.nextLine();
			}
		}

		System.out.print("State: ");
		tryAgain = true;

		while (tryAgain) {
			try {
				state = keyboard.nextLine();
				tryAgain = false;
			} catch (InputMismatchException e) {
				System.out.print("Please enter a state: ");
				keyboard.nextLine();
			}
		}

		System.out.print("Zipcode: ");
		tryAgain = true;

		while (tryAgain) {
			try {
				zip = keyboard.nextLine();
				tryAgain = false;
			} catch (InputMismatchException e) {
				System.out.print("Please enter a zipcode: ");
				keyboard.nextLine();
			}
		}
		Address addr = new Address(num, streetName, city, state, zip);
		return addr;

	}

	public static void getEventInfo(Scanner keyboard, Calendar calendar) {
		// getting userInput to create new event

		Address addr = receiveAddress(keyboard);

		System.out.println("\nPlease enter your event details: ");

		LocalDate date = getDate(keyboard);

		System.out.print("Event name: ");

		String eventName = keyboard.nextLine();

		LocalTime time = getTime(keyboard);

		int min = getDuration(keyboard);

		String notes = receiveNotes(keyboard);

		int categoryNum = getCategory(keyboard);
		String category = getCategoryName(categoryNum);

		Appointment app = new Appointment(date, eventName, time, notes, min, addr, category);
		System.out.println("\nYou entered:\n" + app + "\n");

		addEvent(app, calendar, keyboard);
	}

	public static int getCategory(Scanner keyboard) {
		int category = 0;
		boolean tryAgain = true;

		System.out.println("Category: ");
		System.out.println("1. Birthday");
		System.out.println("2. Meeting");
		System.out.println("3. Homework");
		System.out.println("4. Test");
		System.out.println("5. Appointment");
		System.out.println("6. Anniversary");
		System.out.println("7. Wedding");
		System.out.println("8. Other");

		while (tryAgain) {
			tryAgain = false;
			try {
				category = keyboard.nextInt();
				if (category < 1 || category > 8) {
					System.out.print("Please enter a valid category: ");
					category = keyboard.nextInt();
					tryAgain = true;
				}
			} catch (InputMismatchException e) {
				System.out.print("Please enter a valid category: ");
				category = keyboard.nextInt();
				tryAgain = true;
			}
		}
		return category;

	}

	public static String getCategoryName(int number) {
		switch (number) {
		case 1:
			return "Birthday";
		case 2:
			return "Meeting";
		case 3:
			return "Homework";
		case 4:
			return "Test";
		case 5:
			return "Appointment";
		case 6:
			return "Anniversary";
		case 7:
			return "Wedding";
		default:
			return "Other";
		}

	}

	public static String receiveNotes(Scanner keyboard) {
		System.out.println("Add any additional notes about your event: ");
		String notes = keyboard.nextLine();
		return notes;
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
			if (timeString.charAt(1) == ':') {
				timeString = "0" + timeString;
			}
			if (timeString.length() < 8) {
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
				if ((l = hm.length) > 2 || l < 1) {
					throw new IllegalArgumentException();
				}
				nums = new int[l];
				for (int i = 0; i < l; i++) {
					nums[i] = Integer.parseInt(hm[i]);
				}
			} catch (IllegalArgumentException e) {
				tryAgain = true;
				System.out.println("Please enter a valid value: ");
			}
		} while (tryAgain);

		int min = l == 2 ? nums[0] * 60 + nums[1] : nums[0];

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
				calendar.sendEmail();
				System.out.println("An email with details of this event was sent to your inbox.)");
			} else {
				System.out.println("Action cancelled\n");
			}

		} else {
			calendar.addEvent(app);
			System.out.println("\nEvent added\n");
			calendar.sendEmail();
			System.out.println("An email with details of this event was sent to your inbox.)");
		}
	}

	public static void removeEvent(Scanner keyboard, Calendar calendar) {
		int id = 0;
		boolean tryAgain = true;
		displayEventTitles(calendar);
		System.out.println("Which event would you like to remove?");

		while (tryAgain) {
			try {
				id = keyboard.nextInt();
				keyboard.nextLine();
				tryAgain = false;
			} catch (InputMismatchException e) {
				System.out.print("Please enter an id: ");
				keyboard.nextLine();
			}
		}

		if (calendar.displayEvent(id)) {
			System.out.println("Are you sure want to delete this event? ");
			if (keyboard.nextLine().toLowerCase().equals("yes")) {
				calendar.deleteEvent(id);
				System.out.println("Event deleted.");
			} else {
				System.out.println("Action cancelled");
			}
		} else {
			System.out.println("Invalid event id.");
		}
	}

	public static void modifyEvent(Scanner keyboard, Calendar calendar) {
		System.out.println("Which event would you like to modify: ");
		displayEventTitles(calendar);
		int eventId = 0;
		boolean tryAgain = true;
		while (tryAgain) {
			try {
				eventId = keyboard.nextInt();
				keyboard.nextLine();
				tryAgain = false;
			} catch (InputMismatchException e) {
				System.out.print("Please enter an id: ");
				keyboard.nextLine();
			}
		}
		if (!calendar.displayEvent(eventId)) {
			System.out.println("There is no such event.");
		} else {
			boolean more = true;
			while (more) {
				System.out.println("What would you like to change: ");
				System.out.println("1. Event Name");
				System.out.println("2. Date");
				System.out.println("3. Time");
				System.out.println("4. Address");
				System.out.println("5. Duration");
				System.out.println("6. Notes");
				System.out.println("7. Category");

				int fieldToEdit;
				tryAgain = true;
				while (tryAgain) {
					try {
						fieldToEdit = keyboard.nextInt();
						keyboard.nextLine();

						if (fieldToEdit > 0 && fieldToEdit < 8) {
							tryAgain = false;
							switch (fieldToEdit) {
							case 1:
								modifyName(keyboard, eventId, calendar);
								break;
							case 2:
								modifyDate(keyboard, eventId, calendar);
								break;
							case 3:
								modifyTime(keyboard, eventId, calendar);
								break;
							case 4:
								modifyAddress(keyboard, eventId, calendar);
								break;
							case 5:
								modifyDuration(keyboard, eventId, calendar);
								break;
							case 6:
								modifyNotes(keyboard, eventId, calendar);
								break;
							case 7:
								modifyCategory(keyboard, eventId, calendar);
								break;

							}
						} else {
							System.out.print("Please enter a valid number: ");
							keyboard.nextLine();
						}

					} catch (InputMismatchException e) {
						System.out.print("Please enter a valid number: ");
						keyboard.nextLine();
					}
				}

			}

		}

	}

	private static void modifyCategory(Scanner keyboard, int id, Calendar calendar) {
		System.out.print("\nEnter the new ");
		calendar.modifyCategory(id, getCategoryName(getCategory(keyboard)));

	}

	private static void modifyNotes(Scanner keyboard, int id, Calendar calendar) {
		calendar.modifyDuration(id, receiveNotes(keyboard));

	}

	private static void modifyDuration(Scanner keyboard, int id, Calendar calendar) {
		System.out.println("Enter the new event duration:");
		calendar.modifyDuration(id, getDuration(keyboard));

	}

	private static void modifyAddress(Scanner keyboard, int id, Calendar calendar) {
		System.out.println("Enter the new Address:");
		Address address = receiveAddress(keyboard);
		calendar.modifyAddress(id, address);

	}

	private static void modifyTime(Scanner keyboard, int id, Calendar calendar) {
		System.out.println("Enter the new time:");
		LocalTime time = getTime(keyboard);
		calendar.modifyTime(id, time);

	}

	private static void modifyDate(Scanner keyboard, int id, Calendar calendar) {
		System.out.println("Enter the new date: ");
		LocalDate date = getDate(keyboard);
		calendar.modifyDate(id, date);
	}

	private static void modifyName(Scanner keyboard, int id, Calendar cal) {
		System.out.println("Enter the new event name: ");
		String name = keyboard.nextLine();
		cal.modifyName(id, name);

	}

	public static void displayAllEvents(Calendar calendar, Scanner keyboard) {

		System.out.println("How would you like to display your events? ");

		System.out.println("1. By Date");
		System.out.println("2. By Category");
		System.out.println("3. Event Name");
		int response = 0;
		boolean tryAgain;
		do {
			tryAgain = false;
			try {
				response = keyboard.nextInt();
				keyboard.nextLine();
				if (response != 1 && response != 2 && response != 3) {
					System.out.println("Invalid choice, try again: ");
					tryAgain = true;
				}
			} catch (InputMismatchException e) {
				tryAgain = true;
				System.out.print("Please enter a valid number: ");
				keyboard.nextLine();
			}
		} while (tryAgain);
		if (response == 1) {
			// second column is the event name
			calendar.displayEvents("date, time");
		} else if (response == 2) {
			// 9th column is the date
			calendar.displayEvents("categoryid");
		} else {
			// 12th column is the category
			calendar.displayEvents("eventname");
		}

	}

	public static void displayEventTitles(Calendar calendar) {
		calendar.displayShortEvents();
	}

	public static void displayEventDetails(Calendar calendar, Scanner keyboard) {
		int id = 0;
		boolean tryAgain = true;

		System.out.print("Enter the id of an event to view more details: ");

		while (tryAgain) {
			try {
				id = keyboard.nextInt();
				keyboard.nextLine();
				tryAgain = false;
			} catch (InputMismatchException e) {
				System.out.print("Please enter an id: ");
				tryAgain = true;
			}
		}

		if (!calendar.displayEvent(id)) {
			System.out.println("There is no such event.");
		}
	}

}
