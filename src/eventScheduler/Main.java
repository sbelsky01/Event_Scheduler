package eventScheduler;

import java.time.*;
import java.time.format.*;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Main {

	public static void main(String[] args) {

		Scanner keyboard = new Scanner(System.in);
		Calendar calendar = new Calendar();

		int choice = 0;

		while (choice != 5) {

			choice = menu(keyboard);

			switch (choice) {
			case 1:
				addEvent(keyboard, calendar);
				break;
			case 2:
				removeEvent();
				break;
			case 3:
				modifyEvent();
				break;

			case 4:
				displayEvents(calendar);
				break;
			}

		}
		System.exit(0);
	}

	public static int menu(Scanner keyboard) {
		int choice = 0;

		System.out.println("Options: ");
		System.out.println("1. Add event");
		System.out.println("2. Remove event");
		System.out.println("3. Modify event");
		System.out.println("4. Display events");
		System.out.println("5. Exit");

		System.out.print("What would you like to do? ");

		try {
			choice = keyboard.nextInt();
		} catch (InputMismatchException e) {
			System.out.println("Input mismatch! Enter a new number: ");
		}
		return choice;
	}

	private static void addEvent(Scanner keyboard, Calendar calendar) {
		// TODO Auto-generated method stub

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

		System.out.println("\nPlease enter your appointment details: ");
		System.out.print("Event date (yyyy-mm-dd): ");
		
		boolean tryAgain;
		String dateString = null;
		LocalDate date = null;
		
		do {
			tryAgain = false;
			dateString = keyboard.nextLine();
			try {
				date = LocalDate.parse(dateString);
			}catch(DateTimeParseException e) {
				tryAgain = true;
				System.out.print("Please use the proper format: ");
			}
		}while(tryAgain);
		
		System.out.print("Event name: ");
		String eventName = keyboard.nextLine();
		
		System.out.print("Event time(hh:mm): ");
		
		String stringTime = null;
		LocalTime time = null;
		
		do {
			tryAgain = false;
			stringTime = keyboard.nextLine();
			stringTime += ":00";
			try {
				time = LocalTime.parse(stringTime);
			}catch(DateTimeParseException e) {
				tryAgain = true;
				System.out.print("Please use the proper format: ");
			}
		}while(tryAgain);
		
		System.out.print("How long will the appointment be(in minutes)? ");
		int min = keyboard.nextInt();
		keyboard.nextLine();
		System.out.println("Add any additional notes about your appointment: ");
		String notes = keyboard.nextLine();

		Appointment app = new Appointment(date, eventName, time, notes, min, addr);
		System.out.println("\n" + app + "\n");


		int checkIfAdd = calendar.putEventInPlace(app);
		if (checkIfAdd == -1) {
			System.out.println("There is already an event at this time.");
			System.out.print("Do you wish to proceed? Yes or no: ");
			String answer = keyboard.nextLine();

			if (answer.equalsIgnoreCase("Yes")) {

				calendar.forcePutEventInPlace(app);
				System.out.println("\nEvent added\n\n");
			}
			else {
				System.out.println("Action cancelled\n\n");
			}
			
		} else {
			System.out.println("\nEvent added\n\n");
		}

	}

	private static void removeEvent() {
		// TODO Auto-generated method stub

	}

	private static void modifyEvent() {
		// TODO Auto-generated method stub

		// display arrayList of events with numbers, get index from user and give them
		// options of what to change
		//

	}

	private static void displayEvents(Calendar calendar) {
		// TODO Auto-generated method stub

		ArrayList<Appointment> events = calendar.getEvents();
		
		System.out.println();

		if (events.size() == 0) {
			System.out.println("You have no appointments scheduled.");
		} else {
			System.out.println("  Scheduled Appointments:");
			System.out.println("---------------------------\n");
			
			for (int i = 0; i < events.size(); i++) {

				System.out.println(events.get(i));
				System.out.println();

			}
		}
		
		System.out.println();

	}

}
