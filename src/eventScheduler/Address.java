package eventScheduler;

public class Address {
	private int number;
	private String street;
	private String city;
	private String state;
	private String ZIP;
	
	public Address(int number, String street, String city, String state, String ZIP)
	{
		this.number = number;
		this.street = street;
		this.city = city;
		this.state = state;
		this.ZIP = ZIP;
	}
	
	
	public int getNumber() {
		return number;
	}

	public void setNumber(int number) {
		this.number = number;
	}

	public String getStreet() {
		return street;
	}

	public void setStreet(String street) {
		this.street = street;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getZIP() {
		return ZIP;
	}

	public void setZIP(String zIP) {
		ZIP = zIP;
	}
	
	@Override
	public String toString() {
		StringBuilder sbr = new StringBuilder();
		sbr.append(number + " "+ street);
		sbr.append(" " + city + " " + state + ", " + ZIP);
		return sbr.toString();
	}
	
	
	
}

