package Main;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

public class GameBackground {
	private BufferedImage image;
	private int x;
	private int y;
	
	
	public GameBackground() {
		try {
			image = ImageIO.read(getClass().getResourceAsStream("/Background/backgorund.jpg"));
		
		} catch (IOException e) {
		    e.printStackTrace();
		}
	}
	
	
	public void update() {
        y += 1; 
	}
	
	public void draw(Graphics2D g2) {

	    g2.drawImage(image, x, ( y % 800) - 800, 800, 800, null);
	    g2.drawImage(image, x, y % 800, 800, 800, null);


	}


	public BufferedImage getImage() {
		return image;
	}


	public void setImage(BufferedImage image) {
		this.image = image;
	}


	public int getX() {
		return x;
	}


	public void setX(int x) {
		this.x = x;
	}


	public int getY() {
		return y;
	}


	public void setY(int y) {
		this.y = y;
	}



	
	
	
	
}
