package entity;
//cette classe content les variables qui serot utilisées pour le joueur, les mobstres et les classes NPC ( non player character)

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;

import main.GamePanel;
import main.UtilityTool;

public abstract class Entity {

	GamePanel gp;
	// STATE
	public int worldX, worldY;
	public String direction = "down";
	int dialogueIndex = 0;
	public int spriteNum = 1;
	public boolean collision = false;
	public boolean invincible = false;
	public boolean collisionOn = false;
	public boolean attacking = false;
	public boolean alive = true;
	public boolean dying = false;
	public boolean hpBarOn=false;
	public boolean onPath = false;
	// BufferedImage est utilisé pour contenir les fichiers images
	public BufferedImage up1, up2, down1, down2, left1, left2, right1, right2;

	// images pour l'attaque
	BufferedImage attackUp1, attackUp2, attackUp3, attackUp4, attackDown1, attackDown2, attackDown3, attackDown4,
			attackLeft1, attackLeft2, attackLeft3, attackLeft4, attackRight1, attackRight2, attackRight3, attackRight4;
	public Rectangle solidArea = new Rectangle(16, 32, 14, 14);
	public Rectangle attackArea = new Rectangle(16, 32, 14, 14);
	public int solidAreaDefaultX, solidAreaDefaultY;
	public String dialogues[] = new String[20];
	public BufferedImage image, image2, image3;
	public int type; // 0 = player, 1=npc, 2 = monster
	// COUNTER
	public int actionLockCounter = 0;
	public int invincibleCounter = 0;
	public int spriteCounter = 0;
	public int standCounter =0;
	public int dyingCounter = 0;
	public int hpBarCounter = 0;
	public int shotAvailableCounter = 0;
	// CHARACTER HEALTH STATUS attributes
	public String name;
	public int maxLife;
	public int life;
	public int speed;
	public int attack;
	public int maxMana;
	public int mana;
	public int defense;
	public int strength;
	public int dexterity;
	public int level;
	public int nextLevelExp;
	public int exp;
	public Projectile projectile;
	public int coin;
	public Entity currentWeapon;
	public Entity currentShield;
	
	//ITEM ATTRIBUTES
	public int attackValue;
	public int defenseValue;
	public int useCost;

	public Entity(GamePanel gp) {
		this.gp = gp;
	}

	public void setWorldPosition(int x, int y) {
		this.worldX = x;
		this.worldY = y;
	}

	public int getWorldX() {
		return worldX;
	}

	public int getWorldY() {
		return worldY;
	}

	public void setAction() {
	}
	public void damageReaction() {
		
	}
	
	public void speak() {
		if (dialogues[dialogueIndex] == null) {
			dialogueIndex = 0;
		}
		gp.ui.currentDialogue = dialogues[dialogueIndex];
		dialogueIndex++;

		switch (gp.player.direction) {
		case "up":
			direction = "down";
			break;
		case "down":
			direction = "up";
			break;
		case "left":
			direction = "right";
			break;
		case "right":
			direction = "left";
			break;

		}
	}

	public void checkCollision() {
		collisionOn = false;
		gp.cChecker.checkTile(this);
		gp.cChecker.checkObject(this, false);
		gp.cChecker.checkEntity(this, gp.npc);
		gp.cChecker.checkEntity(this, gp.monster);
		boolean contactPlayer = gp.cChecker.checkPlayer(this);
		
		if (this.type == 2 && contactPlayer == true) {
			
			damagePlayer(attack);
		}
	}
	
	public void update() {
		setAction();
		checkCollision();
		
		// IFCOLLISION IS FALSE NPC CAN MOVE
		if (collisionOn == false) {
			switch (direction) {
				case "up":worldY -= speed;break;
				case "down":worldY += speed;break;
				case "left":worldX -= speed;break;
				case "right":worldX += speed;break;
			}
		}
		spriteCounter++;
		if (spriteCounter > 12) {
			if (spriteNum == 1) {
				spriteNum = 2;
			} else if (spriteNum == 2) {
				spriteNum = 1;
			}
			spriteCounter = 0;
		}
		// this need to be outside of key if statement
		if (invincible == true) {
			invincibleCounter++;
			if (invincibleCounter > 40) {
				invincible = false;
				invincibleCounter = 0;
			}
		}
		if (shotAvailableCounter < 30) {
			shotAvailableCounter++;
		}
	}

	public BufferedImage setup(String imagePath, int width, int height) {

		UtilityTool uTool = new UtilityTool();
		BufferedImage image = null;

		try {
			image = ImageIO.read(getClass().getResourceAsStream(imagePath + ".png"));
			image = uTool.scaleImage(image, width, height);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return image;
	}
	public void damagePlayer(int attack) {
		
		if (gp.player.invincible == false) {
			// we can give damage
			gp.playSE(6);
			int damage = attack -gp.player.defense;
			if (damage < 0) {
				damage = 0;
			}
			gp.player.life -= damage;
			gp.player.invincible = true;
		}
	}
	public void draw(Graphics2D g2) {
		int screenX = worldX - gp.player.worldX + gp.player.screenX;
		int screenY = worldY - gp.player.worldY + gp.player.screenY;
		BufferedImage image = null;

		if (worldX + gp.tileSize > gp.player.worldX - gp.player.screenX
				&& worldX - gp.tileSize < gp.player.worldX + gp.player.screenX
				&& worldY + gp.tileSize > gp.player.worldY - gp.player.screenY
				&& worldY - gp.tileSize < gp.player.worldY + gp.player.screenY) {

			switch (direction) {
			case "up":
				if (spriteNum == 1) {image = up1;}
				if (spriteNum == 2) {image = up2;}
				break;
			case "down":
				if (spriteNum == 1) {image = down1;}
				if (spriteNum == 2) {image = down2;}
				break;
			case "left":
				if (spriteNum == 1) {image = left1;}
				if (spriteNum == 2) {image = left2;}
				break;
			case "right":
				if (spriteNum == 1) {image = right1;}
				if (spriteNum == 2) {image = right2;}
				break;
			}
			//MONSTER HP BAR
			if(type ==2  && hpBarOn == true) {
				
				double oneScale = (double)gp.tileSize/maxLife;
				double hpBarvalue = oneScale*life;
				
				g2.setColor(new Color(35,35,35));
				g2.fillRect(screenX-1, screenY - 16, gp.tileSize+2, 12);
				g2.setColor(new Color(255,0,30));
				//gp.monster[monsterIndex].
				g2.fillRect(screenX, screenY - 15, (int)hpBarvalue, 10);
				
				hpBarCounter++;
				
				if (hpBarCounter > 600) {
					hpBarCounter = 0;
					hpBarOn = false;
				}
			}
			if (invincible == true) {
				hpBarOn=true;
				hpBarCounter = 0;
				changeAlpha(g2, 0.4f);
			}
			if (dying == true) {
				dyingAnimation(g2);
			}

			g2.drawImage(image, screenX, screenY, gp.tileSize, gp.tileSize, null);
			changeAlpha(g2, 1f);
		}
	}
	public void changeAlpha(Graphics2D g2, float alphavalue) {
		g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alphavalue));
	}
	public void dyingAnimation(Graphics2D g2) {
		int i = 5;
		
		if (dyingCounter <= i) {changeAlpha(g2, 0f);}
		if (dyingCounter > i && dyingCounter <= i*2) {changeAlpha(g2, 1f);}
		if (dyingCounter > i*2 && dyingCounter <= i*3) {changeAlpha(g2, 0f);}
		if (dyingCounter > i*3 && dyingCounter <= i*4) {changeAlpha(g2, 1f);}
		if (dyingCounter > i*4 && dyingCounter <= i*5) {changeAlpha(g2, 0f);}
		if (dyingCounter > i*5 && dyingCounter <= i*6) {changeAlpha(g2, 1f);}
		if (dyingCounter > i*6 && dyingCounter <= i*7) {changeAlpha(g2, 0f);}
		if (dyingCounter > i*7 && dyingCounter <= i*8) {changeAlpha(g2, 1f);}
		if (dyingCounter > i*8) {
			dying = false;
			alive = false;
		}
	}
	

}
