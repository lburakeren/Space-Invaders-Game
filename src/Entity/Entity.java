package Entity;

import java.awt.Rectangle;
import java.awt.image.BufferedImage;

public abstract class Entity {
	
	protected int x , y ;
	protected int width , heigth ;
	protected int speedX;
	protected int speedY;
	
	protected boolean visible ;
	protected boolean dying ;
	
	protected Rectangle hitBox ;
	
	public BufferedImage image;
	
	public Entity(int x , int y , int width , int heigth ) {
		this.x = x ;
		this.y = y ;
		this.width = width ;
		this.heigth = heigth ;
		visible = true ;
		dying = false ;
		initHitbox();
	}
	

	private void initHitbox() {
		hitBox = new Rectangle(x , y , width , heigth);
	}
	
	protected void updateHitbox() {
		hitBox.x = x ;
		hitBox.y = y ;
	}
	
	protected void updatePlayerHitbox() {
		hitBox.x = x ;
		hitBox.y = y+20 ;
		hitBox.height = heigth -40 ;
	}
	
	protected void removeHitbox() {
	    hitBox = null ;
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


	public int getWidth() {
		return width;
	}


	public void setWidth(int width) {
		this.width = width;
	}


	public int getHeigth() {
		return heigth;
	}


	public void setHeigth(int heigth) {
		this.heigth = heigth;
	}



	public int getSpeedX() {
		return speedX;
	}


	public void setSpeedX(int speedX) {
		this.speedX = speedX;
	}


	public int getSpeedY() {
		return speedY;
	}


	public void setSpeedY(int speedY) {
		this.speedY = speedY;
	}


	public boolean isVisible() {
		return visible;
	}


	public void setVisible(boolean visible) {
		this.visible = visible;
	}


	public boolean isDying() {
		return dying;
	}


	public void setDying(boolean dying) {
		this.dying = dying;
	}


	public Rectangle getHitBox() {
		return hitBox;
	}


	
	

}
