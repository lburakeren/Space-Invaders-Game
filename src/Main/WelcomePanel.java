package Main;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

public class WelcomePanel extends JPanel implements KeyListener,ActionListener{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Window window;
    private UserList userList;
    
    private GamePanel gamePanel = null ;
    
    private BufferedImage welcomeImage ;
    
    private JComboBox<String> levelComboBox;
    
    private Clip musicClip;
    private AudioInputStream audioInputStream;


    
    public WelcomePanel(Window window , UserList userList ) {
    	this.setWindow(window);
        this.setUserList(userList);
        this.setBackground(Color.blue);
        this.setLayout(null);
        
        try {
            welcomeImage = ImageIO.read(getClass().getResourceAsStream("/WelcomePanel/welcomeScreen.png"));
        } catch (IOException e) {
            e.printStackTrace(); 
        }
        
        try {
            InputStream audioStream = getClass().getResourceAsStream("/Musics/welcomescreen.wav");
            BufferedInputStream bufferedStream = new BufferedInputStream(audioStream);
            audioInputStream = AudioSystem.getAudioInputStream(bufferedStream);
            musicClip = AudioSystem.getClip();
            musicClip.open(audioInputStream);
        } catch (IOException | UnsupportedAudioFileException | LineUnavailableException e) {
            e.printStackTrace();
        }
        
        startMusic();
        

        String[] levels = {"Level 1", "Level 2", "Level 3"};
        levelComboBox = new JComboBox<>(levels);
        levelComboBox.setBounds(650, 20, 100, 30);
        levelComboBox.setSelectedIndex(0); 
        levelComboBox.addActionListener((ActionListener) this);
        levelComboBox.setFocusable(false);
        add(levelComboBox);

        
		addKeyListener(this); 
	    setFocusable(true); 
	    requestFocusInWindow(); 

    }
    
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D) g;

       
        g2.drawImage(welcomeImage, 0, 0, window.getWindowWidth(), window.getWindowHeight(), null);
        
        
        String text = "PRESS ENTER TO PLAY";
        Font font = new Font("Calibri", Font.BOLD, 30);
        g2.setFont(font);
        g2.setColor(Color.LIGHT_GRAY); 
        g2.drawString(text,260, 420);
        
        if(userList.getCurrentUser() != null) {
        	repaint();
            String text2 = "Logined User: " +userList.getCurrentUser().getUsername();
            Font font2 = new Font("Calibri", Font.ITALIC, 20);
            g2.setFont(font2);
            g2.setColor(Color.LIGHT_GRAY); 
            g2.drawString(text2,500, 720);
        }else {
        	repaint();
            String text3 = "No User Logined";
            Font font3 = new Font("Calibri", Font.ITALIC, 20);
            g2.setFont(font3);
            g2.setColor(Color.LIGHT_GRAY); 
            g2.drawString(text3, 500 , 720);
        }
        


   
        
    }
    
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == levelComboBox) {
            window.setSelectedLevel((String) levelComboBox.getSelectedItem());
        }
    }
    
    public void setVisible(boolean visible) {
        super.setVisible(visible);
        if (visible) {
            startMusic();
        } else {
            stopMusic();
        }
    }

    private void startMusic() {
        try {
            if (musicClip != null && !musicClip.isRunning()) {
                musicClip.loop(Clip.LOOP_CONTINUOUSLY);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void stopMusic() {
        try {
            if (musicClip != null && musicClip.isRunning()) {
                musicClip.stop();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
	


	public GamePanel getGamePanel() {
		return gamePanel;
	}


	public void setGamePanel(GamePanel gamePanel) {
		this.gamePanel = gamePanel;
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
		if (e.getKeyCode() == KeyEvent.VK_ENTER) {
	        
			if ( userList.getCurrentUser() != null) {
	            if (gamePanel != null) {
	                remove(gamePanel); 
	                gamePanel = null;
	                revalidate();
	            }
	            
				gamePanel = new GamePanel(window,userList);
	        	window.getMainPanel().add(gamePanel,"game");
                window.getCardLayout().show(window.getMainPanel(), "game");
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        gamePanel.requestFocus();
                    }
                });
            
                repaint();
			} 

        }
		
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}


}
