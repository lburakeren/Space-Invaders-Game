package Entity;

import java.awt.Graphics2D;
import java.io.IOException;

import javax.imageio.ImageIO;

public class AlienBomb extends Entity{
	
	private int live_time = 0 ;

	public AlienBomb(int x, int y, int width, int heigth , int speedX , int speedY ) {
		super(x, y, width, heigth);
		this.speedX = speedX ;
		this.speedY = speedY ;
		loadBombImage();
	}
	
	public void loadBombImage() {
		try {
			image = ImageIO.read(getClass().getResourceAsStream("/Alien1/BOMB.png"));
		} catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	public void update() {
		
		if(getLive_time() == 150) {
			this.setVisible(false);
		}
		
		if(isVisible()) {
			updateHitbox();
			y += speedY ;
			x += speedX ;
			live_time++ ;
		}else{
			removeHitbox() ;
		}
		
	}
	
	public void draw(Graphics2D g2) {

		if(isVisible()) {
			g2.drawImage(image, x , y , width , heigth , null);
		}
			
	}

	public int getLive_time() {
		return live_time;
	}

	public void setLive_time(int live_time) {
		this.live_time = live_time;
	}

}
