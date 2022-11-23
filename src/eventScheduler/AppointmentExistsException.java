package eventScheduler;

public class AppointmentExistsException extends Exception {
	
	public AppointmentExistsException() {
		// TODO Auto-generated constructor stub

		super();

	}

	public AppointmentExistsException(String message) {

		super("There is already an appointment at this time!");

	}

}
