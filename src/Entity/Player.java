package Entity;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import javax.imageio.ImageIO;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

import Main.GamePanel;
import Main.KeyHandler;

public class Player extends Entity{
	
	GamePanel gp ;
	KeyHandler keyH ;
	private int Score = 0;
	private int Health = 3 ;
	

	static ArrayList<Bullet> bullets ;
	
    private ArrayList<BufferedImage> animationFrames;
    private int currentFrameIndex;
    private Timer animationTimer;
    
    private Clip shootingSound;
	
	private boolean spaceTriggered = false;
	
	
	public Player(GamePanel gp , KeyHandler keyH , int x , int y , int width , int heigth) {
		super(x,y,width,heigth);
		this.gp = gp ;
		this.keyH = keyH;
		this.speedX = 5;
		this.speedY = 5 ;
		bullets = new ArrayList<Bullet>();
		loadMusics();
		addMouseListenerToGamePanel();
		loadAnimationFrames();
        startAnimationTimer();
	}	
	
	
    private void addMouseListenerToGamePanel() {
        MouseAdapter mouseAdapter = new MouseAdapter() {
            public void mouseMoved(MouseEvent e) {
                Player.this.setX(e.getX());
                Player.this.setY(e.getY());
            }
            public void mouseClicked(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON1) {
                    Bullet bullet = new Bullet(Player.this.getX() + 30, Player.this.getY() - 10, 10, 30);
                    bullets.add(bullet);
                    shootingSound.setFramePosition(0);
                    shootingSound.start();
                }
            }
        };

        gp.addMouseMotionListener(mouseAdapter);
        gp.addMouseListener(mouseAdapter);
        gp.setCursor(gp.getToolkit().createCustomCursor(
                new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB),
                new Point(),
                "hidden"));
		
	}


	public void loadAnimationFrames() {
        animationFrames = new ArrayList<>();
        try {
            BufferedImage frame1 = ImageIO.read(getClass().getResourceAsStream("/Player/ship2_1.png"));
            BufferedImage frame2 = ImageIO.read(getClass().getResourceAsStream("/Player/ship2_2.png"));
            BufferedImage frame3 = ImageIO.read(getClass().getResourceAsStream("/Player/ship2_3.png"));
            BufferedImage frame4 = ImageIO.read(getClass().getResourceAsStream("/Player/ship2_4.png"));
            animationFrames.add(frame1);
            animationFrames.add(frame2);
            animationFrames.add(frame3);
            animationFrames.add(frame4);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void startAnimationTimer() {
        animationTimer = new Timer();
        animationTimer.schedule(new TimerTask() {
            public void run() {
                currentFrameIndex++;
                if (currentFrameIndex >= animationFrames.size()) {
                    currentFrameIndex = 0;
                }
            }
        }, 0, 40); 
    }

    public void stopAnimationTimer() {
        animationTimer.cancel();
        animationTimer.purge();
    }

	
    public void update() {
    	
		updatePlayerHitbox();

    	if ( keyH.isUpPressed() &&  y>0 ) {
    		y -= speedY ;
    	}
    	if ( keyH.isDownPressed() && y<670) {
    		y += speedY ;
    	}
       	if ( keyH.isLeftPressed() && x>0) {
    		x -= speedX ;
    	}
       	if ( keyH.isRightPressed() && x<710) {
    		x += speedX ;
    	}
       	if (keyH.isSpacePressed() && !spaceTriggered) {
            Bullet bullet = new Bullet(x + 30, y-10, 10 , 30 );
            bullets.add(bullet);
            spaceTriggered = true;
            shootingSound.setFramePosition(0);
            shootingSound.start();
        } else if (!keyH.isSpacePressed()) {
            spaceTriggered = false;
        }
    	
    }
    
    public void draw(Graphics2D g2) {
        
    	image = animationFrames.get(currentFrameIndex);
    	g2.drawImage(image, x , y , width , heigth, null );
    	
    }
    
    public void loadMusics() {
    	
	    try {
	        InputStream audioStream = getClass().getResourceAsStream("/Musics/shoot.wav");
	        BufferedInputStream bufferedStream = new BufferedInputStream(audioStream);
	        AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(bufferedStream);
	        shootingSound = AudioSystem.getClip();
	        shootingSound.open(audioInputStream);
            FloatControl volumeControl = (FloatControl) shootingSound.getControl(FloatControl.Type.MASTER_GAIN);
            float volume = -60.0f; 
            volumeControl.setValue(volume);
	    } catch (IOException | UnsupportedAudioFileException | LineUnavailableException e) {
	        e.printStackTrace();
	    }
    	
    }
    
    public void incrementScore() {
    	Score+=10 ;
    }
    
    public void decrementHealth() {
    	Health-- ;
    }
    
    
	public static ArrayList<Bullet> getBullets() {
		return bullets;
	}

	public static void setBullets(ArrayList<Bullet> bullets) {
		Player.bullets = bullets;
	}

    
	
	public int getScore() {
		return Score;
	}



	public void setScore(int score) {
		Score = score;
	}



	public int getHealth() {
		return Health;
	}



	public void setHealth(int health) {
		Health = health;
	}



	public boolean isSpaceTriggered() {
		return spaceTriggered;
	}



	public void setSpaceTriggered(boolean spaceTriggered) {
		this.spaceTriggered = spaceTriggered;
	}
    
    
	
}                