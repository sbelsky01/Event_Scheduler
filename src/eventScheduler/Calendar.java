package eventScheduler;

import java.util.*;
import java.io.*;

public class Calendar {

	private ArrayList<Appointment> events;

	public Calendar() {

		this.events = new ArrayList<Appointment>();
	}

	public ArrayList<Appointment> getEvents() {
		ArrayList<Appointment> copy = new ArrayList<Appointment>();
		Appointment apptCopy;
		
		for(int i=0; i<events.size(); i++) {
			apptCopy = events.get(i).copy();
			copy.add(apptCopy);
		}
		return events;
	}

	public void addEvents(Appointment e) {

		events.add(e);

	}

	public int putEventInPlace(Appointment event) {

		int i;
		// loop through the array of events and find the right position
		for ( i = 0; i < events.size(); i++) {

			int num = events.get(i).getDate().compareTo(event.getDate());
			
			if(num == 0) {
				num = events.get(i).getTime().compareTo(event.getTime());
			}

			if (event.during((Appointment) events.get(i))) {
				
				
				return -1;
	
			}	

			else if (num > 0) {

				// insert it before the one it's comparing it to
				events.add(i, event);
				return 0;
				

			}

		}
		
		if (i == events.size()) {
			events.add(event);
			
		}
		
		
			return 0;
		

	}

	public void forcePutEventInPlace(Appointment event) {

		/*int i;

		for (i = 0; i < events.size(); i++) {
			
			//should we check here the time, and put in order of that or this is enough?
			events.add(i, event);
			break;
	
		}*/
		
		int i;
		// loop through the array of events and find the right position
		for ( i = 0; i < events.size(); i++) {

			int num = events.get(i).getDate().compareTo(event.getDate());
			
			if(num == 0) {
				num = events.get(i).getTime().compareTo(event.getTime());
			}

			if (num > 0) {

				// insert it before the one it's comparing it to
				events.add(i, event);
				break;
			}
			
		}

	}

}
