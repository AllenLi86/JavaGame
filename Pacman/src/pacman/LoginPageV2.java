package pacman;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class LoginPageV2 extends JFrame{
	WelcomeText welcomeText = new WelcomeText();
	PacmanV4 pac;
	
	LoginPageV2(){
		super("Login Page");
		
		add(welcomeText);
		
		setBounds(100, 100, 420, 480);
		setVisible(true);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		
		
		
	}
	
	public class WelcomeText extends JPanel{
		JLabel text = new JLabel("Login to Start!");
		JLabel loginSuccessText = new JLabel("Login successful!");
		JLabel loginfailText = new JLabel("Login again!");
		JLabel createSuccessText = new JLabel("Create account successful!");
		JLabel createfailText = new JLabel("Account already exists!");
		
		public static JTextField tAccount = new JTextField("EnterAccount");
		public static JTextField tPassword = new JTextField("EnterPassword");
		
		JButton loginButton = new JButton("Login");
		JButton createButton = new JButton("Create");
		
		public static JTextField cAccount = new JTextField("NewAccount");
		public static JTextField cPassword = new JTextField("NewPassword");
		
		static JLabel displayScore;
		static JLabel displayHighestScore;
		
		public static JLabel getDisScore() {
			return displayScore;
		}
		
		public static JLabel getDisHScore() {
			return displayHighestScore;
		}
		
		public static JTextField getTAccount() {
			return tAccount;
		}
		
		WelcomeText(){
			
			text.setFont(new Font("MV Boli", Font.BOLD, 40));
			loginSuccessText.setFont(new Font("MV Boli", Font.BOLD, 40));
			loginfailText.setFont(new Font("MV Boli", Font.BOLD, 40));
			createSuccessText.setFont(new Font("MV Boli", Font.BOLD, 30));
			createfailText.setFont(new Font("MV Boli", Font.BOLD, 30));
			
					
			add(text);

			tAccount.setPreferredSize(new Dimension (120,25));
			tPassword.setPreferredSize(new Dimension (120,25));
			
			cAccount.setPreferredSize(new Dimension (120,25));
			cPassword.setPreferredSize(new Dimension (120,25));
			
			add(tAccount); add(tPassword);
			add(loginButton);
			
			add(cAccount); add(cPassword);
			add(createButton);
			
			add(loginSuccessText);
			loginSuccessText.setVisible(false);
			add(loginfailText);
			loginfailText.setVisible(false);

			add(createSuccessText);
			createSuccessText.setVisible(false);
			add(createfailText);
			createfailText.setVisible(false);

			displayScore = new JLabel();
			displayScore.setFont(new Font("MV Boli", Font.BOLD, 30));
			displayHighestScore = new JLabel();
			displayHighestScore.setFont(new Font("MV Boli", Font.BOLD, 30));
			
			processEvent();

		}
		
		private void processEvent() {
			loginButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					loginCheck();
				}
			});
			
			createButton.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
					
					String account = cAccount.getText(), passwd = cPassword.getText();
					int cResult = DBv3.createAccount(account, passwd);
					if(cResult == 1) {
						createSuccessText.setVisible(true);
						createfailText.setVisible(false);
						loginSuccessText.setVisible(false);
						loginfailText.setVisible(false);
					} else if(cResult == 2) {
						createSuccessText.setVisible(false);
						createfailText.setVisible(true);
						loginSuccessText.setVisible(false);
						loginfailText.setVisible(false);

					} else {
						createSuccessText.setVisible(false);
						createfailText.setVisible(false);
					}
					
					displayScore.setVisible(false);
					displayHighestScore.setVisible(false);
					
				}
			});
		}
		
		// -------------------------Login--------------------------
		public void loginCheck() {
		
			String account = tAccount.getText(), passwd = tPassword.getText();
			Boolean isCorrect;
			
			Properties prop = new Properties();
			prop.put("user", "root");
			prop.put("password", "root");
			
			try(Connection conn = DriverManager.getConnection(
					"jdbc:mysql://localhost:3306/pacmandb", prop);) {
				
				isCorrect = DBv3.login(account, passwd, conn);
				
				if (isCorrect) {
					// 帳號密碼 符合 的狀況
					System.out.println("Welcome");
					pac = new PacmanV4();
					pac.setVisible(true);
					pac.setTitle("PacmanV4");
					pac.setBounds(600, 200, 466, 517);
					pac.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
					loginSuccessText.setVisible(true);
					loginfailText.setVisible(false);
					createSuccessText.setVisible(false);
					createfailText.setVisible(false);
					displayScore.setVisible(true);
					displayHighestScore.setVisible(true);
					
					int s = DBv3.getScoreFromDB(tAccount.getText());
					displayScore.setText("Your privious score : " + s);
					
					int hs = DBv3.getHighestScoreFromDB(tAccount.getText());
					displayHighestScore.setText("Your highest score : " + hs);
					
					add(displayScore);
					add(displayHighestScore);
					
				}else {
					// 帳號密碼 不符合 的狀況
					System.out.println("get out of here");
					loginSuccessText.setVisible(false);
					loginfailText.setVisible(true);
					createSuccessText.setVisible(false);
					createfailText.setVisible(false);
					displayScore.setVisible(false);
					displayHighestScore.setVisible(false);
				}
				
				
			}catch(Exception e) {
				System.out.println(e.toString());
			}
			
			
		}
		// ----------------------------------------------------
		
		
		
		
		
		
		
	}

}
