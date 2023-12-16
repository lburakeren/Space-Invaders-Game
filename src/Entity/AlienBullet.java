package Entity;

import java.awt.Graphics2D;
import java.io.IOException;
import javax.imageio.ImageIO;

public class AlienBullet extends Entity{
	

	public AlienBullet(int x , int y , int width , int heigth) {
		super(x,y,width,heigth);
		this.speedX = 3;
		this.speedY = 3;  
		loadBulletImage();
	}
	
	
	
	public void loadBulletImage() {
		try {
			image = ImageIO.read(getClass().getResourceAsStream("/Alien1/alienbullet.png"));
		} catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	public void update() {
		
		
		if(isVisible()) {
			updateHitbox();
			y += speedY ;
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
