package eventScheduler;

import java.sql.*;

public class Calendar {

	private Connection conn;
	private int userId;

	public Calendar(Connection conn, int id) {
		this.conn = conn;
		this.userId = id;
	}

	public int overlapping(Appointment appt) {
		try {
			String SQL = "SELECT e.eventid, date, time, duration "
					+ "FROM event e JOIN personhasevent phe ON e.eventid = phe.eventid "
					+ "WHERE phe.personid = ? AND date = ?";
			PreparedStatement pstmt = conn.prepareStatement(SQL);
			pstmt.setInt(1, userId);
			pstmt.setDate(2, Date.valueOf(appt.getDate()));
			ResultSet rs = pstmt.executeQuery();
			while(rs.next()) {
				if(appt.during(rs.getDate(2).toLocalDate(), rs.getTime(3).toLocalTime(), rs.getInt(4))) {
					return rs.getInt(1);
				}
			}

		} catch(SQLException e) {
			System.out.println(e.getMessage());
		}

		return 0;

	}

	public void addEvent(Appointment appt) {
		try {
			int newId;
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT max(e.eventid) FROM event e");
			newId = rs.next() ? rs.getInt(1)+1 : 1;

			//write the insert statement
			String SQL = "INSERT INTO event (EventId, EventName, StreetNum, StreetName, "
					+ "City, State, Zip, Date, Time, Duration, Notes) "
					+ "VALUES (?,?,?,?,?,?,?,?,?,?,?);"
					+ "INSERT INTO personhasevent (PersonId, EventId) VALUES (?, ?)";
			//prepare the statement
			PreparedStatement pstmt = conn.prepareStatement(SQL);
			Address addr = appt.getAddress();
			pstmt.setInt(1, newId);
			pstmt.setString(2, appt.getTitle());
			pstmt.setInt(3, addr.getNumber());
			pstmt.setString(4, addr.getStreet());
			pstmt.setString(5, addr.getCity());
			pstmt.setString(6, addr.getState());
			pstmt.setString(7, addr.getZIP());
			pstmt.setDate(8, Date.valueOf(appt.getDate()));
			pstmt.setTime(9, Time.valueOf(appt.getTime()));
			pstmt.setInt(10, appt.getDuration());
			pstmt.setString(11, appt.getNotes());
			pstmt.setInt(12, userId);
			pstmt.setInt(13, newId);
			//execute the statement
			pstmt.executeUpdate();
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
	}

	public void displayEvents() {
		try {
			String SQL = "SELECT * "
					+ "FROM event e JOIN personhasevent phe ON e.eventid = phe.eventid "
					+ "WHERE phe.personId = ?";
			//prepare the statement
			PreparedStatement pstmt;
			pstmt = conn.prepareStatement(SQL);
			pstmt.setInt(1, userId);
			ResultSet rs = pstmt.executeQuery();
			
			//display the results, and display a message if there are no results
			if(!rs.next()) {
				System.out.println("You have no events scheduled.");
			} else {
				Appointment appt;
				Address addr;
				do {
					addr = new Address(rs.getInt(3), rs.getString(4), rs.getString(5), rs.getString(6), rs.getString(7));
					appt = new Appointment(rs.getDate(9).toLocalDate(), rs.getString(2), rs.getTime(8).toLocalTime(), rs.getString(11), rs.getInt(10), addr);
					System.out.println();
					System.out.println("Event #" + rs.getInt(1));
					System.out.println(appt);
					System.out.println();
					
				}while(rs.next());
			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public boolean displayEvent(int id) {
		try {
			String SQL = "SELECT * "
					+ "FROM event e JOIN personhasevent phe ON e.eventId = phe.eventId "
					+ "WHERE phe.personId = ? AND e.eventId = ?";
			//prepare the statement
			PreparedStatement pstmt = conn.prepareStatement(SQL);
			pstmt.setInt(1, userId);
			pstmt.setInt(2, id);
			ResultSet rs = pstmt.executeQuery();
			if(rs.next()) {
				Address addr = new Address(rs.getInt(3), rs.getString(4), rs.getString(5), rs.getString(6), rs.getString(7));
				Appointment appt = new Appointment(rs.getDate(9).toLocalDate(), rs.getString(2), rs.getTime(8).toLocalTime(), rs.getString(11), rs.getInt(10), addr);
				System.out.println(appt);
				return true;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}

	public void deleteEvent(int id) {
		try {
			String SQL = "DELETE FROM personhasevent WHERE EventId = ?;"
						+ "DELETE FROM event WHERE EventId = ?;";
			PreparedStatement pstmt = conn.prepareStatement(SQL);
			pstmt.setInt(1, id);
			pstmt.setInt(2, id);
			pstmt.executeUpdate();
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
	}

}
