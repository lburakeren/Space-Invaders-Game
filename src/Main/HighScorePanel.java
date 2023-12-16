package Main;

import java.awt.Color;
import java.awt.Font;
import java.util.ArrayList;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

public class HighScorePanel extends JPanel{
	
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Window window;
	private ArrayList<User> userList;
    
    public HighScorePanel(Window window , ArrayList<User> userList){
    	this.setWindow(window);
        this.setUserList(userList);
        this.setSize(window.getWindowWidth(), window.getWindowHeight());

        setLayout(null);
        setBackground(Color.BLACK); 

        JLabel titleLabel = new JLabel("HIGH SCORES");
        titleLabel.setBounds(0, 10, getWidth(), 30);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(Color.WHITE); 
        add(titleLabel);

        int y = 50;
        for (User user : userList) {
            JLabel scoreLabel = new JLabel(user.getUsername() + ": " + user.getScore());
            scoreLabel.setBounds(50, y, getWidth() - 100, 20);
            scoreLabel.setHorizontalAlignment(SwingConstants.CENTER);
            scoreLabel.setFont(new Font("Arial", Font.PLAIN, 16)); 
            scoreLabel.setForeground(Color.WHITE); 
            add(scoreLabel);

            y += 30; 
        }
 }
        


	public Window getWindow() {
		return window;
	}

	public void setWindow(Window window) {
		this.window = window;
	}



	public ArrayList<User> getUserList() {
		return userList;
	}



	public void setUserList(ArrayList<User> userList) {
		this.userList = userList;
	}


	
}
