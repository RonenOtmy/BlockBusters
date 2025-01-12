import java.awt.Color;
import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Vector;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;

public class SQLQuery {

	private static Connection con;
	private static Statement stmt;
	private static String queryStr;
	private static ResultSet rs;
	private static PreparedStatement pstmt;

	// connect
	public static void connect(String username, String password) {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");

			con = DriverManager.getConnection(
					"jdbc:mysql://localhost:3306/blockbasterdb?allowPublicKeyRetrieval=true&useSSL=false", username,
					password);
		} catch (SQLException e) {

			e.printStackTrace();
		} catch (ClassNotFoundException e1) {
			e1.printStackTrace();
		}
	}

	// to get current date and time
	private static java.sql.Timestamp getCurrentTimeStamp() {
		java.util.Date today = new java.util.Date();
		return new java.sql.Timestamp(today.getTime());
	}

	public static boolean insertGameResult(GameResult gr) {

		// public FeedbackData(boolean enjoyment, int playersNumber, float averageAge,
		// String notes) {
		SQLQuery.connect("root", "123456");

		String sql = "INSERT INTO gameresults (winteamname, loseteamname ,date) VALUES (?, ?, ?)";

		try (PreparedStatement pstmt = con.prepareStatement(sql)) {
			pstmt.setString(1, gr.getWinTeamName());
			pstmt.setString(2, gr.getLoseTeamName());

			pstmt.setTimestamp(3, getCurrentTimeStamp());

			int affectedRows = pstmt.executeUpdate();
			return affectedRows > 0;

		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return true;
	}

	public static ArrayList<GameResult> readGameResultData(String winName, String loseName) {
		ResultSet rs;
		ArrayList<GameResult> arr = new ArrayList<GameResult>();

		// SQL query to fetch data from the last month
		String sql = "SELECT winteamname, loseteamname FROM gameresults "
				+ "WHERE (date >= CURDATE() - INTERVAL 1 MONTH) AND" + "(winteamname='" + winName + "' OR winteamname='"
				+ loseName + "') AND" + "(loseteamname='" + winName + "' OR loseteamname='" + loseName + "')";

		try {
			Statement stmt = con.createStatement();
			rs = stmt.executeQuery(sql); // Return the ResultSet to the caller

			while (rs.next()) {
				arr.add(new GameResult(rs.getString(1), rs.getString(2)));
			}

		} catch (SQLException e) {
			e.printStackTrace();

		}
		
		return arr;
	}
	
	public static String [][] readGameResultData2DString(String winName, String loseName) {
	
		ArrayList<GameResult> arr = SQLQuery.readGameResultData(winName, loseName);
		
		String[][] strArr = new String[arr.size()][2];
		for (int i = 0;i<arr.size(); i++) {
			strArr[i][0] = arr.get(i).getWinTeamName();
			strArr[i][1] = arr.get(i).getLoseTeamName();
			}
		SQLQuery.disconnect();
		return strArr;
	}

	// disconnect
	public static void disconnect() {

		try {
			con.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	public static void main(String[] args) {

		SQLQuery.connect("root", "123456");

		//SQLQuery.insertGameResult(new GameResult("moti", "eli"));

		ArrayList<GameResult> arr = SQLQuery.readGameResultData("moti", "eli");

		for (int i = 0; i < arr.size(); i++)
			System.out.println(arr.get(i));
		
		String[][] strarr = SQLQuery.readGameResultData2DString("moti", "eli");
		for (int i = 0; i <strarr.length; i++) {
			for (int j = 0; j <strarr[i].length; j++) {
				System.out.print(strarr[i][j]+ " ");
			}
			System.out.println();	
		}
		
		new SQL2Table(strarr);

		SQLQuery.disconnect();

	}

}