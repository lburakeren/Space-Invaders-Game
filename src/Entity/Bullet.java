package Entity;

import java.awt.Graphics2D;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Bullet extends Entity {
	


	public Bullet(int x , int y , int width , int heigth) {
		super(x,y,width,heigth);
		this.speedX = 0;
		this.speedY = 10 ;
		loadBulletImage();
	}
	
	
	
	public void loadBulletImage() {
		try {
			image = ImageIO.read(getClass().getResourceAsStream("/Player/bullet.png"));
		} catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	public void update() {
		
		
		if(isVisible()) {
			updateHitbox();
			y -= speedY ;
		}else{
			removeHitbox() ;
		}
		
	}
	
	public void draw(Graphics2D g2) {
		
		if(isVisible()) {
			g2.drawImage(image, x , y , width , heigth , null);
		}
			
	}
	
}
