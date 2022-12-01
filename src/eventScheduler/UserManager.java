package eventScheduler;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class UserManager {
	private Connection conn;
	
	public UserManager(Connection conn) {
		this.conn = conn;
	}
	
	public boolean addUser(String f, String l) {
		try {
			//set newId to 1 number higher than the maximum id already in the database
			int newId;
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT max(PersonId) FROM PersonTable;");
			newId = rs.next() ? rs.getInt(1)+1 : 1;
			

			//check if the user already exists in the database
			if(getUserId(f, l) > 0) {
				return false;
			}
			
			//once we know the user doesn't already exist, add the information to the database
			String SQL = "INSERT INTO PersonTable (PersonId, FirstName, LastName) VALUES (?,?,?)";
			PreparedStatement pstmt = conn.prepareStatement(SQL);
			pstmt.setInt(1, newId);
			pstmt.setString(2, f);
			pstmt.setString(3, l);
			pstmt.executeUpdate();
			return true;
			
		} catch (SQLException e) {
			System.out.println(e.getMessage());
			return false;
		}
		
	}
	
	//returns the id of the user, returns 0 if the user doesn't exist
	public int signIn(String f, String l) {
		return getUserId(f, l);
	}
	
	//returns the id of the user with the specified first and last name
	private int getUserId(String f, String l) {
		try {
			String SQL = "SELECT PersonId FROM PersonTable WHERE FirstName = ? AND LastName = ?";
			PreparedStatement pstmt = conn.prepareStatement(SQL);
			pstmt.setString(1, f);
			pstmt.setString(2, l);
			ResultSet rs = pstmt.executeQuery();
			if(rs.next()) {
				return rs.getInt(1);
			} else {
				return 0;
			}
		} catch (SQLException e) {
			System.out.println(e.getMessage());
			return 0;
		}
	}

}
