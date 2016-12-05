package assistants;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import model.*;
import play.data.Form;
import play.db.DB;

public class Database {
	public static List<Object[]> getData(String query, int fields) {
		List<Object[]> results = new ArrayList<>();
		try {
			//Establish the Connection
    		Connection conn = DB.getConnection();
			//Create the Statement
            Statement stmt = conn.createStatement();
			//Execute the Statement
            ResultSet rs = stmt.executeQuery(query);
			//Process the ResultSet
			while(rs.next()) {
				Object[] result = new Object[fields];
				for(int i = 0; i < fields; i++) {
					result[i] = rs.getObject(i+1);
				}
				results.add(result);
			}
			//End Connection
			rs.close();
			conn.close();
		} catch (SQLException e) {
			System.out.println("SQL Exception: " + e.getMessage());
			System.out.println("SQL State: " + e.getSQLState());
			System.out.println("Vendor Error: " + e.getErrorCode());
			//e.printStackTrace();
		}
		
		return results;
	}
	public static boolean update(String query) {
		Statement stmt = null;
		boolean result = false;
		try {
			//Establish the Connection
    		Connection conn = DB.getConnection();
			
			//Create the Statement
			stmt = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
			
			//Execute the Statement
			result = stmt.executeUpdate(query) > 0;
			
			conn.close();
		} catch (SQLException e) {
			System.out.println("Failed Query: " + query);
			System.out.println("SQL Exception: " + e.getMessage());
			System.out.println("SQL State: " + e.getSQLState());
			System.out.println("Vendor Error: " + e.getErrorCode());
			//e.printStackTrace();
		}
		return result;
	}
	public static Integer count(String query) {
		Integer result = 0;
		Statement stmt = null;
		ResultSet rs = null;
		try {
			//Establish the Connection
    		Connection conn = DB.getConnection();
			
			//Create the Statement
			stmt = conn.createStatement();
			
			//Execute the Statement
			rs = stmt.executeQuery(query);
			
			//Process the ResultSet
			if(rs.next()) {
				result = rs.getInt(1);
			}
			rs.close();
			conn.close();
		} catch (SQLException e) {
			System.out.println("SQL Exception: " + e.getMessage());
			System.out.println("SQL State: " + e.getSQLState());
			System.out.println("Vendor Error: " + e.getErrorCode());
			//e.printStackTrace();
		}
		
		return result;
	}
}