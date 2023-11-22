package entity;

import java.awt.AlphaComposite;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import main.GamePanel;
import main.KeyHandler;
import object.Coin;
import object.Fireball;
import object.ShieldWood;
import object.SwordNormal;

public class Player extends Entity {

	KeyHandler keyH;

	public final int screenX;
	public final int screenY;

	public int hasCoin = 0;

	public Player(GamePanel gp, KeyHandler keyH) {
		super(gp);

		this.keyH = keyH;

		screenX = gp.screenWidth / 2 - (gp.tileSize / 2);
		screenY = gp.screenHeight / 2 - (gp.tileSize / 2);

		solidArea = new Rectangle();

		solidArea.x = 16;
		solidArea.y = 32;
		solidAreaDefaultX = solidArea.x;
		solidAreaDefaultY = solidArea.y;
		solidArea.width = 14;
		solidArea.height = 14;

		attackArea.width = 36;
		attackArea.height = 36;

		setDefaultValues();
		getPlayerImage();
		getPlayerAttackImage();
	}

	public void setDefaultPositions() {
		worldX = gp.tileSize * 23;
		worldY = gp.tileSize * 21;
		direction = "down";
	}

	public void restoreLifeAndMan() {
		life = maxLife;
		// mana = maxMana;
		invincible = true;
	}

	// position par defaut du joueur
	public void setDefaultValues() {
		worldX = gp.tileSize * 23;
		worldY = gp.tileSize * 21;
		speed = 4;
		direction = "down";

		// player status
		level = 1;
		maxLife = 6; // 3 coeurs == vies --> 1 coeur = 3 vies
		life = maxLife;
		maxMana= 4;
		mana = maxMana;
		strength = 1;
		dexterity = 1;
		exp = 0;
		nextLevelExp = 5;
		coin = 0;
		currentWeapon = new SwordNormal(gp);
		currentShield = new ShieldWood(gp);
		projectile = new Fireball(gp);
		attack = getAttack();
		defense = getDefense();

	}

	public int getAttack() {
		return attack = strength * currentWeapon.attackValue;
	}

	public int getDefense() {
		return defense = dexterity * currentShield.defenseValue;
	}

	public void getPlayerImage() {

		up1 = setup("/player/player_walk_up_1", gp.tileSize, gp.tileSize);
		up2 = setup("/player/player_walk_up_2", gp.tileSize, gp.tileSize);
		down1 = setup("/player/player_walk_down_1", gp.tileSize, gp.tileSize);
		down2 = setup("/player/player_walk_down_2", gp.tileSize, gp.tileSize);
		left1 = setup("/player/player_walk_left_1", gp.tileSize, gp.tileSize);
		left2 = setup("/player/player_walk_left_2", gp.tileSize, gp.tileSize);
		right1 = setup("/player/player_walk_right_1", gp.tileSize, gp.tileSize);
		right2 = setup("/player/player_walk_right_2", gp.tileSize, gp.tileSize);
	}

	public void getPlayerAttackImage() {

		attackUp1 = setup("/player/player_attack_up_1", gp.tileSize, gp.tileSize);
		attackUp2 = setup("/player/player_attack_up_2", gp.tileSize, gp.tileSize);
		
		attackDown1 = setup("/player/player_attack_down_1", gp.tileSize, gp.tileSize);
		attackDown2 = setup("/player/player_attack_down_2", gp.tileSize, gp.tileSize);
	
		attackLeft1 = setup("/player/player_attack_left_1", gp.tileSize, gp.tileSize);
		attackLeft2 = setup("/player/player_attack_left_2", gp.tileSize, gp.tileSize);
	
		attackRight1 = setup("/player/player_attack_right_1", gp.tileSize, gp.tileSize);
		attackRight2 = setup("/player/player_attack_right_2", gp.tileSize, gp.tileSize);
		
	}

	public void update() {

		if (attacking == true) {
			attacking();
		}
		if (keyH.upPressed == true || keyH.downPressed == true || keyH.leftPressed == true || keyH.rightPressed == true
				|| keyH.enterPressed == true || keyH.attackKeyPressed == true) {
			if (keyH.upPressed == true) {
				direction = "up";
				// worldY -= speed;
			} else if (keyH.downPressed == true) {
				direction = "down";
				// worldY += speed;
			} else if (keyH.leftPressed == true) {
				direction = "left";
				// worldX -= speed;
			} else if (keyH.rightPressed == true) {
				direction = "right";
				// worldX += speed;
			}
			// CHECK TILE COLLISION
			collisionOn = false;
			gp.cChecker.checkTile(this);

			// CHECK OBJECT COLLISION
			int objIndex = gp.cChecker.checkObject(this, true);
			pickUpObject(objIndex);

			// CHECK NPC COLLISION
			int npcIndex = gp.cChecker.checkEntity(this, gp.npc);
			interactNPC(npcIndex);

			// CHECK MONSTER COLLISION
			int monsterIndex = gp.cChecker.checkEntity(this, gp.monster);
			contactMonster(monsterIndex);

			// CHECK EVENT
			gp.eHandler.checkEvent();

			// IF COLLISION IS FALSE PLAYER CAN MOVE
			if (collisionOn == false && keyH.enterPressed == false) {
				switch (direction) {
				case "up":
					worldY -= speed;
					break;
				case "down":
					worldY += speed;
					break;
				case "left":
					worldX -= speed;
					break;
				case "right":
					worldX += speed;
					break;
				}
			}
			gp.keyH.enterPressed = false;
			keyH.attackKeyPressed = false;

			spriteCounter++;
			if (spriteCounter > 12) {
				if (spriteNum == 1) {
					spriteNum = 2;
				} else if (spriteNum == 2) {
					spriteNum = 1;
				}
				spriteCounter = 0;
			}

		} else {

			standCounter++;
			if (standCounter == 20) {
				spriteNum = 1;
				standCounter = 0;
			}
		}

		if (gp.keyH.shotKeyPressed == true && projectile.alive == false
				&& shotAvailableCounter == 30 && projectile.haveResource(this)==true) {
			// SET DEFAULT COORDINATE DIRECTION AND USER
			projectile.set(worldX, worldY, direction, true, this);
			
			//SUBTRACT THECOST MANA AMMO ETC
			projectile.subtractResource(this);
			// ADD IT TO THE LIST
			gp.projectileList.add(projectile);
			
			shotAvailableCounter = 0;
			// gp.playSE(8);
		}
		// this need to be outside of key if statement
		if (invincible == true) {
			invincibleCounter++;
			if (invincibleCounter > 60) {
				invincible = false;
				invincibleCounter = 0;
			}
		}

		if (shotAvailableCounter < 30) {
			shotAvailableCounter++;
		}

		if (life < 1) {

			gp.gameState = gp.gameOverState;
			gp.stopMusic();
			gp.playSE(9);
		}

	}

	public void contactMonster(int monsterIndex) {
		if (monsterIndex != 999) {
			if (invincible == false) {
				gp.playSE(6);
				int damage = gp.monster[monsterIndex].attack - defense;
				if (damage < 0) {
					damage = 0;
				}
				life -= damage;
				invincible = true;
			}

		}

	}

	public void interactNPC(int npcIndex) {

		if (gp.keyH.enterPressed == true) {
			if (npcIndex != 999) {
				if (life < maxLife) {
					gp.gameState = gp.dialogueState;
					gp.npc[npcIndex].speak();
					life += 1;
				}
			} else {
				// gp.playSE(7);
				attacking = true;
			}
		}
	}

	public void attacking() {
		spriteCounter++;
		if (spriteCounter <= 5) { // tester en modifiant pour les 4 images que tu voulais creer
			spriteNum = 1;
		}
		if (spriteCounter > 5 && spriteCounter <= 25) {
			spriteNum = 2;
			// save the current worldx, worldy and solid area
			int currentWorldX = worldX;
			int currentWorldY = worldY;
			int solidAreaWidth = solidArea.width;
			int solidAreaHeight = solidArea.height;
			// ajuster player's worldx and y for the attack area
			switch (direction) {
			case "up":
				worldY -= attackArea.height;
				break;
			case "down":
				worldY += attackArea.height;
				break;
			case "left":
				worldX -= attackArea.width;
				break;
			case "right":
				worldX += attackArea.width;
				break;
			}
			// attackarea becomes solidArea
			solidArea.width = attackArea.width;
			solidArea.height = attackArea.height;
			// Chec monster collision with the updated worldX, worldY and solid area
			int monsterIndex = gp.cChecker.checkEntity(this, gp.monster);
			damageMonster(monsterIndex, attack);
			// aafter checking collision, restore the original data
			worldX = currentWorldX;
			worldY = currentWorldY;
			solidArea.width = solidAreaWidth;
			solidArea.height = solidAreaHeight;

		}
		if (spriteCounter > 25) {
			spriteNum = 1;
			spriteCounter = 0;
			attacking = false;
		}
	}

	public void damageMonster(int i, int attack) {
		if (i != 999) {
			if (gp.monster[i].invincible == false) {
				gp.playSE(5);
				int damage = attack - gp.monster[i].defense;
				if (damage < 0) {
					damage = 0;
				}
				gp.monster[i].life -= damage;
				gp.ui.addMessage(damage + " damage");
				gp.monster[i].invincible = true;
				gp.monster[i].damageReaction();

				if (gp.monster[i].life <= 0) {
					gp.monster[i].dying = true;
					gp.ui.addMessage("Killed the " + gp.monster[i].name + "!");
					gp.ui.addMessage("Exp + " + gp.monster[i].exp + "!");
					exp += gp.monster[i].exp;
					hpBarOn = false;
					gp.obj[i] = new Coin(gp);
					gp.obj[i].worldX = gp.monster[i].worldX;
					gp.obj[i].worldY =  gp.monster[i].worldY;
					gp.monster[i] = null;

					checkLevelUp();
				}

			}
		} else {
			System.out.println("Miss");
		}
	}

	public void checkLevelUp() {
		if (exp >= nextLevelExp) {
			level++;
			nextLevelExp *= 2;
			maxLife += 2;
			strength++;
			dexterity++;
			maxMana++;
			mana = maxMana;
			attack = getAttack();
			defense = getDefense();

			// gp.playSE(8);//le numero du son pour passer les levels.
			gp.gameState = gp.dialogueState;
			gp.ui.currentDialogue = "You are level" + level + " ! Ennemies are stronger:\n You need to hit them more";
			gp.aSetter.setMonster();
			gp.aSetter.setObject();
			gp.aSetter.setNPC();
		}
	}

	public void pickUpObject(int i) {

		if (i != 999) {

			String objectName = gp.obj[i].name;

			switch (objectName) {
			
			case "Coin":
				gp.playSE(1);
				hasCoin++;
				gp.obj[i] = null;

				break;
			
			case "Boots":
				gp.playSE(2);
				speed += 2;
				gp.obj[i] = null;
				gp.ui.addMessage("Speed up");
				break;
				case "Diamond":
				gp.playSE(2);
				mana = maxMana;
				gp.ui.addMessage("Fireball reloaded");
				gp.obj[i] = null;
				break;
			case "Potion_red":
				if (gp.player.life == gp.player.maxLife) {
					// nothing to do
				} else {
					gp.playSE(1);
					gp.obj[i] = null;
					gp.player.life = gp.player.maxLife;
				}
				break;
			}

		}
	}

	public void draw(Graphics2D g2) {

		BufferedImage image = null;
		int tempScreenX = screenX;
		int tempScreenY = screenY;

		switch (direction) {
		case "up":
			if (attacking == false) {
				if (spriteNum == 1) {
					image = up1;
				}
				if (spriteNum == 2) {
					image = up2;
				}
			} else {
				// tempScreenY = screenY - gp.tileSize;
				if (spriteNum == 1) {
					image = attackUp1;
				}
				if (spriteNum == 2) {
					image = attackUp2;
				}
			}

			break;
		case "down":
			if (attacking == false) {
				if (spriteNum == 1) {
					image = down1;
				}
				if (spriteNum == 2) {
					image = down2;
				}
			} else {
				if (spriteNum == 1) {
					image = attackDown1;
				}
				if (spriteNum == 2) {
					image = attackDown2;
				}
			}
			break;
		case "left":
			if (attacking == false) {
				if (spriteNum == 1) {
					image = left1;
				}
				if (spriteNum == 2) {
					image = left2;
				}
			} else {
				
				if (spriteNum == 1) {
					image = attackLeft1;
				}
				if (spriteNum == 2) {
					image = attackLeft2;
				}
			}
			break;
		case "right":
			if (attacking == false) {
				if (spriteNum == 1) {
					image = right1;
				}
				if (spriteNum == 2) {
					image = right2;
				}
			} else {
				if (spriteNum == 1) {
					image = attackRight1;
				}
				if (spriteNum == 2) {
					image = attackRight2;
				}
			}
			break;
		}
		if (invincible == true) {
			g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.3f));
		}

		g2.drawImage(image, tempScreenX, tempScreenY, null);

		// reset alpha
		g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
		
	}

}
