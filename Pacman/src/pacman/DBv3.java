package pacman;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Properties;

public class DBv3 {
	static final String sqlCheckAccount = "SELECT * FROM accounts WHERE account = ?";
	static final String sqlAppendAccount = "INSERT INTO accounts (account,password) VALUES (?,?)";
	static PreparedStatement checkStatement, appendStatement;
	
	public static int createAccount (String newAccount, String newPassword) {
		
		Properties prop = new Properties();
		prop.put("user", "root");
		prop.put("password", "root");
		
		try (Connection conn = DriverManager.getConnection(
				"jdbc:mysql://localhost:3306/pacmandb", prop);) {
						
			checkStatement = conn.prepareStatement(sqlCheckAccount);
			appendStatement = conn.prepareStatement(sqlAppendAccount);
			
			if (!isDataRepeat(newAccount)) {
				if (appendData(newAccount, newPassword)) {
					System.out.println("Success");
					return 1;
				}else {
					System.out.println("E2");
					return 0;
				}
			}else {
				System.out.println("E1"); // 帳戶已經存在
				return 2;
			}
			
		}catch(Exception e) {
			System.out.println( e.toString() );
		}
		return 0;
	}
	
	static boolean isDataRepeat(String account) throws Exception {
		checkStatement.setString(1, account);
		ResultSet rs = checkStatement.executeQuery();
		return rs.next();  // 有下一筆就回傳true
	}
	
	static boolean appendData(String account, String password) throws Exception {
		appendStatement.setString(1, account);
		appendStatement.setString(2, password);
		int n = appendStatement.executeUpdate();
		
		return n != 0;
	}

	// ------------------------------------------
	
	// login檢查方法
	static Boolean login(String account, String passwd, Connection conn) 
			throws Exception {
			String sql = "SELECT * FROM accounts WHERE account = ?";
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setString(1, account);
			ResultSet rs = ps.executeQuery();
			
			if (rs.next()) {
				String loginPasswd = rs.getString("password");
				if (passwd.equals(loginPasswd)) {
					return true;
				}else {
					return false;
				}
			}else {
				return false;
			}
		}
		
	// ------------------------------------------
	
	// 把分數上傳到資料庫
	static void updateScore(String account, int score) {
		Properties prop = new Properties();
		prop.put("user", "root");
		prop.put("password", "root");
		
		try (Connection conn = DriverManager.getConnection(
				"jdbc:mysql://localhost:3306/pacmandb", prop);) {
		
		String sql = "UPDATE accounts SET score = ? WHERE account = ?";
		PreparedStatement ps = conn.prepareStatement(sql);
		ps.setInt(1, score);
		ps.setString(2, account);
		ps.executeUpdate();
		}catch(Exception e) {
			System.out.println( e.toString() );
		}
		
	}
	
	// 把最高分數上傳到資料庫
	static void updateHighestScore(String account, int score, int hscore) {
		Properties prop = new Properties();
		prop.put("user", "root");
		prop.put("password", "root");
		
		try (Connection conn = DriverManager.getConnection(
				"jdbc:mysql://localhost:3306/pacmandb", prop);) {
			
			if(score > hscore) {
				String sql = "UPDATE accounts SET highestScore = ? WHERE account = ?";
				PreparedStatement ps = conn.prepareStatement(sql);
				ps.setInt(1, score);
				ps.setString(2, account);
				ps.executeUpdate();
			}
		
		}catch(Exception e) {
			System.out.println( e.toString() );
		}
		
	}
	

	// 讀取資料庫分數資料
	static int getScoreFromDB(String account) {
		int acntScore = 0;
		
		Properties prop = new Properties();
		prop.put("user", "root");
		prop.put("password", "root");
		
		try (Connection conn = DriverManager.getConnection(
				"jdbc:mysql://localhost:3306/pacmandb", prop);) {
		
		
		String sql = "SELECT * FROM accounts WHERE account = ?";
		PreparedStatement ps = conn.prepareStatement(sql);
		ps.setString(1, account);
		ResultSet rs = ps.executeQuery();
		
		if(rs.next()) {
			acntScore = rs.getInt("score");
			System.out.println("gotIt");
			System.out.println(acntScore);
			return acntScore;
		}
		
		}catch(Exception e) {
			System.out.println( e.toString() );
		}
		return acntScore;
		
		
		
	}
	
	// --------------------讀取資料庫最高分數-----------------------
	static int getHighestScoreFromDB(String account) {
		int acntHScore = 0;
		
		Properties prop = new Properties();
		prop.put("user", "root");
		prop.put("password", "root");
		
		try (Connection conn = DriverManager.getConnection(
				"jdbc:mysql://localhost:3306/pacmandb", prop);) {
		
		
		String sql = "SELECT * FROM accounts WHERE account = ?";
		PreparedStatement ps = conn.prepareStatement(sql);
		ps.setString(1, account);
		ResultSet rs = ps.executeQuery();
		
		if(rs.next()) {
			acntHScore = rs.getInt("highestScore");
			System.out.println("gotIt");
			System.out.println(acntHScore);
			return acntHScore;
		}
		
		}catch(Exception e) {
			System.out.println( e.toString() );
		}
		return acntHScore;
		
		
		
	}
	
	
	

	
	
	
	
	
	
	
}




