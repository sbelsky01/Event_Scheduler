package eventScheduler;
import java.time.*;
import java.time.format.*;

public class Appointment extends Event {
	private LocalTime time;
	private int durationInMin;
	private Address address;
	
	public Appointment(LocalDate date,String title, LocalTime time, Address address) {
		this(date, title, time,"", 0, address );
	
	}
	
	public Appointment(LocalDate date,String title, LocalTime time, int duration, Address address) {
		this(date, title, time,"", duration, address );
	
	}
	
	public Appointment(LocalDate date,String title, LocalTime time, String notes, Address address) {
		this(date, title, time,"", 0, address );
	
	}
	
	public Appointment(LocalDate date,String title, LocalTime time, String notes, int duration, Address address ) {
		super(date, title,notes);
		this.time = time;
		this.address = address;
		
		if(duration < 0)
		{
			throw new IllegalArgumentException();
		}
		this.durationInMin = duration;
	}
	
	public Appointment copy() {
		return new Appointment(date, title, time, notes, durationInMin, address);
	}
	
	public void setTime(LocalTime time) {
		this.time = time;
	}
	
	public String getFormattedTime() {
		return time.format(DateTimeFormatter.ofPattern("hh:mm a"));
	}
	
	public LocalTime getTime() {
		return time;
	}
	
	public void setAddress(Address address) {
		this.address = address;
	}
	
	public void setAddress(int number, String street, String city, String state, String ZIP) {
		address = new Address (number, street, city, state, ZIP);
	}
	
	public Address getAddress() {
		return address;
	}
	
	public int getDuration() {
		return durationInMin;
	}
	
	public boolean during(LocalDate otherDate, LocalTime otherTime, int otherDuration)
	{
		if(!getDate().equals(otherDate))
		{
			return false;
		}
		
		LocalTime thisStart = time;
		LocalTime thatStart = otherTime;
		LocalTime thisEnd = time.plusMinutes(durationInMin);
		LocalTime thatEnd = otherTime.plusMinutes(otherDuration);
		
		if(thatStart.compareTo(thisEnd) > 0)
		{
			return false;
		}
		
		if(thatEnd.compareTo(thisStart) < 0)
		{
			return false;
		}
		
		return true;
	}
	
	@Override
	public String toString()
	{
		StringBuilder sbr = new StringBuilder("Date: ");
		sbr.append(getFormattedDate());
		sbr.append("\nTime: " + getFormattedTime());
		sbr.append("\n" + getTitle());
		sbr.append("\n" + address);
		if(!getNotes().equals("")) {
			sbr.append("\n" + getNotes());
		}
		
		return sbr.toString();
	}
}
