package main;

import java.awt.Color;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import javax.swing.JPanel;

import entity.Entity;
import entity.Player;


import tile.TileManager;

@SuppressWarnings("serial")
public class GamePanel extends JPanel implements Runnable {

	// SCREEN SETTING
	final int originalTileSize = 16; // 16x16 title
	final int scale = 3;

	public final int tileSize = originalTileSize * scale; // 48*48
	public final int maxScreenCol = 20;
	public final int maxScreenRow = 12;
	public final int screenWidth = tileSize * maxScreenCol;// 768 pixels
	public final int screenHeight = tileSize * maxScreenRow;// 576 pixels

	// WORLD MAP SETTING
	public final int maxWorldCol = 50;
	public final int maxWorldRow = 50;
	public final int worldWidth = tileSize * maxWorldCol;
	public final int worldHeight = tileSize * maxWorldRow;

	// FOR FULL SCREEN
	int screenWidth2 = screenWidth;
	int screenHeight2 = screenHeight;
	BufferedImage tempScreen;
	Graphics2D g2;
	// FPS
	int FPS = 60;

	// SYSTEM
	public TileManager tileM = new TileManager(this);
	public KeyHandler keyH = new KeyHandler(this);
	Sound se = new Sound();// sound
	Sound music = new Sound();
	public CollisionChecker cChecker = new CollisionChecker(this);
	public AssetSetter aSetter = new AssetSetter(this);
	public UI ui = new UI(this);

	Thread gameThread;

	// ENTITY AND OBJECT
	public Player player = new Player(this, keyH); // player
	public Entity obj[] = new Entity[50]; // objects to add in the game
	public Entity npc[] = new Entity[10]; // npc
	public Entity monster[] = new Entity[40]; // monster
	ArrayList<Entity> entityList = new ArrayList<>();
	public ArrayList<Entity> projectileList = new ArrayList<>();

	// GAME STATE
	public int gameState;
	public final int playState = 1;
	public final int pauseState = 2;
	public final int dialogueState = 3;
	public final int titleState = 0;
	
	//public int level ;
	public final int gameOverState = 6;
	public final int characterState = 4;
	
	// EVENT HANDLER
	public EventHandler eHandler = new EventHandler(this);
	

	public GamePanel() {

		this.setPreferredSize(new Dimension(screenWidth, screenHeight));
		this.setBackground(Color.black);
		this.setDoubleBuffered(true);
		this.addKeyListener(keyH);
		this.setFocusable(true);
	}

	// setup game objects
	public void setupGame() {

		aSetter.setObject();
		aSetter.setNPC();
		keyH.setSelectedLevel(1);
		
		System.out.println("level before start: "+ player.level);//level);
		aSetter.setMonster();
		playMusic(0);
		gameState = titleState;
		tempScreen = new BufferedImage(screenWidth, screenHeight, BufferedImage.TYPE_INT_ARGB);
		g2 = (Graphics2D) tempScreen.getGraphics();
		
		
	}
	

	public void retry() {
		player.setDefaultPositions();
		player.restoreLifeAndMan();
		aSetter.setNPC();
		aSetter.setMonster();
		
		
	}
	public void restart() {
		
		player.setDefaultValues();
		aSetter.setObject();
		aSetter.setNPC();
		aSetter.setMonster();
		
	}
	public void setFullScreen() {
		
		GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
		GraphicsDevice gd= ge.getDefaultScreenDevice();
		gd.setFullScreenWindow(Main.window);
		
		//GET FULL SCREEN WIDTH AND HEIGHT
		screenWidth2 = Main.window.getWidth();
		screenHeight2= Main.window.getHeight();
	}

	public void startGameThread() {
		gameThread = new Thread(this);
		gameThread.start();
	}

	public void run() {
		// TODO Auto-generated method stub
		double drawInterval = 1000000000 / FPS; // 1 seconde divisée par 60 = 0.01666 on dessine l'ecran chaque 0.016
												// secondes
		double delta = 0;
		long lastTime = System.nanoTime();
		long currentTime;
		long timer = 0;
		int drawCount = 0;

		while (gameThread != null) {
			currentTime = System.nanoTime();

			delta += (currentTime - lastTime) / drawInterval;
			timer += (currentTime - lastTime);
			lastTime = currentTime;

			if (delta >= 1) {
				update();
				drawToTempScreen();
				drawToScreen();
				delta--;
				drawCount++;
			}

			if (timer >= 1000000000) {
				drawCount = 0;
				timer = 0;
			}
		}
	}

	// cette méthode met à jour la position du personnage sur l'écran
	public void update() {

		if (gameState == playState) {
			// PLAYER
			player.update();
			// NPC
			for (int i = 0; i < npc.length; i++) {
				if (npc[i] != null) {
					npc[i].update();
				}
			}
			// Monster
			for (int i = 0; i < monster.length; i++) {
				if (monster[i] != null) {
					if (monster[i].alive == true && monster[i].dying == false) {
						monster[i].update();
					}
					if (monster[i].alive == false) {
						monster[i] = null;
						;
					}
				}
			}
			// PROJECTILES
			for (int i = 0; i < projectileList.size(); i++) {
				if (projectileList.get(i) != null) {
					if (projectileList.get(i).alive == true ) {
						projectileList.get(i).update();
					}
					if (projectileList.get(i).alive == false) {
						projectileList.remove(i);
						;
					}
				}
			}
		}
		if (gameState == pauseState) {
			// player.update();
		}

	}

	public void drawToTempScreen() {
		// DEBUG
		long drawStart = 0;
		if (keyH.checkDrawTime == true) {
			drawStart = System.nanoTime();
		}
		// TITLE SCREEN
		if (gameState == titleState) {
			ui.draw(g2);
		} else { //OTHERS
			// tile
			tileM.draw(g2);
			// ADD ENTITIES TO THE LIST
			// PLAYER
			entityList.add(player);
			// NPC
			for (int i = 0; i < npc.length; i++) {
				if (npc[i] != null) {
					entityList.add(npc[i]);
				}
			}
			// MONSTER
			for (int i = 0; i < monster.length; i++) {
				if (monster[i] != null) {
					entityList.add(monster[i]);
				}
			}
			// object
			for (int i = 0; i < obj.length; i++) {
				if (obj[i] != null) {
					entityList.add(obj[i]);
				}
			}
			// projectile
			for (int i = 0; i < projectileList.size(); i++) {
				if (projectileList.get(i) != null) {
					entityList.add(projectileList.get(i) );
				}
			}
			// SORT
			Collections.sort(entityList, new Comparator<Entity>() {

				@Override
				public int compare(Entity e1, Entity e2) {
					int result = Integer.compare(e1.worldY, e2.worldY);
					return result;
				}
			});

			// DRAW ENTITIES
			for (int i = 0; i < entityList.size(); i++) {
				entityList.get(i).draw(g2);
			}

			// EMPTY ENTITY LIST

			entityList.clear();

			ui.draw(g2);
		}

		// DEBUG
		if (keyH.checkDrawTime == true) {
			long drawEnd = System.nanoTime();
			long passed = drawEnd - drawStart;
			g2.setColor(Color.white);
			g2.drawString("Draw Time: " + passed, 10, 400);
			System.out.println("Draw Time: " + passed);
		}
	}
	

	public void drawToScreen() {
		Graphics g = getGraphics();
		g.drawImage(tempScreen, 0, 0, screenWidth2, screenHeight2, null);
		g.dispose();
	}
	// cette méthode permet de jouer la musique
	public void playMusic(int i) {
		music.setFile(i);
		music.play();
		music.loop();
	}

	public void stopMusic() {
		music.stop();
	}

	public void playSE(int i) {
		se.setFile(i);
		se.play();
	}

}
