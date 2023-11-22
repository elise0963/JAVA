package main;

import java.awt.BasicStroke;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.text.DecimalFormat;
import java.util.ArrayList;

import entity.Entity;
import object.Coin;
import object.Heart;

import object.Diamond;
import object.ManaCrystal;

public class UI {

	GamePanel gp;
	Graphics2D g2;
	Font arial_40, arial_80B;
	BufferedImage keyImage, coinImage,heart_full, heart_blank, heart_half,crystal_full, crystal_blank,blueheart;
	public boolean messageOn = false;
	
	ArrayList<String> message = new ArrayList<>();
	ArrayList<Integer> messageCounter = new ArrayList<>();
	double playTime;
	DecimalFormat dFormat = new DecimalFormat("#0.00");
	public String currentDialogue = "";
	public int commandNum = 0;
	public int titleScreenState = 0;

	
	// constructeur
	public UI(GamePanel gp) {
		this.gp = gp;

		arial_40 = new Font("Arial", Font.PLAIN, 40);
		arial_80B = new Font("Arial", Font.BOLD, 80);
		
		Entity coin =new Coin(gp);
		
		coinImage = coin.down1;
		//CREATE HEART OBJECT
		Entity heart = new Heart(gp);
		heart_full = heart.image;
		heart_half= heart.image2;
		heart_blank= heart.image3;
		//CREATE MANA OBJECT
		Entity crystal = new ManaCrystal(gp);
		crystal_full= crystal.image;
		crystal_blank= crystal.image2;
		//CREATE DIAMOND OBECTS
		Entity diamond = new Diamond(gp);
		blueheart = diamond.down1;
	}

	// display message
	public void addMessage(String text) {
		message.add(text);
		messageCounter.add(0);
	}

	public void draw(Graphics2D g2) {

		this.g2 = g2;

		g2.setFont(arial_40);
		g2.setColor(Color.white);

		// TILE STATE
		if (gp.gameState == gp.titleState) {
			drawTitleScreen();

		} else {
			// PLAY STATE
			if (gp.gameState == gp.playState) {
				drawPlayerLife();
				drawMessage();
				drawObjectsScreen();
			}
			// PAUSE STATE
			if (gp.gameState == gp.pauseState) {
				drawPlayerLife();
				drawPauseScreen();
			}
			// DIALOGUE STATE
			if (gp.gameState == gp.dialogueState) {
				drawPlayerLife();
				drawDialogueScreen();
			}
			// GAMEOVERSTATE
			if (gp.gameState == gp.gameOverState) {
				drawGameOverScreen();
			}
			// CHARACTERSTATE
			if (gp.gameState == gp.characterState) {
				drawcharacterScreen();
			}

			
		}

	}
	public void drawObjectsScreen() {
	
		int y = gp.tileSize/2;
		g2.setFont(arial_40);
		g2.setColor(Color.white);
		g2.drawImage(coinImage, gp.tileSize * 15, y, null);
		
		g2.drawString("x " + gp.player.hasCoin, gp.tileSize * 16, 65);

		
	}
	public void drawMessage() {
		int messageX = gp.tileSize;
		int messageY=gp.tileSize*4;
		
		g2.setFont(g2.getFont().deriveFont(Font.BOLD,32F));
		
		for (int i = 0; i< message.size();i++) {
			if (message.get(i)!= null) {
				g2.setColor(Color.white);
				g2.drawString(message.get(i),messageX,messageY);
				
				int counter = messageCounter.get(i) +1;
				messageCounter.set(i, counter);
				messageY+=50;
				
				if (messageCounter.get(i )> 180) {
					message.remove(i);
					messageCounter.remove(i);
				}
			}
		}
		
	}
	public void drawGameOverScreen() {
		gp.playSE(9);
		g2.setColor(new Color(0,0,0,150));
		g2.fillRect(0, 0, gp.screenWidth, gp.screenHeight);
		
		int x;
		int y;
		String text;
		g2.setFont(g2.getFont().deriveFont(Font.BOLD, 110f));
		
		text = "GAME OVER";
		//shadow
		g2.setColor(Color.BLACK);
		x=getXforCenteredText(text);
		y=gp.tileSize*4;
		g2.drawString(text, x, y);
		//Main
		g2.setColor(Color.white);
		g2.drawString(text, x-4, y-4);
		
		//retry
		g2.setFont(g2.getFont().deriveFont(50f));
		text = "Retry";
		x=getXforCenteredText(text);
		y+=gp.tileSize*4;
		g2.drawString(text, x, y);
		if (commandNum==0) {
			g2.drawString(">", x-40, y);
		}
		//Back to the title screen
		text= "Quit";
		x=getXforCenteredText(text);
		y+=55;
		g2.drawString(text, x, y);
		if (commandNum==1) {
			g2.drawString(">", x-40, y);
		}
	}

	public void drawPlayerLife() {
		//gp.player.life=2;
		int x = gp.tileSize/2;
		int y = gp.tileSize/2;
		int i=0;
		//DRAW MAX LIFE
		while (i<gp.player.maxLife/2) { //car 1 coeur est egale à 2 vies
			g2.drawImage(heart_blank, x, y, null);
			i++;
			x+=gp.tileSize;
		}
		
		//RESET
		x = gp.tileSize/2;
		y = gp.tileSize/2;
		i=0;
		//DRAW CURRENT LIFE
		while (i<gp.player.life) { //car 1 coeur est egale à 3 vies
			g2.drawImage(heart_half, x, y, null);
			i++;
			if (i<gp.player.life) {
				g2.drawImage(heart_full, x, y, null);
			}
			i++;
			x+=gp.tileSize;
		}
		//DRAW MAXMANA
		x = (gp.tileSize/2) -5;
		y = (int)(gp.tileSize*1.5);
		i=0;
		while (i< gp.player.maxMana) {
			g2.drawImage(crystal_blank, x, y, null);
			i++;
			x += 35;
		}
		
		//DRAW MANA
		x = (gp.tileSize/2) -5;
		y = (int)(gp.tileSize*1.5);
		i=0;
		while (i< gp.player.mana) {
			g2.drawImage(crystal_full, x, y, null);
			i++;
			x += 35;
		}
		
	}
	public void drawTitleScreen() {
		
		if (titleScreenState ==0) {
			g2.setColor(new Color(0,0,0));
			g2.fillRect(0, 0, gp.screenWidth, gp.screenHeight);
			
			// TITLE NAME
			g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 86F));
			String text = "MARS KILLER 2D";
			int x = getXforCenteredText(text);
			int y = gp.tileSize * 3;
			
			//SHADOW
			g2.setColor(Color.gray);
			g2.drawString(text, x+5,y+5);
			
			//MAIN COLOR
			g2.setColor(Color.white);
			g2.drawString(text, x, y);
			
			//Mars killer image
			x = gp.screenWidth / 2 -(gp.tileSize);
			y+= gp.tileSize*2;
			g2.drawImage(gp.player.down1, x, y, gp.tileSize*2, gp.tileSize*2, null);
			
			//MENU
			g2.setFont(g2.getFont().deriveFont(Font.BOLD, 48F));
			
			text = "NEW GAME";
			x = getXforCenteredText(text);
			y+= gp.tileSize*3;
			g2.drawString(text, x, y);
			if (commandNum ==0 ) {
				g2.drawString(">", x-gp.tileSize, y);
			}
		
			
			text = "QUIT";
			x = getXforCenteredText(text);
			y+= gp.tileSize;
			g2.drawString(text, x, y);
			if (commandNum ==1 ) {
				g2.drawString(">", x-gp.tileSize, y);
			}
		}
		
	}

	public void drawDialogueScreen() {
		
		// WINDOW
		int x = gp.tileSize * 2;
		int y = gp.tileSize / 2;
		int width = gp.screenWidth - (gp.tileSize * 4);
		int height = gp.tileSize * 2;
		drawSubWindow(x, y, width, height);

		g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 25F));
		x += gp.tileSize;
		y += gp.tileSize;

		for (String line : currentDialogue.split("\n")) {
			g2.drawString(line, x, y);
			y += 20;
		}

	}
	
	public void drawcharacterScreen() {
		
		//CREATE FRAME
		final int frameX = gp.tileSize;
		final int frameY=gp.tileSize;
		final int frameWidth=gp.tileSize*6;
		final int frameHeight=gp.tileSize*10;
		drawSubWindow(frameX, frameY,frameWidth,frameHeight);
		
		//TEXT
		g2.setColor(Color.white);
		g2.setFont(g2.getFont().deriveFont(32F));
		
		int textX = frameX + 20;
		int textY = frameY + gp.tileSize;
		final int lineHeight = 35;
		
		//NAMES
		g2.drawString("Level", textX, textY);
		textY += lineHeight;
		g2.drawString("Life", textX, textY);
		textY += lineHeight;
		g2.drawString("Mana", textX, textY);
		textY += lineHeight;
		g2.drawString("Attack", textX, textY);
		textY += lineHeight;
		g2.drawString("Exp", textX, textY);
		textY += lineHeight;
		g2.drawString("Next Level", textX, textY);
		textY += lineHeight;
		g2.drawString("Coin", textX, textY);
		textY += lineHeight + 10;
		
		//VALUES
		int tailX = (frameX + frameWidth) - 30;
		textY = frameY + gp.tileSize;
		String value;
		
		value = String.valueOf(gp.player.level);
		textX = getXforALignToRightText(value, tailX);
		g2.drawString(value, textX, textY);
		textY += lineHeight;
		
		value = String.valueOf(gp.player.life + "/" + gp.player.maxLife);
		textX = getXforALignToRightText(value, tailX);
		g2.drawString(value, textX, textY);
		textY += lineHeight;
		
		value = String.valueOf(gp.player.mana + "/" + gp.player.maxMana);
		textX = getXforALignToRightText(value, tailX);
		g2.drawString(value, textX, textY);
		textY += lineHeight;
		
		value = String.valueOf(gp.player.attack);
		textX = getXforALignToRightText(value, tailX);
		g2.drawString(value, textX, textY);
		textY += lineHeight;
		
		value = String.valueOf(gp.player.exp);
		textX = getXforALignToRightText(value, tailX);
		g2.drawString(value, textX, textY);
		textY += lineHeight;
		
		value = String.valueOf(gp.player.nextLevelExp);
		textX = getXforALignToRightText(value, tailX);
		g2.drawString(value, textX, textY);
		textY += lineHeight;
		
		value = String.valueOf(gp.player.coin);
		textX = getXforALignToRightText(value, tailX);
		g2.drawString(value, textX, textY);
		textY += lineHeight;
	}
	public void drawSubWindow(int x, int y, int width, int height) {
		Color c = new Color(0, 0, 0, 150);
		g2.setColor(c);
		g2.fillRoundRect(x, y, width, height, 35, 35);

		c = new Color(255, 255, 255);
		g2.setColor(c);
		g2.setStroke(new BasicStroke(5));
		g2.drawRoundRect(x + 5, y + 5, width - 10, height - 10, 25, 25);

	}

	public void drawPauseScreen() {

		g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 80f));
		String text = "PAUSED";
		int x = getXforCenteredText(text);
		int y = gp.screenHeight / 2;

		g2.drawString(text, x, y);
	}

	public int getXforCenteredText(String text) {

		int length = (int) g2.getFontMetrics().getStringBounds(text, g2).getWidth();
		int x = gp.screenWidth / 2 - length / 2;
		return x;
	}
	public int getXforALignToRightText(String text, int tailX) {

		int length = (int) g2.getFontMetrics().getStringBounds(text, g2).getWidth();
		int x = tailX-  length;
		return x;
	}

}
