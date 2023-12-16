package Main;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

public class GameWonPanel extends JPanel implements KeyListener{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Window window;
    private UserList userList;
    
    private BufferedImage image1 ;
    
    public GameWonPanel(Window window , UserList userList ) {
    	this.setWindow(window);
        this.setUserList(userList);
        this.setBackground(Color.black);
        
        try {
            image1 = ImageIO.read(getClass().getResourceAsStream("/GameWonPanel/gameWON.png"));
        } catch (IOException e) {
            e.printStackTrace(); 
        }
        
		this.addKeyListener(this); 
	    this.setFocusable(true); 
	    this.setFocusTraversalKeysEnabled(false);
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentShown(ComponentEvent e) {
                requestFocusInWindow();
            }
        });

    }
    
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D) g;

       
        g2.drawImage(image1, 100, 100, window.getWindowWidth()-200, window.getWindowHeight()-200, null);

        
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


	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void keyPressed(KeyEvent e) {
	    if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
	        window.showWelcomeScreen();
	    }
		
	}


	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	

}
