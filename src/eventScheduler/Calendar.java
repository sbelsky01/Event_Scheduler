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
			while (rs.next()) {
				if (appt.during(rs.getDate(2).toLocalDate(), rs.getTime(3).toLocalTime(), rs.getInt(4))) {
					return rs.getInt(1);
				}
			}

		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}

		return 0;

	}

	public void addEvent(Appointment appt) {
		try {
			int newId;
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT max(e.eventid) FROM event e");
			newId = rs.next() ? rs.getInt(1) + 1 : 1;

			// write the insert statement
			String SQL = "INSERT INTO event (EventId, EventName, StreetNum, StreetName, "
					+ "City, State, Zip, Date, Time, Duration, Notes, CategoryId) "
					+ "VALUES (?,?,?,?,?,?,?,?,?,?,?,?);"
					+ "INSERT INTO personhasevent (PersonId, EventId) VALUES (?, ?)";
			// prepare the statement
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
			pstmt.setString(12, appt.getCategory());
			pstmt.setInt(13, userId);
			pstmt.setInt(14, newId);
			// execute the statement
			pstmt.executeUpdate();
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
	}

	public void displayEvents(String colName) {
		try {
			String SQL = "SELECT * FROM event e " + "JOIN personhasevent phe ON e.eventid = phe.eventid "
					+ "JOIN categorytypes c ON e.categoryid = c.categoryid " + "WHERE phe.personId = ? " + "ORDER BY e."
					+ colName + " ASC";
			// prepare the statement
			PreparedStatement pstmt = conn.prepareStatement(SQL);
			pstmt.setInt(1, userId);
			ResultSet rs = pstmt.executeQuery();

			// display the results, and display a message if there are no results
			if (!rs.next()) {
				System.out.println("You have no events scheduled.");
			} else {
				do {
					System.out.println("#" + rs.getInt(1));
					System.out.println("\t" + rs.getString(2));
					System.out.print("\tDate: ");
					System.out.println(
							rs.getDate(9).toLocalDate().format(DateTimeFormatter.ofLocalizedDate(FormatStyle.LONG)));
					System.out.print("\tTime: ");
					System.out.println(rs.getTime(8).toLocalTime().format(DateTimeFormatter.ofPattern("hh:mm a")));
					System.out.print("\t" + rs.getInt(3) + " " + rs.getString(4) + " ");
					System.out.println(rs.getString(5) + ", " + rs.getString(6) + "  " + rs.getString(7));
					System.out.println("\tCategory: " + rs.getString(16));
					System.out.println("\t" + rs.getString(11));
					System.out.println();
				} while (rs.next());
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void displayShortEvents() {
		try {
			String SQL = "SELECT e.EventId, EventName "
					+ "FROM event e JOIN personhasevent phe ON e.eventid = phe.eventid " + "WHERE phe.personId = ?";
			// prepare the statement
			PreparedStatement pstmt;
			pstmt = conn.prepareStatement(SQL);
			pstmt.setInt(1, userId);
			ResultSet rs = pstmt.executeQuery();

			// display the results, and display a message if there are no results
			if (!rs.next()) {
				System.out.println("You have no events scheduled.");
			} else {
				do {
					System.out.println();
					System.out.print("#" + rs.getInt(1) + " ");
					System.out.println(rs.getString(2));

				} while (rs.next());
			}
			System.out.println();

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public boolean displayEvent(int id) {
		try {
			String SQL = "SELECT * " + "FROM event e JOIN personhasevent phe ON e.eventId = phe.eventId "
					+ "LEFT JOIN categorytypes c ON e.categoryid = c.categoryid "
					+ "WHERE phe.personId = ? AND e.eventId = ?";
			// prepare the statement
			PreparedStatement pstmt = conn.prepareStatement(SQL);
			pstmt.setInt(1, userId);
			pstmt.setInt(2, id);
			ResultSet rs = pstmt.executeQuery();
			if (rs.next()) {
//				Address addr = new Address(rs.getInt(3), rs.getString(4), rs.getString(5), rs.getString(6),
//						rs.getString(7));
//				Appointment appt = new Appointment(rs.getDate(9).toLocalDate(), rs.getString(2),
//						rs.getTime(8).toLocalTime(), rs.getString(11), rs.getInt(10), addr, rs.getString(13));
//				System.out.println("");
//				System.out.println("\n" + appt + "\n");
				System.out.print("Date: ");
				System.out.println(
						rs.getDate(9).toLocalDate().format(DateTimeFormatter.ofLocalizedDate(FormatStyle.LONG)));
				System.out.println("Time: ");
				System.out.println(rs.getTime(8).toLocalTime().format(DateTimeFormatter.ofPattern("hh:mm a")));
				System.out.println("Category: " + rs.getString(14));
				System.out.println(rs.getString(2));
				System.out.print(rs.getInt(3) + " " + rs.getString(4) + " ");
				System.out.println(rs.getString(5) + ", " + rs.getString(6) + "  " + rs.getString(7));
				System.out.println(rs.getString(11));
				System.out.println();
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
			String SQL = "DELETE FROM personhasevent WHERE EventId = ?;" + "DELETE FROM event WHERE EventId = ?;";
			PreparedStatement pstmt = conn.prepareStatement(SQL);
			pstmt.setInt(1, id);
			pstmt.setInt(2, id);
			pstmt.executeUpdate();
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
	}

	public void setId(int id) {
		this.userId = id;
	}

	public void sendEmail() {
		Email email = new Email(getUserEmail(), "Event Created", getNewestEvent());

	}

	public String getUserEmail() {
		String SQL = "SELECT email " + "FROM persontable p " + "WHERE p.personId = ?";
		PreparedStatement pstmt;
		String email = null;

		try {
			pstmt = conn.prepareStatement(SQL);

			pstmt.setInt(1, userId);
			ResultSet rs = pstmt.executeQuery();
			if (rs.next()) {
				email = rs.getString(1);
			}

		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		return email;
	}

	public String getNewestEvent() {
		// Sort in descending order and only allow one to get the last event added
		String SQL = "SELECT * " + "FROM event e JOIN personhasevent phe ON e.eventId = phe.eventId "
				+ "WHERE phe.personId = ? " + "ORDER BY e.eventid DESC LIMIT 1";
		// prepare the statement
		PreparedStatement pstmt;
		String event = null;
		try {
			pstmt = conn.prepareStatement(SQL);

			pstmt.setInt(1, userId);
			ResultSet rs = pstmt.executeQuery();

			if (rs.next()) {

				Address addr = new Address(rs.getInt(3), rs.getString(4), rs.getString(5), rs.getString(6),
						rs.getString(7));
				Appointment appt = new Appointment(rs.getDate(9).toLocalDate(), rs.getString(2),
						rs.getTime(8).toLocalTime(), rs.getString(11), rs.getInt(10), addr, rs.getString(12));

				event = "\n" + appt + "\n";
			}
			System.out.println(event);
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		return event;
	}

	public void modifyName(int id, String name) {

		String SQL = "UPDATE event SET eventname = ? WHERE eventid = ?";
		PreparedStatement pstmt;
		try {
			pstmt = conn.prepareStatement(SQL);
			pstmt.setString(1, name);
			pstmt.setInt(2, id);
			ResultSet rs = pstmt.executeQuery();

		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}

	}

	public void modifyDate(int id, LocalDate date) {
		String SQL = "UPDATE event e SET date = ? WHERE eventid = ?";
		PreparedStatement pstmt;
		try {
			pstmt = conn.prepareStatement(SQL);
			pstmt.setDate(1, Date.valueOf(date));
			pstmt.setInt(2, id);
			pstmt.executeQuery();

		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}

	}

	public void modifyTime(int id, LocalTime time) {
		String SQL = "UPDATE event e SET time = ? WHERE eventid = ?";
		PreparedStatement pstmt;
		try {
			pstmt = conn.prepareStatement(SQL);
			pstmt.setTime(1, Time.valueOf(time));
			pstmt.setInt(2, id);
			pstmt.executeQuery();

		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
	}

	public void modifyAddress(int id, Address address) {
		String SQL = "UPDATE event e SET streetnum = ?, streetname = ?, city = ?, state = ?, zip = ? WHERE eventid = ?";
		PreparedStatement pstmt;
		try {
			pstmt = conn.prepareStatement(SQL);
			pstmt.setInt(1, address.getNumber());
			pstmt.setString(2, address.getStreet());
			pstmt.setString(3, address.getCity());
			pstmt.setString(4, address.getState());
			pstmt.setString(5, address.getZIP());
			pstmt.setInt(6, id);
			pstmt.executeQuery();

		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}

	}

	public void modifyDuration(int id, int duration) {
		String SQL = "UPDATE event e SET duration = ? WHERE eventid = ?";
		PreparedStatement pstmt;
		try {
			pstmt = conn.prepareStatement(SQL);
			pstmt.setInt(1, duration);
			pstmt.setInt(2, id);
			pstmt.executeQuery();

		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}

	}

	public void modifyDuration(int id, String notes) {
		String SQL = "UPDATE event e SET notes = ? WHERE eventid = ?";
		PreparedStatement pstmt;
		try {
			pstmt = conn.prepareStatement(SQL);
			pstmt.setString(1, notes);
			pstmt.setInt(2, id);
			pstmt.executeQuery();

		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}

	}

	public void modifyCategory(int id, String category) {
		String SQL = "UPDATE categorytypes SET categorydescription = ? WHERE eventid = ?";
		PreparedStatement pstmt;
		try {
			pstmt = conn.prepareStatement(SQL);
			pstmt.setString(1, category);
			pstmt.setInt(2, id);
			pstmt.executeQuery();

		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}

	}

}
