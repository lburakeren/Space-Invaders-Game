package Main;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.JPanel;

import Entity.AlienBomb;
import Entity.AlienBullet;
import Entity.Aliens;
import Entity.Bullet;
import Entity.Player;



public class GamePanel extends JPanel implements Runnable {

    private static final long serialVersionUID = 1L;

    private int FPS = 60;
    private double currentFPS = 60 ;
    
    private long currentTime = 0;
    private long progress_count = 0 ;
    
    private static ArrayList<Aliens> aliensList = new ArrayList<>();

	KeyHandler keyH = new KeyHandler();
    Thread gameThread;
    
    private GameOverPanel gameOverPanel = null;
    private GameWonPanel gameWonPanel = null;
    
    
    Player player = new Player(this,keyH, 350, 600, 70, 70);
    private GameBackground background;
    
	private Window window;
	private UserList userList;
	
	private int k=0 , l=0 , m=0 ;
	
    private Clip musicClip;
    private AudioInputStream audioInputStream;
    
    private Clip gameOver ;
    private Clip gameWin ;
    private Clip aliendeath;
    private Clip alienhitted ;
    private Clip playerhitted ;
	
    

    public GamePanel(Window window , UserList userList) {
    	this.setWindow(window);
        this.setUserList(userList);
    	this.setSize(800,800);
    	background = new GameBackground();
        this.setLayout(null);
        this.currentTime = System.currentTimeMillis() / 1000;
        
        loadMusics();

    	this.addKeyListener(keyH);
    	setFocusable(true);
    	LoadLevels();
        createAliens1();
        startGameThread();
    }


	public void startGameThread() {
        gameThread = new Thread(this);
        gameThread.start();
    }

  

    @Override
    public void run() {
        long initialTimeInSeconds = System.currentTimeMillis() / 1000;
        int previousSecond = (int) currentTime;
        boolean running = true;
        
        int frameCount = 0;
        long lastFPSTime = System.nanoTime();

        while (running) {
            double drawInterval = 1000000000 / FPS;
            double nextDrawTime = System.nanoTime() + drawInterval;

            currentTime = System.currentTimeMillis() / 1000 - initialTimeInSeconds;

            if ((int) currentTime != previousSecond) {
                previousSecond = (int) currentTime;

                if (currentTime > 0 && previousSecond % 1 == 0) {
                    progress_count++;
                }
            }

            update();
            repaint();

            if (getProgress_count() >= 100) {
                running = false;
                
                gameWin.setFramePosition(0);
                gameWin.start();

                if (player.getScore() > userList.getCurrentUser().getScore()) {
                    userList.getCurrentUser().setScore(player.getScore());
                    userList.updateUserScoreOnFile();
                }

                gameWonPanel = new GameWonPanel(window, userList);
                window.getMainPanel().add(gameWonPanel, "gameWon");
                window.getCardLayout().show(window.getMainPanel(), "gameWon");
                return;
            }

            if (player.getHealth() <= 0) {
                
                gameOver.setFramePosition(0);
                gameOver.start();
            	
            	running = false;
                

                if (player.getScore() > userList.getCurrentUser().getScore()) {
                    userList.getCurrentUser().setScore(player.getScore());
                    userList.updateUserScoreOnFile();
                }

                gameOverPanel = new GameOverPanel(window, userList);
                window.getMainPanel().add(gameOverPanel, "gameOver");

                window.getCardLayout().show(window.getMainPanel(), "gameOver");
                return;
            }

            frameCount++;
            long currentTime = System.nanoTime();
            if (currentTime - lastFPSTime >= 1000000000) {
                double elapsedSeconds = (currentTime - lastFPSTime) / 1000000000.0;
                double currentFPS = frameCount / elapsedSeconds;
                frameCount = 0;
                lastFPSTime = currentTime;
                setCurrentFPS(currentFPS); 
            }

            try {
                double remainingTime = nextDrawTime - System.nanoTime();
                remainingTime = remainingTime / 1000000;
                if (remainingTime < 0) {
                    remainingTime = 0;
                }

                Thread.sleep((long) remainingTime);
                nextDrawTime += drawInterval;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
    public void update() {
        
    	
    	player.update();
    	background.update();
    	
    	// Level Design 
    	
    	if(currentTime == 37 && aliensList.size() == 19) {
            aliensList.clear();
            createAliens2();
    	}
    	
    	if(currentTime == 74 && aliensList.size() == 34) {
            aliensList.clear();
            createAliens3();
    	}
    	
    	
    	if(!aliensList.isEmpty()) {
        	for ( Aliens alien : aliensList) {
        		alien.update();
        	}
    	}

    	
    	
    	// Alien - PlayerBullet Collision
    	ArrayList<Bullet> bullets =  Player.getBullets();
    	for ( Bullet bullet : bullets) {
    		for(Aliens alien1 : aliensList) {
    			if(bullet.getHitBox()!=null) {
    				if(alien1.getHitBox() != null) {
            			if(bullet.getHitBox().intersects(alien1.getHitBox())) {
                			alien1.decrementAlienHp();
                			if( alien1.getAlienHp() == 0) {
                				alien1.setDying(true);
                                aliendeath.setFramePosition(0);
                                aliendeath.start();
                			}
                			bullet.setVisible(false);
                			player.incrementScore();
                            alienhitted.setFramePosition(0);
                            alienhitted.start();
                		}
            		}
    			}
    		}		
    	}
    	
    	
    	
    	
    	// AlienBullet - Player Collision 
    	ArrayList<AlienBullet> aliensBullets = new ArrayList<AlienBullet>();
    	
    	 for ( Aliens alien : aliensList) {
      		for(int j=0 ; j<alien.getAliensBullets().size() ; j++) {
      			if(!aliensBullets.contains(alien.getAliensBullets().get(j))) {
      				aliensBullets.add(alien.getAliensBullets().get(j));
      			}
      		}
         } 

    	for (AlienBullet alienBullet : aliensBullets) {
    	    if (alienBullet.getHitBox() != null) {
    	        if (alienBullet.getHitBox().intersects(player.getHitBox())) {
    	            alienBullet.setVisible(false);
    	            player.decrementHealth();
                    playerhitted.setFramePosition(0);
                    playerhitted.start();
    	        }
    	    }
    	}
    	
    	//AlienBomb - Player Collision 
    	ArrayList<AlienBomb> aliensBombs = new ArrayList<AlienBomb>();
    	
   	 	for ( Aliens alien : aliensList) {
   	 			for(int j=0 ; j<alien.getAliensBombs().size() ; j++) {
   	 				if(!aliensBombs.contains(alien.getAliensBombs().get(j))) {
   	 					aliensBombs.add(alien.getAliensBombs().get(j));
   	 				}
   	 			}
   	 	} 

   	 	for (AlienBomb alienBomb : aliensBombs) {
   	 		if (alienBomb.getHitBox() != null) {
   	 			if (alienBomb.getHitBox().intersects(player.getHitBox())) {
   	 				alienBomb.setVisible(false);
   	 				player.decrementHealth();
                    playerhitted.setFramePosition(0);
                    playerhitted.start();
   	 			}
   	 		}
   	 	}
    	

    	
        
    	
   
    	
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D) g;
        
        background.draw(g2);
   
        player.draw(g2);
        
        
        
        // Alien Drawing
        
        if(!aliensList.isEmpty()) {
            for ( Aliens alien : aliensList) {
        		alien.draw(g2);
            } 
        }

        
        
       
        // PlayerBullet Drawing
        ArrayList<Bullet> bullets =  Player.getBullets();
        
        for (int w = 0 ; w<bullets.size() ; w++) {
        	Bullet m = (Bullet) bullets.get(w);
        	m.draw(g2);
        	m.update();
        }
        

        
        // AlienBullet Drawing
        ArrayList<AlienBullet> aliensbullets = new ArrayList<AlienBullet>(); 
        
        for ( Aliens alien : aliensList) {
    		for(int j=0 ; j<alien.getAliensBullets().size() ; j++) {
    			if(!aliensbullets.contains(alien.getAliensBullets().get(j))) {
    				aliensbullets.add(alien.getAliensBullets().get(j));
    			}
    		}
        } 
        for(int w = 0 ; w<aliensbullets.size() ; w++) {
        	AlienBullet k = (AlienBullet) aliensbullets.get(w);
        	k.draw(g2);
        	k.update();
        }
        
        //AlienBomb Drawing
        ArrayList<AlienBomb> aliensbombs = new ArrayList<AlienBomb>(); 
        
        for ( Aliens alien : aliensList) {
    		for(int j=0 ; j<alien.getAliensBombs().size() ; j++) {
    			if(!aliensbombs.contains(alien.getAliensBombs().get(j))) {
    				aliensbombs.add(alien.getAliensBombs().get(j));
    			}
    		}
        } 
        for(int w = 0 ; w<aliensbombs.size() ; w++) {
        	AlienBomb k = (AlienBomb) aliensbombs.get(w);
        	k.draw(g2);
        	k.update();
        }
        
        //Displaying number of lives
        BufferedImage hearthImage = null ;
        try {
			hearthImage = ImageIO.read(getClass().getResourceAsStream("/GamePanel/hearth.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
        g2.drawImage(hearthImage, 240, 5, 20, 20,null);
        String numberOfLivesText = "x" + player.getHealth();
        g2.setFont(new Font("Calibri", Font.PLAIN, 20));
        g2.setColor(Color.RED);
        g2.drawString(numberOfLivesText,260,20);
        
        
        
        // Displaying Score
        String scoreText = "" + player.getScore();
        g2.setFont(new Font("Calibri", Font.PLAIN, 20));
        g2.setColor(Color.RED);
        g2.drawString(scoreText, 380, 20);
        
        //Displaying Level Counter
        String progress_count_Text = window.getSelectedLevel()+" : %" + getProgress_count();
        g2.setFont(new Font("Calibri", Font.PLAIN, 20));
        g2.setColor(Color.RED);
        g2.drawString(progress_count_Text, 70, 20);
        
      //Displaying Level Counter
        String fps_Text = (int) getCurrentFPS() + " FPS";
        g2.setFont(new Font("Calibri", Font.PLAIN, 20));
        g2.setColor(Color.RED);
        g2.drawString(fps_Text, 600, 20);
       
        
        
        g2.dispose();
       
        
    }
    
    
    public void createAliens1() {
    	aliensList.clear();
        for( int i = 0 ; i<6 ; i++) {
        	Aliens tempAlien = new Aliens( i*100-100   , i*100-200 , 100 , 100 ,1 , 1 ,1,k);
        	aliensList.add(i, tempAlien);
        }
        for(int i = 6 ; i<12 ; i++) {
        	Aliens tempAlien = new Aliens( i*100-600   , i*100-1500 , 100 , 100 , 1 , 1 ,1,k);
        	aliensList.add(i , tempAlien);
        }
        for(int i = 12 ; i<16 ; i++) {
        	Aliens tempAlien = new Aliens( i*100-900   , i*100-2500 , 100 , 100 , 1 , 1 ,1,k);
        	aliensList.add(i , tempAlien);
        }
        for(int i = 16 ; i<19 ; i++) {
        	Aliens tempAlien = new Aliens( i*125-1500   , -1300 , 100 , 100 , 1 , 1 ,1,k);
        	aliensList.add(i , tempAlien);
        }
    }
    
    
  
    
    public void createAliens2() {
        for( int i = 0 ; i<8 ; i++) {
        	Aliens tempAlien = new Aliens( i*100   , -100 , 100 , 100 , 0 , 1 ,2,l);
        	aliensList.add(i, tempAlien);
        }
        Aliens bombAlien1 = new Aliens( 50   , -300 , 100 , 100 , 0 , 1 , 3,m);
        aliensList.add(8,bombAlien1);
        Aliens bombAlien2 = new Aliens( 500  , -400 , 100 , 100 , 0 , 1 , 3,m);
        aliensList.add(9,bombAlien2);
        for( int i= 10 ; i<14 ; i++ ) {
        	Aliens tempAlien = new Aliens( (i-7)*100   , -500 , 100 , 100 , 0 , 1 ,2,l);
        	aliensList.add(i, tempAlien);
        }
        Aliens bombAlien3 = new Aliens( 50  , -600 , 100 , 100 , 0 , 1 , 3,m);
        aliensList.add(14,bombAlien3);
        Aliens bombAlien4 = new Aliens( 400  , -800 , 100 , 100 , 0 , 1 , 3,m);
        aliensList.add(15,bombAlien4);
        Aliens bombAlien5 = new Aliens( 650  , -1000 , 100 , 100 , 0 , 1 , 3,m);
        aliensList.add(16,bombAlien5);
        Aliens bombAlien6 = new Aliens( 100  , -1200 , 100 , 100 , 0 , 1 , 3,m);
        aliensList.add(17,bombAlien6);
        
        
        for(int i = 18 ; i<23 ; i++) {
        	Aliens tempAlien = new Aliens( (i-17)*100   , -1000 , 100 , 100 , 0 , 1 ,2,l);
        	aliensList.add(i, tempAlien);
        }
        
        for(int i = 23 ; i<26 ; i++) {
        	Aliens tempAlien = new Aliens( (i-23)*100   , -800 , 100 , 100 , 0 , 1 ,2,l);
        	aliensList.add(i, tempAlien);
        }
        
        for(int i = 26 ; i<34 ; i++) {
        	Aliens tempAlien = new Aliens( (i-26)*100   , -1300 , 100 , 100 , 0 , 1 ,2,l);
        	aliensList.add(i, tempAlien);
        }
        
		
    }
    
    public void createAliens3() {
        for( int i = 0 ; i<6 ; i++) {
        	if( i==4 || i == 1) {
        		Aliens tempAlien = new Aliens( i*100+100   , -600+(i*100) , 100 , 100 , 0 , 1 ,1,k);
            	aliensList.add(i, tempAlien);
        	}else {
        		Aliens tempAlien = new Aliens( i*100+100   , -600+(i*100) , 100 , 100 , 0 , 1 ,2,l);
            	aliensList.add(i, tempAlien);
        	}
        	
        }
        for( int i = 6 ; i<12 ; i++) {
        	Aliens tempAlien = new Aliens( (i-6)*100+100   , -100+((i-6)*-100) , 100 , 100 , 0 , 1 ,2,l);
        	aliensList.add(i, tempAlien);
        }
        Aliens bombAlien0 = new Aliens( 100  , -350 , 100 , 100 , 0 , 1 , 3,m);
        aliensList.add(12,bombAlien0);
        Aliens bombAlien1 = new Aliens( 600  , -350 , 100 , 100 , 0 , 1 , 3,m);
        aliensList.add(13,bombAlien1);
        Aliens bombAlien2 = new Aliens( 350  , -600 , 100 , 100 , 0 , 1 , 3,m);
        aliensList.add(14,bombAlien2);
        Aliens bombAlien3 = new Aliens( 350  , -200 , 100 , 100 , 0 , 1 , 3,m);
        aliensList.add(15,bombAlien3);
        Aliens shooteralien1 = new Aliens( 0  , -600 , 100 , 100 , 0 , 1 , 1,k);
        aliensList.add(16,shooteralien1);
        Aliens shooteralien2 = new Aliens( 700  , -600 , 100 , 100 , 0 , 1 , 1,k);
        aliensList.add(17,shooteralien2);
        Aliens shooteralien3 = new Aliens( 0  , -100 , 100 , 100 , 0 , 1 , 1,k);
        aliensList.add(18,shooteralien3);
        Aliens shooteralien4 = new Aliens( 700  , -100 , 100 , 100 , 0 , 1 , 1,k);
        aliensList.add(19,shooteralien4);
    	
    }
    
    public void LoadLevels() {
    	if(window.getSelectedLevel() == "Level 1") {
    		k=5 ;
    		l=9 ;
    		m=7 ;
    	}
    	if(window.getSelectedLevel() == "Level 2") {
    		k=6 ;
    		l=10;
    		m=8;
    		
    	}
    	if(window.getSelectedLevel() == "Level 3") {
    		k=8;
    		l=12;
    		m=10;
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
    


    private void loadMusics() {
        try {
            InputStream audioStream = getClass().getResourceAsStream("/Musics/gamescreen.wav");
            BufferedInputStream bufferedStream = new BufferedInputStream(audioStream);
            audioInputStream = AudioSystem.getAudioInputStream(bufferedStream);
            musicClip = AudioSystem.getClip();
            musicClip.open(audioInputStream);
            FloatControl volumeControl = (FloatControl) musicClip.getControl(FloatControl.Type.MASTER_GAIN);
            float volume = -10.0f; 
            volumeControl.setValue(volume);
        } catch (IOException | UnsupportedAudioFileException | LineUnavailableException e) {
            e.printStackTrace();
        }
        
        startMusic();
        
	    try {
	        InputStream audioStream = getClass().getResourceAsStream("/Musics/gameover.wav");
	        BufferedInputStream bufferedStream = new BufferedInputStream(audioStream);
	        AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(bufferedStream);
	        gameOver = AudioSystem.getClip();
	        gameOver.open(audioInputStream);
            FloatControl volumeControl = (FloatControl) gameOver.getControl(FloatControl.Type.MASTER_GAIN);
            float volume = 0.0f; 
            volumeControl.setValue(volume);
	    } catch (IOException | UnsupportedAudioFileException | LineUnavailableException e) {
	        e.printStackTrace();
	    }
	    
	    
	    try {
	        InputStream audioStream = getClass().getResourceAsStream("/Musics/gamewin.wav");
	        BufferedInputStream bufferedStream = new BufferedInputStream(audioStream);
	        AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(bufferedStream);
	        gameWin = AudioSystem.getClip();
	        gameWin.open(audioInputStream);
            FloatControl volumeControl = (FloatControl) gameWin.getControl(FloatControl.Type.MASTER_GAIN);
            float volume = 0.0f; 
            volumeControl.setValue(volume);
	    } catch (IOException | UnsupportedAudioFileException | LineUnavailableException e) {
	        e.printStackTrace();
	    }
	    
	    
	    try {
	        InputStream audioStream = getClass().getResourceAsStream("/Musics/enemydeath.wav");
	        BufferedInputStream bufferedStream = new BufferedInputStream(audioStream);
	        AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(bufferedStream);
	        aliendeath = AudioSystem.getClip();
	        aliendeath.open(audioInputStream);
            FloatControl volumeControl = (FloatControl) aliendeath.getControl(FloatControl.Type.MASTER_GAIN);
            float volume = -10.0f; 
            volumeControl.setValue(volume);
	    } catch (IOException | UnsupportedAudioFileException | LineUnavailableException e) {
	        e.printStackTrace();
	    }
	    
	    try {
	        InputStream audioStream = getClass().getResourceAsStream("/Musics/enemyhitted.wav");
	        BufferedInputStream bufferedStream = new BufferedInputStream(audioStream);
	        AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(bufferedStream);
	        alienhitted = AudioSystem.getClip();
	        alienhitted.open(audioInputStream);
            FloatControl volumeControl = (FloatControl) alienhitted.getControl(FloatControl.Type.MASTER_GAIN);
            float volume = -10.0f; 
            volumeControl.setValue(volume);
	    } catch (IOException | UnsupportedAudioFileException | LineUnavailableException e) {
	        e.printStackTrace();
	    }
	    
	    try {
	        InputStream audioStream = getClass().getResourceAsStream("/Musics/playerhitted.wav");
	        BufferedInputStream bufferedStream = new BufferedInputStream(audioStream);
	        AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(bufferedStream);
	        playerhitted = AudioSystem.getClip();
	        playerhitted.open(audioInputStream);
            FloatControl volumeControl = (FloatControl) playerhitted.getControl(FloatControl.Type.MASTER_GAIN);
            float volume = -10.0f; 
            volumeControl.setValue(volume);
	    } catch (IOException | UnsupportedAudioFileException | LineUnavailableException e) {
	        e.printStackTrace();
	    }
	      
	    
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
    
  	
    public long getCurrentTime() {
		return currentTime;
	}


	public void setCurrentTime(long currentTime) {
		this.currentTime = currentTime;
	}


	public long getProgress_count() {
		return progress_count;
	}


	public void setProgress_count(int progress_count) {
		this.progress_count = progress_count;
	}


	public double getCurrentFPS() {
		return currentFPS;
	}


	public void setCurrentFPS(double currentFPS2) {
		this.currentFPS = currentFPS2;
	}




  
}
