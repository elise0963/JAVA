package monster;

import java.awt.Rectangle;
import java.util.Random;

import entity.Entity;
import main.GamePanel;
import object.AlienProjectile;


public class Alien extends Entity {
	GamePanel gp;

	public Alien(GamePanel gp) {
		super(gp);

		this.gp = gp;
		type = 2;
		solidArea = new Rectangle();

		name = "Aliens";
		speed = 1;
		maxLife = 20;
		life = maxLife;
		attack = 2;
		defense = 0;
		exp = 2;
		projectile = new AlienProjectile(gp);

		solidArea.x = 16;
		solidArea.y = 32;
		solidAreaDefaultX = solidArea.x;
		solidAreaDefaultY = solidArea.y;
		solidArea.width = 14;
		solidArea.height = 14;

		getImage();
	}

	public void getImage() {

		up1 = setup("/monster/greenslime_up_1", gp.tileSize, gp.tileSize);
		up2 = setup("/monster/greenslime_up_2", gp.tileSize, gp.tileSize);
		down1 = setup("/monster/greenslime_down_1", gp.tileSize, gp.tileSize);
		down2 = setup("/monster/greenslime_down_2", gp.tileSize, gp.tileSize);
		left1 = setup("/monster/greenslime_left_1", gp.tileSize, gp.tileSize);
		left2 = setup("/monster/greenslime_left_2", gp.tileSize, gp.tileSize);
		right1 = setup("/monster/greenslime_right_1", gp.tileSize, gp.tileSize);
		right2 = setup("/monster/greenslime_right_2", gp.tileSize, gp.tileSize);
	}
	
	public void setAction() {
	    actionLockCounter++;

	    if (actionLockCounter == 120) {
	        // Calculate direction towards the player
	        int playerX = gp.player.worldX;
	        int playerY = gp.player.worldY;

	        int deltaX = playerX - worldX;
	        int deltaY = playerY - worldY;

	        if (Math.abs(deltaX) > Math.abs(deltaY)) {
	            // Move horizontally
	            direction = (deltaX > 0) ? "right" : "left";
	        } else {
	            // Move vertically
	            direction = (deltaY > 0) ? "down" : "up";
	        }

	        actionLockCounter = 0;
	    }

	    int i = new Random().nextInt(100) + 1;
	    if (i > 99 && projectile.alive == false && shotAvailableCounter == 30) {
	        projectile.set(worldX, worldY, direction, true, this);
	        gp.projectileList.add(projectile);
	        shotAvailableCounter = 0;
	    }
	}
	
	
	
	public void damageReaction() {
		actionLockCounter= 0;
		direction = gp.player.direction;
	}
	
	

}
