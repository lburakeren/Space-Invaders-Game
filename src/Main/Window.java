package Main;
import java.awt.CardLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.*;

public class Window {
	
	

	private UserList userList = new UserList();
	
	private JPanel mainPanel;
    private CardLayout cardLayout;
    private WelcomePanel welcomePanel;
    private RegistrationPanel registrationPanel;
	private LoginPanel loginPanel;
	
	private String selectedLevel = "Level 1";

    
    private int windowWidth ;
    private int windowHeight ;
    

	public Window(int width, int height, String title) {
		
		this.windowWidth = width ;
		this.windowHeight = height ;
		
		JFrame frame = new JFrame();
		frame.setTitle(title);

		
		frame.setSize(width, height);


		JMenuBar menuBar = new JMenuBar();
	    JMenu fileMenu = new JMenu("File");
	    JMenu helpMenu = new JMenu("Help");

	    JMenuItem register = new JMenuItem("Register");
	    JMenuItem playGame = new JMenuItem("Play Game");
	    JMenuItem highScore = new JMenuItem("High Score");
	    JMenuItem quit = new JMenuItem("Quit");
	    JMenuItem about = new JMenuItem("About");
	    
	    fileMenu.add(register);
	    fileMenu.add(playGame);
	    fileMenu.add(highScore);
	    fileMenu.add(quit);

	    helpMenu.add(about);

	    menuBar.add(fileMenu);
	    menuBar.add(helpMenu);
	    

	    frame.setJMenuBar(menuBar); 
	    
		
		mainPanel = new JPanel();
        cardLayout = new CardLayout();
        mainPanel.setLayout(cardLayout);
        
        welcomePanel = new WelcomePanel(this, userList);
        mainPanel.add(welcomePanel, "welcome");

        registrationPanel = new RegistrationPanel(this,userList);
        mainPanel.add(registrationPanel, "registration");

        loginPanel = new LoginPanel(this, userList);
        mainPanel.add(loginPanel, "login");
        
        
        showWelcomeScreen();


	    
	    register.addActionListener(new ActionListener() {
	        
	        public void actionPerformed(ActionEvent e) {
	        	cardLayout.show(mainPanel, "registration");
	        }
	    });

	    playGame.addActionListener(new ActionListener() {
	        
	        public void actionPerformed(ActionEvent e) {
	        	cardLayout.show(mainPanel, "login");
	        }
	    });

	    highScore.addActionListener(new ActionListener() {
	        @Override
	        public void actionPerformed(ActionEvent e) {
	            ArrayList<User> highScores = userList.getHighScores();
	            HighScorePanel highScorePanel = new HighScorePanel(Window.this,highScores);
	            mainPanel.add(highScorePanel, "High Scores");
	        	cardLayout.show(mainPanel, "High Scores");
	        }
	    });

	    quit.addActionListener(new ActionListener() {
	        @Override
	        public void actionPerformed(ActionEvent e) {
	            System.exit(0);
	        }
	    });

	    about.addActionListener(new ActionListener() {
	        @Override
	        public void actionPerformed(ActionEvent e) {
	            JOptionPane.showMessageDialog(frame, "Developer: Burak EREN\nSchool Number: 20200702005 \nEmail: burak.eren3@std.yeditepe.edu.tr");
	        }
	    });
	    
	    frame.getContentPane().add(mainPanel);
	    
		frame.setResizable(false);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
		

	
		
	}

	public void showWelcomeScreen() {
		 cardLayout.show(mainPanel, "welcome");
		 welcomePanel.setFocusable(true); 
		 welcomePanel.requestFocusInWindow(); 
	}
	
	public int getWindowWidth() {
		return windowWidth;
	}

	public void setWindowWidth(int windowWidth) {
		this.windowWidth = windowWidth;
	}

	public int getWindowHeight() {
		return windowHeight;
	}

	public void setWindowHeight(int windowHeight) {
		this.windowHeight = windowHeight;
	}
	
    public JPanel getMainPanel() {
		return mainPanel;
	}

	public void setMainPanel(JPanel mainPanel) {
		this.mainPanel = mainPanel;
	}

	public CardLayout getCardLayout() {
		return cardLayout;
	}

	public void setCardLayout(CardLayout cardLayout) {
		this.cardLayout = cardLayout;
	}

	public String getSelectedLevel() {
		return selectedLevel;
	}

	public void setSelectedLevel(String selectedLevel) {
		this.selectedLevel = selectedLevel;
	}


	

  
}


