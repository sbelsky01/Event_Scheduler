package eventScheduler;
import java.time.*;
import java.time.format.*;


public class Event {
	protected LocalDate date;
	protected boolean status;
	protected String title;
	protected String notes;
	

	public Event(LocalDate date, String title) {
		this(date, title, "");
	}
	
	public Event(LocalDate date,String title, String notes) {
		this.date = date;
		status = false;
		this.title = title;
		this.notes = notes;
	}
	
	public LocalDate getDate() {
		return date;
	}
	
	public String getFormattedDate() {
		return date.format(DateTimeFormatter.ofLocalizedDate(FormatStyle.LONG));
	}
	
	public void setDate(LocalDate date) {
		
		this.date = date;
	}
	
	public boolean getStatus() {
		return status;
	}
	
	public void setStatus(boolean completed) {
		this.status = completed;
	}
	
	public String getNotes() {
		return notes;
	}
		
	public void setNotes (String notes) {
		this.notes = notes;
	}
	
	public void addToNotes (String notes) {
		StringBuilder sb = new StringBuilder();
		sb.append(this.notes + notes );
		this.notes = sb.toString();
	}
	
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	
	
	
}
