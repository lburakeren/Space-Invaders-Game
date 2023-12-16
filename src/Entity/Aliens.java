package Entity;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import javax.imageio.ImageIO;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public class Aliens extends Entity {
	
	private int alienHp = 0 ;
	
	private static ArrayList<AlienBullet> AliensBullets ;
	private static ArrayList<AlienBomb> AliensBombs ;
	
    private ArrayList<BufferedImage> animationFrames;
    private int currentFrameIndex;
    private Timer animationTimer;
    private Random random;
	
	private int type  ;

    private Clip enemyShoot ;
    
	public Aliens(int x , int y , int width , int heigth , int speedX , int speedY , int type , int alienHp) {
		super(x,y,width,heigth);
		this.type = type ;
		this.speedX = speedX ;
		this.speedY = speedY ;
		this.alienHp = alienHp ;
		AliensBullets = new ArrayList<AlienBullet>();
		loadMusics();
		setAliensBombs(new ArrayList<AlienBomb>());
		loadAnimationFrames();
		random = new Random();
        startAnimationTimer();
	}




	public void loadAnimationFrames() {
		animationFrames = new ArrayList<>();
		try {
			
			if (type == 1) {
				BufferedImage frame1 = ImageIO.read(getClass().getResourceAsStream("/Alien1/alien1_1.png"));
				BufferedImage frame2 = ImageIO.read(getClass().getResourceAsStream("/Alien1/alien1_3.png"));
	            animationFrames.add(frame1);
	            animationFrames.add(frame2);
			}
			if (type == 2) {
				BufferedImage frame1 = ImageIO.read(getClass().getResourceAsStream("/Alien2/alien2_1.png"));
				BufferedImage frame2 = ImageIO.read(getClass().getResourceAsStream("/Alien2/alien2_2.png"));
				BufferedImage frame3 = ImageIO.read(getClass().getResourceAsStream("/Alien2/alien2_3.png"));
	            animationFrames.add(frame1);
	            animationFrames.add(frame2);
	            animationFrames.add(frame3);
			}
			if (type == 3) {
				BufferedImage frame1 = ImageIO.read(getClass().getResourceAsStream("/Alien3/alien3_1.png"));
				BufferedImage frame2 = ImageIO.read(getClass().getResourceAsStream("/Alien3/alien3_2.png"));
				BufferedImage frame3 = ImageIO.read(getClass().getResourceAsStream("/Alien3/alien3_3.png"));
	            animationFrames.add(frame1);
	            animationFrames.add(frame2);
	            animationFrames.add(frame3);
			}
		
		} catch(IOException e) {
			e.printStackTrace();
		}
	}
	
    public void startAnimationTimer() {
        animationTimer = new Timer();
        animationTimer.schedule(new TimerTask() {
            public void run() {
                currentFrameIndex = random.nextInt(animationFrames.size());
            }
        }, 0, 1000);
    }

    public void stopAnimationTimer() {
        animationTimer.cancel();
        animationTimer.purge();
    }
    
    
	public void update() {
		
		
		
		if(type == 1) {
			if(!isDying()) {
				image = animationFrames.get(currentFrameIndex);
				updateHitbox();
				if(x==700 && speedX > 0) {
					speedX = -speedX;
				}
				if(x==0 && speedX < 0) {
					speedX = -speedX ;
				}
				x += speedX ;
				y += speedY ;
				if(  (y % 150 == 0) || (y == 50 )  ) {
					if( y>0) {
						AlienBullet  alienbullet = new AlienBullet(x+45, y+100, 10, 30);
						AliensBullets.add(alienbullet);
			            enemyShoot.setFramePosition(0);
			            enemyShoot.start();
					}
				}
			}else{
				removeHitbox() ;
			}	
		}
		
		if(type == 2) {
			if(!isDying()) {
				image = animationFrames.get(currentFrameIndex);
				updateHitbox();
				y += speedY ;
			}else{
				removeHitbox() ;
			}	
		}
		
		if(type == 3) {
			if(!isDying()) {
				image = animationFrames.get(currentFrameIndex);
				updateHitbox();
				y += speedY ;
				if(  (y % 200 == 0)  || (y == 50) ) {
					if( y>0) {
						AlienBomb  alienbomb1 = new AlienBomb(x+100, y+50, 30, 30 , 1 , 1);
						AliensBombs.add(alienbomb1);
						AlienBomb  alienbomb2 = new AlienBomb(x, y+50, 30, 30 , -1 , 1);
						AliensBombs.add(alienbomb2);
						AlienBomb  alienbomb3 = new AlienBomb(x+100, y+100, 30, 30 , 1 , 2);
						AliensBombs.add(alienbomb3);
						AlienBomb  alienbomb4 = new AlienBomb(x, y+100, 30, 30 , -1 , 2);
						AliensBombs.add(alienbomb4);
						AlienBomb  alienbomb5 = new AlienBomb(x+50, y+100, 30, 30 , 0 , 2);
						AliensBombs.add(alienbomb5);
			            enemyShoot.setFramePosition(0);
			            enemyShoot.start();
					}
				}
			}else{
				removeHitbox() ;
			}	
		}
		
		
		

	}
	


	public void draw(Graphics2D g2) {
		
		if(!isDying()) {
			g2.drawImage(image, x , y , width, heigth , null);
			
		}
		
	}
	
	

	
	private void loadMusics() {
	    try {
	        InputStream audioStream = getClass().getResourceAsStream("/Musics/shoot.wav");
	        BufferedInputStream bufferedStream = new BufferedInputStream(audioStream);
	        AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(bufferedStream);
	        enemyShoot = AudioSystem.getClip();
	        enemyShoot.open(audioInputStream);
            FloatControl volumeControl = (FloatControl) enemyShoot.getControl(FloatControl.Type.MASTER_GAIN);
            float volume = -60.0f; 
            volumeControl.setValue(volume);
	    } catch (IOException | UnsupportedAudioFileException | LineUnavailableException e) {
	        e.printStackTrace();
	    }
		
	}
	
	public void decrementAlienHp() {
		alienHp--;
	}


	public ArrayList<AlienBullet> getAliensBullets() {
		return AliensBullets;
	}



	public static void setAliensBullets(ArrayList<AlienBullet> aliensBullets) {
		try {
			AliensBullets = aliensBullets;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


	public int getAlienHp() {
		return alienHp;
	}



	public void setAlienHp(int alienHp) {
		this.alienHp = alienHp;
	}



	public ArrayList<AlienBomb> getAliensBombs() {
		return AliensBombs;
	}



	public static void setAliensBombs(ArrayList<AlienBomb> aliensBombs) {
		AliensBombs = aliensBombs;
	}
	
	
	
}
