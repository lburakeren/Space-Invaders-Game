package Main;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

public class LoginPanel extends JPanel{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JTextField usernameField;
    private JPasswordField passwordField;
    
    private Window window;
    private UserList userList;

    public LoginPanel(Window window , UserList userList) {
        this.setWindow(window);
        this.setUserList(userList);
        this.setSize(window.getWindowWidth(), window.getWindowHeight());

        setLayout(null); 

        JLabel usernameLabel = new JLabel("Username:");
        usernameField = new JTextField(20);
        JLabel passwordLabel = new JLabel("Password:");
        passwordField = new JPasswordField(20);
        JButton loginButton = new JButton("Login");

      
        int componentWidth = 100; 
        int componentHeight = 20; 

        int x = (window.getWindowWidth() - componentWidth) / 2 - 10; 
        int y = (window.getWindowWidth() - componentHeight * 5) / 2 - 80; 

        
        usernameLabel.setBounds(x, y, componentWidth, componentHeight);
        usernameField.setBounds(x, y + 30, componentWidth, componentHeight);
        passwordLabel.setBounds(x, y + 60, componentWidth, componentHeight);
        passwordField.setBounds(x, y + 90, componentWidth, componentHeight);
        loginButton.setBounds(x, y + 120, componentWidth, componentHeight);

        
        add(usernameLabel);
        add(usernameField);
        add(passwordLabel);
        add(passwordField);
        add(loginButton);

        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = usernameField.getText();
                char[] password = passwordField.getPassword();
               

                
                boolean loginSuccessful = userList.searchUser(username,password);

                if (loginSuccessful) {
                  
                	window.showWelcomeScreen();
                	
                } else {
                    JOptionPane.showMessageDialog(LoginPanel.this, "Invalid username or password", "Login Failed", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    }

	public Window getWindow() {
		return window;
	}

	public void setWindow(Window window) {
		this.window = window;
	}

	public UserList getUserList() {
		return userList;
	}

	public void setUserList(UserList userList) {
		this.userList = userList;
	}
}
